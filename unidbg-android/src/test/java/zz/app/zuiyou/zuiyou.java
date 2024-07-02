package zz.app.zuiyou;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Module;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class zuiyou extends AbstractJni {

    private AndroidEmulator emulator;
    private VM vm;
    private Module module;
    private DvmClass nativeAPI;

    private String apkBasePath = "unidbg-android/src/test/java/zz/app";

    zuiyou() {

        //1.App常量
        String processName = "com.xiaochuankeji.tieba";
        String apkPath =  apkBasePath + "/zuiyou/zuiyou.apk";
        String soName = "net_crypto";
        String clsName = "com/sina/weibo/security/WeiboSecurityUtils";

        //2.創建emulator
        emulator = AndroidEmulatorBuilder
                .for32Bit()
                .addBackendFactory(new Unicorn2Factory(true))
                .setProcessName(processName)
                .build();

        //3.多線程支持+IO
        emulator.getSyscallHandler().setEnableThreadDispatcher(true);
        emulator.getBackend().registerEmuCountHook(100000);

        //補文件
        //emulator.getSyscallHandler().addIOResolver(new DYIOResolver());

        //4.Memory初始化
        Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));

        //5.創建VM
        vm = emulator.createDalvikVM(new File(apkPath));
        vm.setJni(this);
        vm.setVerbose(true);

        //7.獲取class
        DalvikModule dm = vm.loadLibrary(soName, true);
        module = dm.getModule();
        dm.callJNI_OnLoad(emulator);

        //nativeAPI = vm.resolveClass(clsName);

    }

    public static void main(String[] args) throws Exception {
        zuiyou test = new zuiyou();
        test.native_init();
        String sign = test.callSign();
        System.err.println("sign=" + sign);
    }

    public void native_init(){
        // 0x4a069
        List<Object> list = new ArrayList<>(10);
        list.add(vm.getJNIEnv()); // 第一个参数是env
        list.add(0); // 第二个参数，实例方法是jobject，静态方法是jclass，直接填0，一般用不到。
        module.callFunction(emulator, 0x4a069, list.toArray());
    }


    private String callSign(){
        // 准备入参
        List<Object> list = new ArrayList<>(10);
        list.add(vm.getJNIEnv()); // 第一个参数是env
        list.add(0); // 第二个参数，实例方法是jobject，静态方法是jclass，直接填0，一般用不到。
        list.add(vm.addLocalObject(new StringObject(vm, "12345")));
        ByteArray plainText = new ByteArray(vm, "r0ysue".getBytes(StandardCharsets.UTF_8));
        list.add(vm.addLocalObject(plainText));
        Number number = module.callFunction(emulator, 0x4a28D, list.toArray());
        return vm.getObject(number.intValue()).getValue().toString();
    }



    @Override
    public DvmObject<?> callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "com/izuiyou/common/base/BaseApplication->getAppContext()Landroid/content/Context;":
                return vm.resolveClass("android/content/Context").newObject(null);
        }
        return super.callStaticObjectMethodV(vm, dvmClass, signature, vaList);
    }

    @Override
    public boolean callStaticBooleanMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature){
            case "android/os/Debug->isDebuggerConnected()Z":{
                return false;
            }
        }
        throw new UnsupportedOperationException(signature);
    }

    @Override
    public int callStaticIntMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature){
            case "android/os/Process->myPid()I":{
                return emulator.getPid();
            }

        }
        throw new UnsupportedOperationException(signature);
    }


    @Override
    public DvmObject<?> callObjectMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "android/content/Context->getClass()Ljava/lang/Class;":{
                return dvmObject.getObjectType();
            }
            case "java/lang/Class->getSimpleName()Ljava/lang/String;":{
                return new StringObject(vm, "AppController");
            }
            case "android/content/Context->getFilesDir()Ljava/io/File;":
            case "java/lang/String->getAbsolutePath()Ljava/lang/String;": {
                return new StringObject(vm, "/data/user/0/cn.xiaochuankeji.tieba/files");
            }
        }
        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    }

}
