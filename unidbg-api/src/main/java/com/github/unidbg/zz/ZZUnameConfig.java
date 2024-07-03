package com.github.unidbg.zz;

public class ZZUnameConfig {

    public static String sysname = "Linux";
    public static String nodename = "localhost";
    public static String release = "4.14.243-gff8eae656fe6-ab8007944";
    public static String version = "#1 SMP PREEMPT Thu Dec 16 11:39:02 UTC 2021";
    public static String machineArm32 = "armv7l";
    public static String machineArm64 = "aarch64";
    public static String domainname = "localdomain";

}


/**  获取uname信息：
 * 参考文档：https://www.yuque.com/lilac-2hqvv/xdwlsg/gri3ins5e22b5x7o
 * 在 adb shell 中可以获取 除 domainname 之外的其他信息。

 usage: uname [-asnrvm]
 Print system information.

 -s      System name
 -n      Network (domain) name
 -r      Kernel Release number
 -v      Kernel Version
 -m      Machine (hardware) name
 -a      All of the above

 *
 */