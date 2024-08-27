package zz.app.base.files;

import com.github.unidbg.Emulator;
import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.file.linux.AndroidFileIO;

import java.util.UUID;



public class DefaultProcFileResolver implements IOResolver<AndroidFileIO> {

    private String procFileRoot;

    public DefaultProcFileResolver(String procFileRoot) {
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

    private FileResult<AndroidFileIO> handleFile(Emulator<AndroidFileIO> emulator, String pathname, int oflags, String fileRoot) {


        if (fileRoot == null || fileRoot.isEmpty()) {
            return null;
        }

        //如果後綴不是/, 則加上。
        if (!fileRoot.endsWith("/")) {
            fileRoot = fileRoot + "/";
        }

        //================================= local file ======================================

        if (pathname.equals(AppProcFile.proc_cpuinfo)) {
            return IOResolverUtil.createLocalFile(pathname, oflags, fileRoot + AppProcFile.LocalProcFile.proc_cpuinfo);
        }

        if (pathname.equals(AppProcFile.proc_version)) {
            return IOResolverUtil.createLocalFile(pathname, oflags, fileRoot + AppProcFile.LocalProcFile.proc_version);
        }
        if (pathname.equals(AppProcFile.proc_meminfo)) {
            return IOResolverUtil.createLocalFile(pathname, oflags, fileRoot + AppProcFile.LocalProcFile.proc_meminfo);
        }
        if (pathname.equals(AppProcFile.proc_stat)) {
            return IOResolverUtil.createLocalFile(pathname, oflags, fileRoot + AppProcFile.LocalProcFile.proc_stat);
        }
        if (pathname.equals(AppProcFile.proc_self_exe)) {
            return IOResolverUtil.createLocalFile(pathname, oflags, fileRoot + AppProcFile.LocalProcFile.proc_self_exe);
        }
        if (pathname.equals(AppProcFile.proc_asound_cardX_id)) {
            return IOResolverUtil.createLocalFile(pathname, oflags, fileRoot + AppProcFile.LocalProcFile.proc_asound_cardX_id);
        }

        if (pathname.equals(AppProcFile.power_supply_battery_temp)) {
            return IOResolverUtil.createLocalFile(pathname, oflags, fileRoot + AppProcFile.LocalProcFile.battery_temp);
        }

        if (pathname.equals(AppProcFile.power_supply_battery_voltage_now)) {
            return IOResolverUtil.createLocalFile(pathname, oflags, fileRoot + AppProcFile.LocalProcFile.battery_voltage_now);
        }


        //================================== byte file =====================================

        if (pathname.equals(AppProcFile.proc_sys_kernel_random_boot)) {
            return IOResolverUtil.createByteFile(pathname, oflags, UUID.randomUUID().toString());
        }
        if (pathname.equals(AppProcFile.cpu_cpu0_cpufreq_cpuinfo_max_freq)) {
            return IOResolverUtil.createByteFile(pathname, oflags, "1766400");
        }

        if(pathname.equals(AppProcFile.proc_cmdline_pid) || pathname.equals(AppProcFile.proc_cmdline_self)) {
            String content = AppProcFile.getProcCmdlineContent();
            return IOResolverUtil.createByteFile(pathname, oflags, content);
        }

        if (pathname.equals(AppProcFile.proc_status_pid) || pathname.equals(AppProcFile.proc_status_self)) {
            String content = AppProcFile.getProcStatusContent();
            return IOResolverUtil.createByteFile(pathname, oflags, content);
        }

        //================================== other =====================================
        return null;
    }


}
