package zz.app.base;

import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.StringObject;

public class BaseAbstractJniHelper {

    public static BaseAbstractJni abstractJni;

    //******************************* 常用补环境 ****************************************

    public static StringObject getAppFilesDir() {
        String dirs = String.format("/data/user/0/%s/files", abstractJni.appInfo.bundleName);
        return new StringObject(abstractJni.vm, dirs);
    }

    public static DvmObject<?> getContext() {
        return abstractJni.vm.resolveClass("android/content/Context").newObject(null);
    }

    public static DvmClass getClass(DvmObject<?> dvmObject) {
        return dvmObject.getObjectType();
    }

}
