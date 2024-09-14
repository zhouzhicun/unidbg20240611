package zz.app.bili;

import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.jni.ProxyDvmObject;
import zz.app.base.AppInfo;
import zz.app.base.BaseAbstractJni;

import java.util.Map;
import java.util.TreeMap;

public class bili extends BaseAbstractJni {

    bili() {

        //1.App常量
        String projectName = "bili";
        String apkFileName =  "bilibili.apk";
        String bundleName = "tv.danmaku.bili";
        String soName = "bili";
        String clsName = "com.bilibili.nativelibrary.LibBili";
        AppInfo appInfo = new AppInfo(false, projectName, apkFileName, bundleName, soName, clsName);
        build(appInfo, null);

    }

    public static void main(String[] args) {
        bili test = new bili();
        test.callSign();
    }



    public void callSign(){
        TreeMap<String, String> map = new TreeMap<>();
        map.put("build", "6180500");
        map.put("mobi_app", "android");
        map.put("channel", "shenma069");
        map.put("appkey", "1d8b6e7d45233436");
        map.put("s_locale", "zh_CN");
        DvmObject<?> mapObject = ProxyDvmObject.createObject(vm, map);

        String method = "s(Ljava/util/SortedMap;)Lcom/bilibili/nativelibrary/SignedQuery;";
        String ret = nativeAPI.callStaticJniMethodObject(emulator, method, mapObject).getValue().toString();
        System.err.println("ret = " + ret);
    }


    @Override
    public boolean callBooleanMethod(BaseVM vm, DvmObject<?> dvmObject, String signature, VarArg varArg) {
        switch (signature) {
            case "java/util/Map->isEmpty()Z":
                Map map = (Map) dvmObject.getValue();
                return map.isEmpty();
        }

        return super.callBooleanMethod(vm, dvmObject, signature, varArg);
    }

    @Override
    public DvmObject<?> callObjectMethod(BaseVM vm, DvmObject<?> dvmObject, String signature, VarArg varArg) {
        switch (signature) {
            case "java/util/Map->get(Ljava/lang/Object;)Ljava/lang/Object;": {
                Map map = (Map) dvmObject.getValue();
                String key = (String) varArg.getObjectArg(0).getValue();
                String value = (String) map.get(key);
                return ProxyDvmObject.createObject(vm, value);
            }
            case "java/util/Map->put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;": {
                Map map = (Map) dvmObject.getValue();
                String key = (String) varArg.getObjectArg(0).getValue();
                String value = (String) varArg.getObjectArg(1).getValue();
                map.put(key, value);
                return ProxyDvmObject.createObject(vm, map);
            }
        }
        return super.callObjectMethod(vm, dvmObject, signature, varArg);
    }

    @Override
    public DvmObject<?> callStaticObjectMethod(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        switch (signature) {
            case "com/bilibili/nativelibrary/SignedQuery->r(Ljava/util/Map;)Ljava/lang/String;": {
                Map map = (Map) varArg.getObjectArg(0).getValue();
                String str = SignedQuery.r(map);

                //String转DvmObject
                return ProxyDvmObject.createObject(vm, str);
            }
        }
        return super.callStaticObjectMethod(vm, dvmClass, signature, varArg);
    }


    @Override
    public DvmObject<?> newObject(BaseVM vm, DvmClass dvmClass, String signature, VarArg varArg) {
        switch (signature) {
            case "com/bilibili/nativelibrary/SignedQuery-><init>(Ljava/lang/String;Ljava/lang/String;)V":{
                String arg1 = varArg.getObjectArg(0).getValue().toString();
                String arg2 = varArg.getObjectArg(1).getValue().toString();

                //创建对象：vm.resolveClass(clsName).newObject(代理对象)
                return vm.resolveClass("com/bilibili/nativelibrary/SignedQuery").newObject(new SignedQuery(arg1, arg2));
            }
        }
        return super.newObject(vm, dvmClass, signature, varArg);
    }
}
