package zz.app.base;

import com.github.unidbg.Emulator;
import com.github.unidbg.linux.android.SystemPropertyHook;
import com.github.unidbg.linux.android.SystemPropertyProvider;

public class Utils {

    //創建systemPropertyHook
    public static SystemPropertyHook createSystemPropertyHook(Emulator<?> emulator) {

        SystemPropertyHook systemPropertyHook = new SystemPropertyHook(emulator);
        systemPropertyHook.setPropertyProvider(new SystemPropertyProvider() {
            @Override
            public String getProperty(String key) {
                System.out.println("lilac systemkey:" + key);
                switch (key) {
                    case "net.hostname":{
                        return "MIX2S-zhongeryayadeM";
                    }
                    case "ro.serialno":{
                        return "f8a995f5";
                    }
                    case "ro.boot.serialno":{
                        return "f8a995f5";
                    }
                    case "ro.product.brand":{
                        return "Xiaomi";
                    }
                    case "ro.product.manufacturer":{
                        return "Xiaomi";
                    }
                    case "ro.product.model":{
                        return "MIX 2S";
                    }
                    case "ro.product.cpu.abi":{
                        return "arm64-v8a";
                    }
                    case "ro.product.cpu.abilist":{
                        return "arm64-v8a,armeabi-v7a,armeabi";
                    }
                    case "ro.boot.vbmeta.digest":{
                        return null;
                    }
                    case "init.svc.droid4x":{
                        return null;
                    }
                }
                return "";
            }
        });

        return systemPropertyHook;

    }


}
