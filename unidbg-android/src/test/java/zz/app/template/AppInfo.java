package zz.app.template;

public class AppInfo {

    public boolean is64Bit = true;
    public String bundleName;
    public String rootfs;
    public String apkPath;
    public String soName;
    public String clsName;

    private String apkBasePath = "unidbg-android/src/test/java/zz/app/";

    public AppInfo(boolean is64Bit, String bundleName, String rootfs, String apkPath, String soName, String clsName) {
        this.is64Bit = is64Bit;
        this.bundleName = bundleName;

        if(rootfs != null && !rootfs.isEmpty()) {
            this.rootfs = apkBasePath + rootfs;
        } else {
            this.rootfs = null;
        }

        this.apkPath = apkBasePath + apkPath;

        if(soName.startsWith("lib") || soName.endsWith(".so")) {
            soName = soName.replace(".so", "");
            soName = soName.replace("lib", "");
        }
        this.soName = soName;
        this.clsName = clsName;
    }
}