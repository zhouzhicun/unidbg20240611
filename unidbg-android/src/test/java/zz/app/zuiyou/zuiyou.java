package zz.app.zuiyou;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Module;
import com.github.unidbg.arm.backend.Unicorn2Factory;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;
import zz.app.template.AppInfo;
import zz.app.template.BaseAbstractJni;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class zuiyou extends BaseAbstractJni {

    zuiyou() {

        //1.App常量
        String bundleName = "com.xiaochuankeji.tieba";
        String apkPath =  "zuiyou/zuiyou.apk";
        String soName = "libnet_crypto.so";
        String clsName = "com/sina/weibo/security/WeiboSecurityUtils";
        AppInfo appInfo = new AppInfo(false, bundleName, apkPath, soName, clsName);

        build(appInfo, null);

    }


    /************************************ 函数调用 *************************************/

    public static void main(String[] args) throws Exception {
        zuiyou test = new zuiyou();
        test.call_init();
        String sign = test.call_sign();
        System.err.println("sign=" + sign);
    }


    public void call_init(){
        callJNIFunc(0x4a069, null);
    }

    public String call_sign(){

        List<Object> params = new ArrayList<>(10);

        //调用callJNIFunc
//        StringObject param1 = new StringObject(vm, "123456");
//        ByteArray param2 = new ByteArray(vm, "hello".getBytes(StandardCharsets.UTF_8));
//        params.add(vm.addLocalObject(param1));
//        params.add(vm.addLocalObject(param2));


        //调用callJNIFuncV2
        String param1 = "123456";
        byte[] param2 = param1.getBytes(StandardCharsets.UTF_8);
        params.add(param1);
        params.add(param2);

        Number signHash = callJNIFuncV2(0x4a28d, params);
        return vm.getObject(signHash.intValue()).getValue().toString();
    }


    /************************************ 补环境 *************************************/


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
