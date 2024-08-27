package zz.app.base.files;

public class AppFileName {

    //root文件检测，不需要补, 直接返回Null即可。
    public static final String[] rootFiles = {

            "/system",
            "/vendor",
            "/su",
            "/xbin",
            "/sbin",
            "/subin",
            "/etc",
            "/sys",
            "/proc",
            "/dev",

            "/subin/su",
            "/sbin/su",
            "/sbin/daemonsu",
            "/vendor/bin/su",
            "/vendor/bin/daemonsu",
            "/system/sbin/su",
            "/system/sbin/daemonsu",
            "/system/bin/su",
            "/system/bin/daemonsu",
            "/system/xbin/su",
            "/system/xbin/daemonsu",

    };

    //网络文件：/proc/net 下的文件一律不要补，有两个理由。1：Google 禁止普通进程访问该目录, 2.这个目录主要用于检测环境，比如 IDA/Frida Server 的端口检测。
    public static final String[] netFiles = {
            "/proc/net/arp",
            "/proc/net/tcp",
            "/proc/net/unix",
    };

}
