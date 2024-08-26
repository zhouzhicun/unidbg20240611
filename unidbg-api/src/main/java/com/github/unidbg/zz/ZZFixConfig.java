package com.github.unidbg.zz;

public class ZZFixConfig {

    //固定值
    public static long curTime = 1719919836983L;
    public static int pid = 0x5000;

    //固定开关
    public static boolean fix_gettimeofday = true;
    public static boolean fix_clock_gettime = true;
    public static boolean fix_file_timestamp = true;
    public static boolean fix_ramdom_file = true;
    public static boolean fix_getramdom = true;
    public static boolean fix_processid = true;

}
