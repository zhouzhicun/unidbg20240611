package com.github.unidbg.zz;


import com.github.unidbg.Emulator;
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

public class Utils {

    static void hookSVC(Emulator<?> emulator) {

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


    //patch: 例如：hexcode = [0xd0, 0x1a, 0xaa, 0x20]
    static void patchCode(Emulator<?> emulator, long addr, String machineHexCode) {

        UnidbgPointer pointer = UnidbgPointer.pointer(emulator, addr);
        byte[] bytes = StringUtils.hexToBytes(machineHexCode);
        pointer.write(bytes);
    }

    //patch：例如：asmCode = "subs r0, r2, r3";
    static void patchASM(Emulator<?> emulator, long addr, String asmCode) {

        UnidbgPointer pointer = UnidbgPointer.pointer(emulator, addr);
        Keystone keystone = new Keystone(KeystoneArchitecture.Arm64, KeystoneMode.LittleEndian);
        byte[] codeBytes = keystone.assemble(asmCode).getMachineCode();
        pointer.write(codeBytes);
    }


    static void hookRead(Emulator<?> emulator) {
        long begin = 0x1000;
        long end = 0x2000;
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
        }, begin, end, null);

        emulator.getBackend().hook_add_new(new WriteHook() {
            @Override
            public void hook(Backend backend, long address, int size, long value, Object user) {

            }

            @Override
            public void onAttach(UnHook unHook) {}

            @Override
            public void detach() {}
        }, begin, end, null);
    }

}
