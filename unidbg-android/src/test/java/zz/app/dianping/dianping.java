package zz.app.dianping;

import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.linux.android.dvm.jni.ProxyDvmObject;
import com.sun.org.apache.bcel.internal.generic.SWITCH;
import zz.app.base.AppInfo;
import zz.app.base.BaseAbstractJni;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 参考龙哥unidbg基本使用（六）：https://www.yuque.com/lilac-2hqvv/xdwlsg/avlc01hrko6yvr26
 */

public class dianping extends BaseAbstractJni {

    dianping() {

        //1.App常量
        String projectName = "dianping";
        String apkFileName =  "dianping.apk";
        String bundleName = "com.dianping.v1";
        String soName = "mtguard";
        String clsName = "com/meituan/android/common/mtguard/NBridge$SIUACollector";
        AppInfo appInfo = new AppInfo(false, projectName, apkFileName, bundleName, soName, clsName);

        List<AppInfo.VirtualModuleName> virtualModules = new ArrayList<>();
        virtualModules.add(AppInfo.VirtualModuleName.AndroidModule);
        virtualModules.add(AppInfo.VirtualModuleName.JniGraphics);
        appInfo.addDependLibrary(virtualModules, null);

        build(appInfo, null);

    }

    public static void main(String[] args) {
        dianping test = new dianping();
        System.err.println("env_info = " + test.getEnvironmentInfo());
        System.err.println("env_info_ext = " + test.getEnvironmentInfoExtra());

    }

    public String getEnvironmentInfo(){
        String result = nativeAPI.newObject(null).callJniMethodObject(emulator, "getEnvironmentInfo()Ljava/lang/String;").getValue().toString();
        return result;
    }

    public String getEnvironmentInfoExtra(){
        String result = nativeAPI.newObject(null).callJniMethodObject(emulator, "getEnvironmentInfoExtra()Ljava/lang/String;").getValue().toString();
        return result;
    }


    @Override
    public DvmObject<?> allocObject(BaseVM vm, DvmClass dvmClass, String signature) {
        switch (signature) {
            case "java/lang/StringBuilder->allocObject": {
                return ProxyDvmObject.createObject(vm, new StringBuilder());
            }

        }
        return super.allocObject(vm, dvmClass, signature);
    }

    @Override
    public void callVoidMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "java/lang/StringBuilder-><init>()V": {
                return;
            }
        }
        super.callVoidMethodV(vm, dvmObject, signature, vaList);
    }

    @Override
    public DvmObject<?> callObjectMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "com/meituan/android/common/mtguard/NBridge$SIUACollector->getEnvironmentInfo()Ljava/lang/String;": {
                return new StringObject(vm, "0|0|0|-|0|");
            }
            case "java/lang/StringBuilder->append(Ljava/lang/String;)Ljava/lang/StringBuilder;": {
                StringBuilder str = (StringBuilder) dvmObject.getValue();
                str.append(vaList.getObjectArg(0).getValue().toString());
                return ProxyDvmObject.createObject(vm, str);
            }
            case "com/meituan/android/common/mtguard/NBridge$SIUACollector->isVPN()Ljava/lang/String;": {
                return new StringObject(vm, "0");
            }
            case "com/meituan/android/common/mtguard/NBridge$SIUACollector->brightness(Landroid/content/Context;)Ljava/lang/String;": {
                return new StringObject(vm, "0.8");
            }
            case "com/meituan/android/common/mtguard/NBridge$SIUACollector->systemVolume(Landroid/content/Context;)Ljava/lang/String;": {
                return new StringObject(vm, "0");
            }
            case "java/lang/StringBuilder->toString()Ljava/lang/String;": {
                StringBuilder str = (StringBuilder) dvmObject.getValue();
                return new StringObject(vm, str.toString());
            }
        }

        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    }


    @Override
    public DvmObject<?> getObjectField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        switch (signature) {
            case "com/meituan/android/common/mtguard/NBridge$SIUACollector->mContext:Landroid/content/Context;": {
                return vm.resolveClass("android/content/Context").newObject(null);
            }
        }
        return super.getObjectField(vm, dvmObject, signature);
    }

    @Override
    public boolean callBooleanMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "com/meituan/android/common/mtguard/NBridge$SIUACollector->isAccessibilityEnable()Z": {
                return false;
            }
        }
        return super.callBooleanMethodV(vm, dvmObject, signature, vaList);
    }

    @Override
    public DvmObject<?> callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "java/lang/String->valueOf(I)Ljava/lang/String;": {
                String str = String.valueOf(vaList.getIntArg(0));
                return new StringObject(vm, str);
            }
        }
        return super.callStaticObjectMethodV(vm, dvmClass, signature, vaList);
    }

    @Override
    public int callIntMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "com/meituan/android/common/mtguard/NBridge$SIUACollector->uiAutomatorClickCount()I": {
                return 0;
            }
        }
        return super.callIntMethodV(vm, dvmObject, signature, vaList);
    }
}
