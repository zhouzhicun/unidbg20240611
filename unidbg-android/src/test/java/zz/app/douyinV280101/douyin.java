package zz.app.douyinV280101;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Module;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.AbstractJni;
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.memory.Memory;

import java.io.File;

public class douyin extends AbstractJni {

    private AndroidEmulator emulator;
    private VM vm;
    private Module module;
    private DvmClass nativeAPI;

    douyin() {

        //1.App常量
        String processName = "com.nike.omega";
        String apkPath = "lvzhou.apk";
        String soName = "oasiscore";  //從apk加載，不需要lib前綴和.so。
        String clsName = "com/sina/weibo/security/WeiboSecurityUtils";

        //2.創建emulator
        emulator = AndroidEmulatorBuilder
                .for64Bit()
                .addBackendFactory(new Unicorn2Factory(true))
                .setProcessName(processName)
                .build();

        //3.多線程支持+IO
        emulator.getSyscallHandler().setEnableThreadDispatcher(true);
        emulator.getBackend().registerEmuCountHook(100000);
        //emulator.getSyscallHandler().addIOResolver(new dyResolver());

        //4.Memory初始化
        Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));

        //5.創建VM
        vm = emulator.createDalvikVM(new File(apkPath));
        vm.setJni(this);
        vm.setVerbose(true);

        //7.獲取class
        DalvikModule dm = vm.loadLibrary(soName, true);
        nativeAPI = vm.resolveClass(clsName);
        dm.callJNI_OnLoad(emulator);

    }

    public static void main(String[] args) {
        douyin test = new douyin();
    }



}
