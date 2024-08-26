package com.github.unidbg.zz;

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;

import java.util.HashMap;

public class ZZUnameConfig {

    public class UnameConfingFN {

        public static final String sysName = "sysName";
        public static final String machineArm32 = "machineArm32";
        public static final String machineArm64 = "machineArm64";
        public static final String nodename = "nodename";
        public static final String domainname = "domainname";
        public static final String release = "release";
        public static final String version = "version";

    }

    //固定配置
    public static String sysname = "Linux";
    public static String machineArm32 = "armv7l";
    public static String machineArm64 = "aarch64";

    public static String nodename = "localhost";
    public static String domainname = "localdomain";

    //可配
    public static String release = "4.14.243-gff8eae656fe6-ab8007944";
    public static String version = "#1 SMP PREEMPT Thu Dec 16 11:39:02 UTC 2021";


    public static void updateUname(String release, String version) {
        ZZUnameConfig.release = release;
        ZZUnameConfig.version = version;
    }


    public static void updateUname(HashMap<String, String> configMap) {
        if(configMap == null || configMap.isEmpty()) {
            return;
        }

       for (String key : configMap.keySet()) {
           String value = configMap.get(key);
           switch (key) {
               case UnameConfingFN.sysName: {
                   ZZUnameConfig.sysname = value;
                   break;
               }
               case UnameConfingFN.machineArm32: {
                   ZZUnameConfig.machineArm32 = value;
                   break;
               }
               case UnameConfingFN.machineArm64: {
                   ZZUnameConfig.machineArm64 = value;
                   break;
               }
               case UnameConfingFN.nodename: {
                   ZZUnameConfig.nodename = value;
                   break;
               }
               case UnameConfingFN.domainname: {
                   ZZUnameConfig.domainname = value;
                   break;
               }
               case UnameConfingFN.release: {
                   ZZUnameConfig.release = value;
                   break;
               }
               case UnameConfingFN.version: {
                   ZZUnameConfig.version = value;
                   break;
               }
           }
       }

    }
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