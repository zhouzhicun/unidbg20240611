package zz.app.base.files;


import zz.app.base.BaseAbstractJniHelper;

public class AppProcFile {

    //本地補的文件名
    public class LocalProcFile {

        public static final String proc_meminfo = "proc_meminfo";
        public static final String proc_cpuinfo = "proc_cpuinfo";
        public static final String proc_version = "proc_version";

        public static final String proc_stat = "proc_stat";
        public static final String proc_asound_cardX_id = "proc_asound_cardX_id";
        public static final String proc_self_exe = "proc_self_exe";
        public static final String proc_sys_kernel_random_boot = "proc_sys_kernel_random_boot";

        public static final String battery_temp = "battery_temp";
        public static final String battery_voltage_now = "battery_voltage_now";

    }

    //信息获取
    public static String proc_meminfo = "/proc/meminfo";
    public static String proc_cpuinfo = "/proc/cpuinfo";
    public static String proc_version = "/proc/version";
    public static String proc_stat = "/proc/stat";
    public static String proc_asound_cardX_id = "/proc/asound/cardX/id";
    public static String proc_self_exe = "/proc/self/exe";
    public static String proc_sys_kernel_random_boot = "proc/sys/kernel/random/boot";

    //電池+cpu
    public static String power_supply_battery_temp = "/sys/class/power_supply/battery/temp";
    public static String power_supply_battery_voltage_now = "/sys/class/power_supply/battery/voltage_now";
    public static String cpu_cpu0_cpufreq_cpuinfo_max_freq = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";


    //maps文件
    public static String proc_maps_self = "/proc/self/maps";
    public static String proc_maps_pid = "/proc/%d/maps";

    //cmdline文件
    public static String proc_cmdline_self = "/proc/self/cmdline";
    public static String proc_cmdline_pid = "/proc/%d/cmdline";

    //status文件
    public static String proc_status_self = "/proc/self/status";
    public static String proc_status_pid = "/proc/%d/status";



    public static void updatePid() {
        int pid = BaseAbstractJniHelper.abstractJni.emulator.getPid();
        proc_maps_pid = String.format(proc_maps_pid, pid);
        proc_status_pid = String.format(proc_status_pid, pid);
        proc_cmdline_pid = String.format(proc_cmdline_pid, pid);
    }


    public static String getProcCmdlineContent() {
        String bundleName = BaseAbstractJniHelper.abstractJni.appInfo.bundleName;
        String fileContent = String.format("%s\0", bundleName);
        return fileContent;
    }

    //遇到对 maps 的访问，建议优先使用真实 maps，如果出现内存异常（这意味着样本在基于真实 maps 做内存访问），就使用 fakemaps。
    public static String getProcMapsContent() {
        String fileContent = "";
        return fileContent;
    }

    public static String getProcStatusContent() {

        int pid = BaseAbstractJniHelper.abstractJni.emulator.getPid();
        String fileContent =
                "Name:   ip.android.view\n" +
                        "Umask:  0077\n" +
                        "State:  S (sleeping)\n" +
                        "Tgid:   "+pid+"\n" +
                        "Ngid:   0\n" +
                        "Pid:    "+pid+"\n" +
                        "PPid:   6119\n" +
                        "TracerPid:      0\n" +
                        "Uid:    10494   10494   10494   10494\n" +
                        "Gid:    10494   10494   10494   10494\n" +
                        "FDSize: 512\n" +
                        "Groups: 3002 3003 9997 20494 50494 99909997\n" +
                        "VmPeak:  2543892 kB\n" +
                        "VmSize:  2466524 kB\n" +
                        "VmLck:         0 kB\n" +
                        "VmPin:         0 kB\n" +
                        "VmHWM:    475128 kB\n" +
                        "VmRSS:    415548 kB\n" +
                        "RssAnon:          144072 kB\n" +
                        "RssFile:          267216 kB\n" +
                        "RssShmem:           4260 kB\n" +
                        "VmData:  1488008 kB\n" +
                        "VmStk:      8192 kB\n" +
                        "VmExe:        20 kB\n" +
                        "VmLib:    239368 kB\n" +
                        "VmPTE:      2360 kB\n" +
                        "VmPMD:        16 kB\n" +
                        "VmSwap:    13708 kB\n" +
                        "Threads:        122\n" +
                        "SigQ:   0/21555\n" +
                        "SigPnd: 0000000000000000\n" +
                        "ShdPnd: 0000000000000000\n" +
                        "SigBlk: 0000000080001204\n" +
                        "SigIgn: 0000000000000001\n" +
                        "SigCgt: 0000000e400096fc\n" +
                        "CapInh: 0000000000000000\n" +
                        "CapPrm: 0000000000000000\n" +
                        "CapEff: 0000000000000000\n" +
                        "CapBnd: 0000000000000000\n" +
                        "CapAmb: 0000000000000000\n" +
                        "Seccomp:        2\n" +
                        "Speculation_Store_Bypass:       unknown\n" +
                        "Cpus_allowed:   07\n" +
                        "Cpus_allowed_list:      0-2\n" +
                        "Mems_allowed:   1\n" +
                        "Mems_allowed_list:      0\n" +
                        "voluntary_ctxt_switches:        17290\n" +
                        "nonvoluntary_ctxt_switches:     10433";

        return fileContent;
    }

}
