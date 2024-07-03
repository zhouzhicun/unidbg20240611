package zz.app.template;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Emulator;
import com.github.unidbg.Module;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.file.linux.AndroidFileIO;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.jni.ProxyDvmObject;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.pointer.UnidbgPointer;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class BaseAbstractJni extends AbstractJni {

    protected AppInfo appInfo;

    protected AndroidEmulator emulator;
    protected VM vm;
    protected Module module;
    protected DvmClass nativeAPI;

    /******************************* build ****************************************/

    public void build(AppInfo appInfo, List<IOResolver<AndroidFileIO> > resolvers) {

        //1.創建emulator
        AndroidEmulatorBuilder builder = null;
        if (appInfo.is64Bit) {
            builder = AndroidEmulatorBuilder.for64Bit();
        } else {
            builder = AndroidEmulatorBuilder.for32Bit();
        }

        build(appInfo, resolvers, builder);

    }

    //arm64: ARM64SyscallHandler, arm32: ARM32SyscallHandler
    public void build(AppInfo appInfo, List<IOResolver<AndroidFileIO>> resolvers, AndroidEmulatorBuilder builder) {

        this.appInfo = appInfo;

        emulator = builder.addBackendFactory(new Unicorn2Factory(true))
                .setProcessName(appInfo.bundleName)
                .build();

        //IO补环境, 支持多个resolver, 且后添加的优先级更高。
        if(resolvers != null && !resolvers.isEmpty()) {
            for (IOResolver<AndroidFileIO> resolver : resolvers) {
                emulator.getSyscallHandler().addIOResolver(resolver);
            }
        }

        commonBuild();

    }

//    public AndroidEmulatorBuilder createCustomBuilder() {
//
//        AndroidEmulatorBuilder builder = null;
//        builder = new AndroidEmulatorBuilder(true){
//            @Override
//            public AndroidEmulator build() {
//                return new AndroidARMEmulator(processName,rootDir,backendFactories) {
//                    @Override
//                    protected UnixSyscallHandler<AndroidFileIO> createSyscallHandler(SvcMemory svcMemory) {
//                        return new DemoARM64SyscallHandler(svcMemory);
//                    }
//                };
//            }
//        };
//
//        return builder;
//    }



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

        //6.獲取class
        DalvikModule dm = vm.loadLibrary(appInfo.soName, true);
        module = dm.getModule();
        dm.callJNI_OnLoad(emulator);
        nativeAPI = vm.resolveClass(appInfo.clsName);
    }




    /******************************* helper ****************************************/

    private List<Object> createParams() {
        List<Object> list = new ArrayList<>(10);
        list.add(vm.getJNIEnv());   // 第一个参数是env
        list.add(0);                // 第二个参数，实例方法是jobject，静态方法是jclass，直接填0，一般用不到。
        return list;
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
                if (param instanceof Boolean) {
                    resultParams.add((Boolean) param ? VM.JNI_TRUE : VM.JNI_FALSE);
                    continue;
                } else if (param instanceof String ||
                        param instanceof byte[] ||
                        param instanceof short[] ||
                        param instanceof int[] ||
                        param instanceof float[] ||
                        param instanceof double[] ||
                        param instanceof Enum) {
                    DvmObject<?> obj = ProxyDvmObject.createObject(vm, param);
                    resultParams.add(obj.hashCode());
                    vm.addLocalObject(obj);
                    continue;
                } else if(param instanceof Hashable) {

                    resultParams.add(param.hashCode()); // dvm object
                    if(param instanceof DvmObject) {
                        vm.addLocalObject((DvmObject<?>) param);
                    }
                    continue;
                }
                resultParams.add(param);
            }
        }
        return module.callFunction(emulator, offset, resultParams.toArray());
    }

}
