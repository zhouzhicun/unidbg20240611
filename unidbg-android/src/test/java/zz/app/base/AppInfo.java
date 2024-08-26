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

    public boolean is64Bit = true;
    public String bundleName;
    public String rootfs;
    public String rootsource;
    public String apkPath;
    public String soName;
    public String clsName;

    //复杂配置
    public List<VirtualModuleName> virtualLibrarys;
    public List<String> dependLibrarys;
    public Map<String, String> systemProperties;
    public List<IOResolver<AndroidFileIO>> ioResolvers;

    private String apkBasePath = "unidbg-android/src/test/java/zz/app/";

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