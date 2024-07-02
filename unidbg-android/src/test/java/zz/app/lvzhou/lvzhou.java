package zz.app.lvzhou;

import com.github.unidbg.linux.android.dvm.array.ByteArray;
import zz.app.template.AppInfo;
import zz.app.template.BaseAbstractJni;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class lvzhou extends BaseAbstractJni {

    lvzhou() {

        //1.App常量
        String bundleName = "com.sina.oasis";
        String apkPath =  "lvzhou/lvzhou.apk";
        String soName = "oasiscore";
        String clsName = "com/sina/weibo/security/WeiboSecurityUtils";

        AppInfo appInfo = new AppInfo(false, bundleName, apkPath, soName, clsName);
        build(appInfo, null);

    }

    public static void main(String[] args) {
        lvzhou test = new lvzhou();
        System.err.println("sign = " + test.getS());
    }

    public String getS(){

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

        List<Object> list = new ArrayList<>(10);
        list.add(vm.addLocalObject(inputByteArray));
        list.add(0);

        Number number = callJNIFunc(0xC365, list);
        String result = vm.getObject(number.intValue()).getValue().toString();
        return result;
    }

}
