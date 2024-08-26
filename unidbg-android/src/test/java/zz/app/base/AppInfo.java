package zz.app.base;

import com.github.unidbg.file.IOResolver;
import com.github.unidbg.file.linux.AndroidFileIO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class AppInfo {

    public enum VirtualModuleName {
        AndroidModule,
        JniGraphics,
        MediaNdkModule
    }

    private String apkBasePath = "unidbg-android/src/test/java/zz/app/";

    public boolean is64Bit = true;      //是否ARM64
    public String bundleName;           //apk包名
    public String rootfs;               //fs根目錄
    public String rootsource;           //
    public String apkPath;              //apk路徑
    public String soName;               //so的名字，支持 libXXX.so， 或者XXX。
    public String clsName;              //接口類

    //其他配置
    public List<VirtualModuleName> virtualLibrarys;
    public List<String> dependLibrarys;
    public Map<String, String> systemProperties;
    public List<IOResolver<AndroidFileIO>> ioResolvers;


    public AppInfo(boolean is64Bit, String bundleName, String rootfs, String rootsource, String apkPath, String soName, String clsName) {
        this.is64Bit = is64Bit;
        this.bundleName = bundleName;

        if(rootfs != null && !rootfs.isEmpty()) {
            this.rootfs = apkBasePath + rootfs;
        } else {
            this.rootfs = null;
        }

        if(rootsource != null && !rootsource.isEmpty()) {
            this.rootsource = apkBasePath + rootsource;
        } else {
            this.rootsource = null;
        }


        this.apkPath = apkBasePath + apkPath;

        if(soName.startsWith("lib") || soName.endsWith(".so")) {
            soName = soName.replace(".so", "");
            soName = soName.replace("lib", "");
        }
        this.soName = soName;
        this.clsName = clsName;

    }


    public void addDependLibrary(List<VirtualModuleName> virtualLibrarys, List<String> dependLibrarys) {

        this.virtualLibrarys = virtualLibrarys;

        //添加librarys的路径前缀
        if(dependLibrarys != null) {
            List<String> resultDependLibrarys = new ArrayList<>();
            for(String libName : dependLibrarys) {
                libName = apkBasePath +libName;
                resultDependLibrarys.add(libName);
            }
            this.dependLibrarys = resultDependLibrarys;
        }

    }

}