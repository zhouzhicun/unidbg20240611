package zz.app.base;

import com.github.unidbg.ModuleListener;
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

    //是否ARM64
    public boolean is64Bit = true;

    public String projectName;          //unidbg工程名
    public String apkPath;          //apk文件名
    public String bundleName;           //app包名
    public String soName;               //so的名字，支持 libXXX.so， 或者XXX。
    public String clsName;              //接口類

    //相关目录
    public String rootfs;               //fs根目錄
    public String resourcefs;             //
    public String outputfs;             //trace或其他输出目录


    //其他配置
    public List<VirtualModuleName> virtualLibrarys;
    public List<String> dependLibrarys;
    public Map<String, String> systemProperties;
    public List<IOResolver<AndroidFileIO>> ioResolvers;
    public ModuleListener moduleListener;

    public AppInfo(boolean is64Bit, String projectName, String apkFileName, String bundleName, String soName, String clsName) {
        this.is64Bit = is64Bit;

        this.projectName = projectName;
        this.bundleName = bundleName;

        this.apkPath = String.format("%s/%s/%s", apkBasePath, projectName, apkFileName);

        //目录
        this.rootfs = String.format("%s/%s/rootfs", apkBasePath, projectName);
        this.resourcefs = String.format("%s/%s/source", apkBasePath, projectName);
        this.outputfs = String.format("%s/%s/output", apkBasePath, projectName);



        if(soName.startsWith("lib") || soName.endsWith(".so")) {
            soName = soName.replace(".so", "");
            soName = soName.replace("lib", "");
        }
        this.soName = soName;
        this.clsName = clsName;

    }


    //依赖的so库都放在 resource目录下.
    public void addDependLibrary(List<VirtualModuleName> virtualLibrarys, List<String> dependLibrarys) {

        this.virtualLibrarys = virtualLibrarys;

        //添加librarys的路径前缀
        if(dependLibrarys != null) {
            List<String> resultDependLibrarys = new ArrayList<>();
            for(String libName : dependLibrarys) {
                libName = String.format("%s/%s", this.resourcefs, libName);
                resultDependLibrarys.add(libName);
            }
            this.dependLibrarys = resultDependLibrarys;
        }

    }

}