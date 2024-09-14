package zz.app.base;


import com.github.unidbg.Emulator;
import com.github.unidbg.Module;
import com.github.unidbg.arm.Arm64Svc;
import com.github.unidbg.arm.backend.Backend;
import com.github.unidbg.arm.backend.ReadHook;
import com.github.unidbg.arm.backend.UnHook;
import com.github.unidbg.arm.backend.WriteHook;
import com.github.unidbg.arm.context.RegisterContext;
import com.github.unidbg.memory.SvcMemory;
import com.github.unidbg.pointer.UnidbgPointer;
import keystone.Keystone;
import keystone.KeystoneArchitecture;
import keystone.KeystoneMode;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String curTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }

    //创建文件
    public static void createFile(String filePath) {

        File file = new File(filePath);
        try {
            //如果文件已存在，则先删除。
            if (file.exists()) {
                file.delete();
            }
            //创建新文件
            file.createNewFile();

        } catch (IOException e) {
            System.out.println("文件创建失败。");
            e.printStackTrace();
        }
    }

    //trace指令到文件中
    public static void traceCode(Emulator<?> emulator, Module module, String traceFile, boolean traceRW) {

        createFile(traceFile);

        try {
            FileOutputStream fileStream = new FileOutputStream(traceFile);
            PrintStream traceStream = new PrintStream(fileStream);
            emulator.traceCode(module.base, module.base + module.size).setRedirect(traceStream);
            if (traceRW) {
                emulator.traceRead().setRedirect(traceStream);
                emulator.traceWrite().setRedirect(traceStream);
            }

        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            System.err.println("file not found: " + traceFile);
        }
    }


    //patch: 例如：hexcode = [0xd0, 0x1a, 0xaa, 0x20]
    public static void patchCode(Emulator<?> emulator, long addr, String machineHexCode) {

        UnidbgPointer pointer = UnidbgPointer.pointer(emulator, addr);
        byte[] bytes = StringUtils.hexToBytes(machineHexCode);
        pointer.write(bytes);
    }

    //patch：例如：asmCode = "subs r0, r2, r3";
    public static void patchASM(Emulator<?> emulator, long addr, String asmCode) {

        UnidbgPointer pointer = UnidbgPointer.pointer(emulator, addr);
        Keystone keystone = new Keystone(KeystoneArchitecture.Arm64, KeystoneMode.LittleEndian);
        byte[] codeBytes = keystone.assemble(asmCode).getMachineCode();
        pointer.write(codeBytes);
    }


    //hook svc系统调用
    public static void hookSVC(Emulator<?> emulator) {

        SvcMemory svcMemory = emulator.getSvcMemory();
        svcMemory.registerSvc(new Arm64Svc() {
            @Override
            public long handle(Emulator<?> emulator) {
                RegisterContext context = emulator.getContext();
                int svcNumber = context.getIntArg(7);
                System.out.println("Hit custom SVC number: " + svcNumber);
                return 0;
            }
        });
    }

    //Hook 内存读写访问
    public static void hookMemoryAccess(Emulator<?> emulator, long beginAddr, long endAddr) {

        emulator.getBackend().hook_add_new(new ReadHook() {
            @Override
            public void hook(Backend backend, long address, int size, Object user) {
                byte[] bytes = backend.mem_read(address, size);
                System.out.println(String.format(">>> memory read at 0x%x, block size = 0x%x\n", address, size));
            }

            @Override
            public void onAttach(UnHook unHook) {}

            @Override
            public void detach() {}

        }, beginAddr, endAddr, null);

        emulator.getBackend().hook_add_new(new WriteHook() {
            @Override
            public void hook(Backend backend, long address, int size, long value, Object user) {
//                byte[] bytes = backend.mem_read(address, size);
//                System.out.println(String.format(">>> memory read at 0x%x, block size = 0x%x\n", address, size));


//                //注意:
//                // read/write hook 的发生时机是读写活动发生前，我们希望在读写活动发生之后做检索，
//                // 这样才能找到最早的数据源，所以这里我选择手动写入。
//                byte[] writeData = new byte[size];
//                for (int i = 0; i < size; i++) {
//                    writeData[i] = (byte)(value >> (8 * i));
//                }
//                backend.mem_write(address, writeData);
            }

            @Override
            public void onAttach(UnHook unHook) {}

            @Override
            public void detach() {}

        }, beginAddr, endAddr, null);
    }



//    public static AndroidEmulatorBuilder createCustomBuilder() {
//
//        AndroidEmulatorBuilder builder = null;
//        builder = new AndroidEmulatorBuilder(true){
//            @Override
//            public AndroidEmulator build() {
//                return new AndroidARMEmulator(processName,rootDir,backendFactories) {
//                    @Override
//                    protected UnixSyscallHandler<AndroidFileIO> createSyscallHandler(SvcMemory svcMemory) {
//                        return new DemoARM64SyscallHandler(svcMemory);
//                    }
//                };
//            }
//        };
//
//        return builder;
//    }


}
