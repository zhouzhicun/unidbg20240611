package zz.app.moji;

import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import zz.app.base.AppInfo;
import zz.app.base.BaseAbstractJni;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class moji extends BaseAbstractJni {

    moji() {

        //1.App常量
        String bundleName = "com.moji.mjweather";
        String rootfs = "moji/rootfs";
        String rootresource = "moji/resource";
        String apkPath =  "moji/moji.apk";
        String soName = "encrypt";
        String clsName = "com.moji.mjweather.library.Digest";

        AppInfo appInfo = new AppInfo(true, bundleName, rootfs, rootresource, apkPath, soName, clsName);
        build(appInfo, null);

    }

    public static void main(String[] args) {
        moji test = new moji();

        System.err.println("sign = " + test.call_sign());
    }

    public String call_sign() {
        System.err.println("开始 call sign: ");
        // arg3 bytes
        String input = "{\"common\":{\"platform\":\"Android\",\"identifier\":\"\",\"app_version\":\"1009087802\",\"os_version\":\"31\",\"device\":\"Pixel 4\",\"brand\":\"google\",\"pid\":\"5068\",\"language\":\"CN\",\"uid\":\"968403975009353785\",\"uaid\":\"968403975172931586\",\"width\":1080,\"height\":2236,\"package_name\":\"com.moji.mjweather\",\"amp\":\"1725432467598\",\"locationcity\":1,\"current_city\":33,\"token\":\"0b3d96327296374be7af09cf92d92295\",\"vip\":\"0\",\"weather_tab_style\":0,\"giuid\":\"gtc_97da812cf686a7a19d79e40fc4b97d8232\",\"smid\":\"DUwXaIhvLQDepEEOAgqF5yLJ-5V4vD8Dpk86\",\"security_request\":0,\"net\":\"wifi\"},\"params\":{\"mobile\":\"EdiOaYg8uVwFTpYfiKiFOg==\",\"is_sercret\":1}}";
        List<Object> list = new ArrayList<>(10);
        list.add(vm.addLocalObject(new StringObject(vm, input)));

        Number number = callJNIFunc(0x3D1A0, list);
        String result = vm.getObject(number.intValue()).getValue().toString();
        return result;
    }


    public void addBreakPoint() {
        //emulator.attach().addBreakPoint(module, 0x8AB2 + 1);  //arm32需要+1
    }

    @Override
    public DvmObject<?> callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        switch (signature) {
            case "com/moji/tool/AppDelegate->getAppContext()Landroid/content/Context;": {
                return vm.resolveClass("com.view.mjweather.MJApplication").newObject(null);
            }
            case "android/os/ServiceManager->getService(Ljava/lang/String;)Landroid/os/IBinder;": {
                return null;
            }
        }
        return super.callStaticObjectMethodV(vm, dvmClass, signature, vaList);
    }

    @Override
    public DvmObject<?> getStaticObjectField(BaseVM vm, DvmClass dvmClass, String signature) {
        switch (signature) {
            case "android/app/ActivityThread->sCurrentActivityThread:Landroid/app/ActivityThread;": {
                return vm.resolveClass("android/app/ActivityThread").newObject(null);
            }
        }
        return super.getStaticObjectField(vm, dvmClass, signature);
    }

    @Override
    public void setStaticObjectField(BaseVM vm, DvmClass dvmClass, String signature, DvmObject<?> value) {
        switch (signature) {
            case "android/app/ActivityThread->sPackageManager:Landroid/content/pm/IPackageManager;": {
                return;
            }
        }
        super.setStaticObjectField(vm, dvmClass, signature, value);
    }

    @Override
    public DvmObject<?> callObjectMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "com/view/mjweather/MJApplication->getPackageManager()Landroid/content/pm/PackageManager;": {
                return vm.resolveClass("android.content.pm.PackageManager").newObject(null);
            }
            case "com/view/mjweather/MJApplication->getPackageName()Ljava/lang/String;": {
                return new StringObject(vm, "com.moji.mjweather");
            }
        }
        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    }





}
