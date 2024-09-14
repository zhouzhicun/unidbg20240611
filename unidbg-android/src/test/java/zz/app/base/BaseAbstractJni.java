package zz.app.base;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Emulator;
import com.github.unidbg.Module;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.file.linux.AndroidFileIO;
import com.github.unidbg.linux.ARM32SyscallHandler;
import com.github.unidbg.linux.ARM64SyscallHandler;
import com.github.unidbg.linux.AndroidSyscallHandler;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.SystemPropertyHook;
import com.github.unidbg.linux.android.SystemPropertyProvider;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.jni.ProxyDvmObject;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.virtualmodule.android.AndroidModule;
import com.github.unidbg.virtualmodule.android.JniGraphics;
import com.github.unidbg.virtualmodule.android.MediaNdkModule;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import zz.app.base.files.AppProcFile;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseAbstractJni extends AbstractJni {

    public AppInfo appInfo;
    public AndroidEmulator emulator;
    public VM vm;
    public Module module;
    public DvmClass nativeAPI;

    /******************************* build ****************************************/

    public void build(AppInfo appInfo, AndroidEmulatorBuilder builder) {

        this.appInfo = appInfo;

        //判断是否有传入builder，没有则創建builder
        if(builder == null) {
            if (appInfo.is64Bit) {
                builder = AndroidEmulatorBuilder.for64Bit();
            } else {
                builder = AndroidEmulatorBuilder.for32Bit();
            }
        }

        emulator = builder.addBackendFactory(new Unicorn2Factory(true))
                .setProcessName(appInfo.bundleName)
                .setRootDir(new File(appInfo.rootfs))
                .build();

        //IO补环境, 支持多个resolver, 且后添加的优先级更高。
        if(appInfo.ioResolvers != null && !appInfo.ioResolvers.isEmpty()) {
            for (IOResolver<AndroidFileIO> resolver : appInfo.ioResolvers) {
                emulator.getSyscallHandler().addIOResolver(resolver);
            }
        }

        commonBuild();

        //更新全局变量
        BaseAbstractJniHelper.abstractJni = this;
        AppProcFile.updatePid();

    }


    public void commonBuild() {

        //3.多線程支持
        emulator.getSyscallHandler().setEnableThreadDispatcher(true);
        emulator.getBackend().registerEmuCountHook(100000);

        //4.Memory初始化
        Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));

        //5.創建VM
        vm = emulator.createDalvikVM(new File(appInfo.apkPath));
        vm.setJni(this);
        vm.setVerbose(true);

        //预加載依賴的Library
        preLoadLibrary();

        //添加moduleListener
        if(appInfo.moduleListener != null) {
            memory.addModuleListener(appInfo.moduleListener);
        }

        //6.獲取class
        DalvikModule dm = vm.loadLibrary(appInfo.soName, true);
        module = dm.getModule();
        dm.callJNI_OnLoad(emulator);
        nativeAPI = vm.resolveClass(appInfo.clsName);
    }

    private void preLoadLibrary() {

        Memory memory = emulator.getMemory();

        //添加sysPropertyHook
        if (appInfo.systemProperties != null) {
            SystemPropertyHook propertyHook = createSystemPropertyHook(emulator);
            memory.addHookListener(propertyHook);
        }


        //1.预加载virtualModule
        if (appInfo.virtualLibrarys != null) {
            for (AppInfo.VirtualModuleName moduleName: appInfo.virtualLibrarys) {
                switch (moduleName) {
                    case AndroidModule: {
                        new AndroidModule(emulator, vm).register(memory);
                        break;
                    }
                   case JniGraphics: {
                       new JniGraphics(emulator, vm).register(memory);
                       break;
                   }
                   case MediaNdkModule: {
                       new MediaNdkModule(emulator, vm).register(memory);
                       break;
                   }
                }
            }
        }

        //2.预加载依赖library
        if(appInfo.dependLibrarys != null) {
            for (String libName : appInfo.dependLibrarys) {
                vm.loadLibrary(libName, true);
            }
        }
    }





    //******************************* helper ****************************************

    //启用日志
    public void enableLog() {
        if (appInfo.is64Bit) {
            Logger.getLogger(ARM64SyscallHandler.class).setLevel(Level.DEBUG);
        } else {
            Logger.getLogger(ARM32SyscallHandler.class).setLevel(Level.DEBUG);
        }
        Logger.getLogger(AndroidSyscallHandler.class).setLevel(Level.DEBUG);
    }

    //主动调用JNI函数时，所有参数都要调用vm.addLocalObject() 将对象hash添加到list中。
    public Number callJNIFunc(long offset, List<Object> params) {

        List<Object> resultParams = createParams();
        if(params != null && !params.isEmpty()) {
            resultParams.addAll(params);
        }
        return module.callFunction(emulator, offset, resultParams.toArray());
    }


    public Number callJNIFuncV2(long offset, List<Object> params) {

        List<Object> resultParams = createParams();
        if(params != null && !params.isEmpty()) {
            for (Object param : params) {
                if (param instanceof Boolean) {         // boolean
                    resultParams.add((Boolean) param ? VM.JNI_TRUE : VM.JNI_FALSE);
                } else if(param instanceof DvmObject) { // dvm object
                    resultParams.add(param.hashCode());
                    vm.addLocalObject((DvmObject<?>) param);
                }  else {   // 其他
                    DvmObject<?> dvmObject = ProxyDvmObject.createObject(vm, param);
                    if(dvmObject == null) {
                        System.err.println("参数类型不支持，请完善 callJNIFuncV2 方法");
                        System.err.println("curParam = " + param.toString());
                        return null;
                    }
                    resultParams.add(dvmObject.hashCode());
                    vm.addLocalObject(dvmObject);
                }

            }
        }
        return module.callFunction(emulator, offset, resultParams.toArray());
    }


    /******************************* private ****************************************/

    private List<Object> createParams() {
        List<Object> list = new ArrayList<>(10);
        list.add(vm.getJNIEnv());   // 第一个参数是env
        list.add(0);                // 第二个参数，实例方法是jobject，静态方法是jclass，直接填0，一般用不到。
        return list;
    }


    //創建systemPropertyHook
    private SystemPropertyHook createSystemPropertyHook(Emulator<?> emulator) {

        SystemPropertyHook systemPropertyHook = new SystemPropertyHook(emulator);
        systemPropertyHook.setPropertyProvider(new SystemPropertyProvider() {
            @Override
            public String getProperty(String key) {
                System.err.println("zz getSystemProperty ==> key:" + key);
                if (appInfo.systemProperties.containsKey(key)) {
                    return appInfo.systemProperties.get(key);
                }
                return "";
            }
        });

        return systemPropertyHook;
    }
}
