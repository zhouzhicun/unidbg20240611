package zz.app.zuiyou;

import com.github.unidbg.linux.ARM32SyscallHandler;
import com.github.unidbg.linux.AndroidSyscallHandler;
import com.github.unidbg.linux.android.dvm.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import zz.app.base.AppInfo;
import zz.app.base.BaseAbstractJni;
import zz.app.base.BaseAbstractJniHelper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class zuiyou extends BaseAbstractJni {

    zuiyou() {

        //1.App常量
        String bundleName = "com.xiaochuankeji.tieba";
        String rootfs = "zuiyou/rootfs";
        String rootresource = "zuiyou/resource";
        String apkPath =  "zuiyou/zuiyou.apk";
        String soName = "libnet_crypto.so";
        String clsName = "com.sina.weibo.security.WeiboSecurityUtils";   //支持.或者 / 分隔。

        AppInfo appInfo = new AppInfo(false, bundleName, rootfs, rootresource, apkPath, soName, clsName);
        build(appInfo, null);

    }


    /************************************ 函数调用 *************************************/

    public static void main(String[] args) throws Exception {

        //添加日志
        Logger.getLogger(ARM32SyscallHandler.class).setLevel(Level.DEBUG);
        Logger.getLogger(AndroidSyscallHandler.class).setLevel(Level.DEBUG);


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

        Map<String, String> map = new HashMap<>();
        map.put("one", "111");
        map.put("two", "222");
        params.add(map);

        Number signHash = callJNIFuncV2(0x4a28d, params);
        return vm.getObject(signHash.intValue()).getValue().toString();
    }


    /************************************ 补环境 *************************************/


    @Override
    public DvmObject<?> callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "com/izuiyou/common/base/BaseApplication->getAppContext()Landroid/content/Context;":
                return BaseAbstractJniHelper.getContext();
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
        String clsName = dvmObject.getObjectType().getClassName();
        System.out.println("clsName=" + clsName);
        switch (signature) {
            case "android/content/Context->getClass()Ljava/lang/Class;":{
                return BaseAbstractJniHelper.getClass(dvmObject);
            }
            case "java/lang/Class->getSimpleName()Ljava/lang/String;":{
                return new StringObject(vm, "AppController");
            }
            case "android/content/Context->getFilesDir()Ljava/io/File;":
            case "java/lang/String->getAbsolutePath()Ljava/lang/String;": {
                return BaseAbstractJniHelper.getAppFilesDir();
            }
        }
        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    }

}
