package zz.app.base;

import java.util.HashMap;
import java.util.Map;

public class SystemProperty {

    public static final String net_hostname = "net.hostname";
    public static final String ro_serialno = "ro.serialno";
    public static final String ro_boot_serialno = "ro.boot.serialno";
    public static final String ro_product_brand = "ro.product.brand";
    public static final String ro_product_manufacturer = "ro.product.manufacturer";
    public static final String ro_product_model = "ro.product.model";
    public static final String ro_product_cpu_abi = "ro.product.cpu.abi";
    public static final String ro_product_cpu_abilist = "ro.product.cpu.abilist";
    public static final String ro_boot_vbmeta_digest = "ro.boot.vbmeta.digest";
    public static final String init_svc_droid4x = "init.svc.droid4x";


    public static Map<String, String> defaultARM64SystemProperty() {

        Map<String, String > systemPropertyMap = new HashMap<>();
        systemPropertyMap.put(net_hostname, "MIX2S-zhongeryayadeM");
        systemPropertyMap.put(ro_serialno, "f8a995f5");
        systemPropertyMap.put(ro_boot_serialno, "f8a995f5");
        systemPropertyMap.put(ro_product_brand, "Xiaomi");

        systemPropertyMap.put(ro_product_manufacturer, "Xiaomi");
        systemPropertyMap.put(ro_product_model, "MIX 2S");
        systemPropertyMap.put(ro_product_cpu_abi, "arm64-v8a");
        systemPropertyMap.put(ro_product_cpu_abilist, "arm64-v8a,armeabi-v7a,armeabi");

        systemPropertyMap.put(ro_boot_vbmeta_digest, "");
        systemPropertyMap.put(init_svc_droid4x, "");

        return systemPropertyMap;
    }


    public static Map<String, String> defaultARM32SystemProperty() {

        Map<String, String > systemPropertyMap = new HashMap<>();
        systemPropertyMap.put(net_hostname, "MIX2S-zhongeryayadeM");
        systemPropertyMap.put(ro_serialno, "f8a995f5");
        systemPropertyMap.put(ro_boot_serialno, "f8a995f5");
        systemPropertyMap.put(ro_product_brand, "Xiaomi");

        systemPropertyMap.put(ro_product_manufacturer, "Xiaomi");
        systemPropertyMap.put(ro_product_model, "MIX 2S");
        systemPropertyMap.put(ro_product_cpu_abi, "armeabi-v7a");
        systemPropertyMap.put(ro_product_cpu_abilist, "armeabi-v7a,armeabi");

        systemPropertyMap.put(ro_boot_vbmeta_digest, "");
        systemPropertyMap.put(init_svc_droid4x, "");

        return systemPropertyMap;
    }

}


