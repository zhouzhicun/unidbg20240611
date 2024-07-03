package zz.app.base;

import com.github.unidbg.Emulator;
import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.file.linux.AndroidFileIO;
import com.github.unidbg.linux.file.ByteArrayFileIO;
import com.github.unidbg.linux.file.SimpleFileIO;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** proc文件

 文件访问最主要服务于环境检测和信息收集，举例如下。
 ● 访问 /proc/self/maps 检测 frida/xposed 等模块
 ● 访问 /proc/net/tcp 检测 frida/ida server 默认端口
 ● 访问 /proc/self/cmdline 确认在自身进程内
 ● 访问 apkfile 做签名校验，解析资源文件等等
 ● 访问 /xbin/su 检测 root
 ● 访问 /data/data/package/xxx 读取自身相关文件

 环境检测
 当样本做环境检测相关的文件访问时，主要检测这些文件是否存在，以及是否有权限
 ● Root检测（检测 su、Magisk、Riru，检测市面上的 Root 工具）
 ● 模拟器检测（检测 Qemu，检测各家模拟器，比如夜神、雷电、Mumu 等模拟器的文件特征、驱动特征等）
 ● 危险应用检测（各类多开助手、按键精灵、接码平台等）
 ● 云手机检测 （以各种云手机产品为主）
 ● Hook框架（以 Xposed、Substrate、Frida 为主）
 ● 脱壳机（以 Fart、DexHunter、Youpk 三者为主）

 */

//本地補的文件名
class LocalProcFN {

    public static final String proc_cpuinfo = "proc_cpuinfo";
    public static final String proc_version = "proc_version";
    public static final String proc_meminfo = "proc_meminfo";
    public static final String proc_stat = "proc_stat";
    public static final String proc_self_exe = "proc_self_exe";
    public static final String proc_asound_cardX_id = "proc_asound_cardX_id";
    public static final String battery_temp = "battery_temp";
    public static final String battery_voltage_now = "battery_voltage_now";

}

//so庫訪問的文件名
class AccessProcFN {

    public static final String proc_sys_kernel_random_boot = "proc/sys/kernel/random/boot";
    public static final String proc_cpuinfo = "/proc/cpuinfo";
    public static final String proc_version = "/proc/version";
    public static final String proc_meminfo = "/proc/meminfo";
    public static final String proc_stat = "/proc/stat";
    public static final String proc_self_exe = "/proc/self/exe";
    public static final String proc_asound_cardX_id = "/proc/asound/cardX/id";

    //電池+cpu
    public static final String power_supply_battery_temp = "/sys/class/power_supply/battery/temp";
    public static final String power_supply_battery_voltage_now = "/sys/class/power_supply/battery/voltage_now";
    public static final String cpu_cpu0_cpufreq_cpuinfo_max_freq = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";

    public static final String proc_pid_status = "/proc/%d/status";
}



public class ProcFileResolver implements IOResolver<AndroidFileIO> {

    private String procFileRoot;

    public ProcFileResolver(String procFileRoot) {
        this.procFileRoot = procFileRoot;
    }

    @Override
    public FileResult<AndroidFileIO> resolve(Emulator<AndroidFileIO> emulator, String pathname, int oflags) {

        System.out.println("File open: " + pathname);

        FileResult<AndroidFileIO> result = handleFile(emulator, pathname, oflags, this.procFileRoot);
        if (result.isSuccess()) {
            return result;
        } else {
            System.err.println("未處理proc文件, 待補充: " + pathname);
        }

        return null;
    }




    private File loadLocalFile(String fileRoot, String fileName) {
        File procFile = new File(fileRoot + fileName);
        if (!procFile.exists()) {
            System.out.println("本地不存在如下proc文件：" + fileName);
            return null;
        }
        return procFile;

    }

    private FileResult<AndroidFileIO> handleFile(Emulator<AndroidFileIO> emulator, String pathname, int oflags, String procFileRoot) {


        if (procFileRoot == null || procFileRoot.isEmpty()) {
            return null;
        }

        //如果後綴不是/, 則加上。
        if (!procFileRoot.endsWith("/")) {
            procFileRoot = procFileRoot + "/";
        }

        String temp_proc_pid_status = String.format(AccessProcFN.proc_pid_status, emulator.getPid());

        //=======================================================================

        if (pathname.equals(AccessProcFN.proc_sys_kernel_random_boot)) {
            return FileResult.success(new ByteArrayFileIO(oflags, pathname, UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
        }
        if (pathname.equals(AccessProcFN.cpu_cpu0_cpufreq_cpuinfo_max_freq)) {
            return FileResult.success(new ByteArrayFileIO(oflags, pathname, "1766400".getBytes()));
        }

        if (pathname.equals(AccessProcFN.proc_cpuinfo)) {
            File procFile = loadLocalFile(procFileRoot, LocalProcFN.proc_cpuinfo);
            return FileResult.success(new SimpleFileIO(oflags, procFile, pathname));
        }

        if (pathname.equals(AccessProcFN.proc_version)) {
            File procFile = loadLocalFile(procFileRoot, LocalProcFN.proc_version);
            return FileResult.success(new SimpleFileIO(oflags, procFile, pathname));
        }
        if (pathname.equals(AccessProcFN.proc_meminfo)) {
            File procFile = loadLocalFile(procFileRoot, LocalProcFN.proc_meminfo);
            return FileResult.success(new SimpleFileIO(oflags, procFile, pathname));
        }
        if (pathname.equals(AccessProcFN.proc_stat)) {
            File procFile = loadLocalFile(procFileRoot, LocalProcFN.proc_stat);
            return FileResult.success(new SimpleFileIO(oflags, procFile, pathname));
        }
        if (pathname.equals(AccessProcFN.proc_self_exe)) {
            File procFile = loadLocalFile(procFileRoot, LocalProcFN.proc_self_exe);
            return FileResult.success(new SimpleFileIO(oflags, procFile, pathname));
        }
        if (pathname.equals(AccessProcFN.proc_asound_cardX_id)) {
            File procFile = loadLocalFile(procFileRoot, LocalProcFN.proc_asound_cardX_id);
            return FileResult.success(new SimpleFileIO(oflags, procFile, pathname));
        }

        if (pathname.equals(AccessProcFN.power_supply_battery_temp)) {
            File procFile = loadLocalFile(procFileRoot, LocalProcFN.battery_temp);
            return FileResult.success(new SimpleFileIO(oflags, procFile, pathname));
        }

        if (pathname.equals(AccessProcFN.power_supply_battery_voltage_now)) {
            File procFile = loadLocalFile(procFileRoot, LocalProcFN.battery_voltage_now);
            return FileResult.success(new SimpleFileIO(oflags, procFile, pathname));
        }


        //================================== proc/pid/status =====================================

        if (pathname.equals(temp_proc_pid_status)) {
            return FileResult.success(new ByteArrayFileIO(oflags, pathname,
                    ("Name:   ip.android.view\n" +
                            "Umask:  0077\n" +
                            "State:  S (sleeping)\n" +
                            "Tgid:   " + emulator.getPid() + "\n" +
                            "Ngid:   0\n" +
                            "Pid:    " + emulator.getPid() + "\n" +
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
                            "nonvoluntary_ctxt_switches:     10433").getBytes(StandardCharsets.UTF_8)));

        }


        //================================== other =====================================
        return null;
    }


}
