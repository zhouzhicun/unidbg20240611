package zz.app.lvzhou;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Module;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.AbstractJni;
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class lvzhou extends AbstractJni {

    private AndroidEmulator emulator;
    private VM vm;
    private Module module;
    private DvmClass nativeAPI;

    private String apkBasePath = "unidbg-android/src/test/java/zz/app";

    lvzhou() {

        //1.App常量
        String processName = "com.sina.oasis";
        String apkPath =  apkBasePath + "/lvzhou/lvzhou.apk";
        String soName = "oasiscore";
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
        nativeAPI = vm.resolveClass(clsName);
        dm.callJNI_OnLoad(emulator);

    }

    public static void main(String[] args) {
        lvzhou test = new lvzhou();
        System.out.println(test.getS());
    }

    public String getS(){
        // args list
        List<Object> list = new ArrayList<>(10);
        // arg1 env
        list.add(vm.getJNIEnv());
        // arg2 jobject/jclazz 一般用不到，直接填0
        list.add(0);
        // arg3 bytes
        String input = "aid=01A-khBWIm48A079Pz_DMW6PyZR8" +
                "uyTumcCNm4e8awxyC2ANU.&cfrom=28B529501" +
                "0&cuid=5999578300&noncestr=46274W9279Hr1" +
                "X49A5X058z7ZVz024&platform=ANDROID&timestamp" +
                "=1621437643609&ua=Xiaomi-MIX2S__oasis__3.5.8_" +
                "_Android__Android10&version=3.5.8&vid=10190135" +
                "94003&wm=20004_90024";
        byte[] inputByte = input.getBytes(StandardCharsets.UTF_8);
        ByteArray inputByteArray = new ByteArray(vm,inputByte);
        list.add(vm.addLocalObject(inputByteArray));
        // arg4 ,boolean false 填入0
        list.add(0);
        // 参数准备完成
        // call function
        Number number = module.callFunction(emulator, 0xC365, list.toArray());
        String result = vm.getObject(number.intValue()).getValue().toString();
        return result;
    }


}
