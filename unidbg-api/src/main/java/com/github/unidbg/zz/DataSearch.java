package com.github.unidbg.zz;

import com.github.unidbg.Emulator;
import com.github.unidbg.arm.backend.Backend;
import com.github.unidbg.arm.backend.ReadHook;
import com.github.unidbg.arm.backend.UnHook;
import com.github.unidbg.arm.backend.WriteHook;
import com.github.unidbg.pointer.UnidbgPointer;
import com.sun.jna.Pointer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * 源码来源： https://www.yuque.com/lilac-2hqvv/glw3nr/kwdrl60zsxaxo4gh
 */

public class DataSearch {
    private final Emulator<?> emulator;
    private final byte[] data;

    public DataSearch(Emulator<?> emulator, byte[] data) {
        this.emulator = emulator;
        this.data = data;
        hookReadAndWrite();
    }

    private void hookReadAndWrite(){
        ReadHook readHook = new ReadHook() {
            @Override
            public void hook(Backend backend, long address, int size, Object user) {
                searchData(data, address);
            }

            @Override
            public void onAttach(UnHook unHook) {

            }

            @Override
            public void detach() {

            }
        };

        WriteHook writeHook = new WriteHook() {
            @Override
            public void hook(Backend backend, long address, int size, long value, Object user) {

                //注意:
                // read/write hook 的发生时机是读写活动发生前，我们希望在读写活动发生之后做检索，
                // 这样才能找到最早的数据源，所以这里我选择手动写入。
                byte[] writeData = new byte[size];
                for (int i = 0; i < size; i++) {
                    writeData[i] = (byte)(value >> (8 * i));
                }
                backend.mem_write(address, writeData);

                searchData(data, address);
            }

            @Override
            public void onAttach(UnHook unHook) {

            }

            @Override
            public void detach() {

            }
        };
        emulator.getBackend().hook_add_new(writeHook, 1, 0, null);
        emulator.getBackend().hook_add_new(readHook, 1, 0, null);
    }

    public void searchData(byte[] data, long address){
        List<Pointer> list = new ArrayList<>();
        Backend backend = emulator.getBackend();
        long start = address - 64;
        long end = address + 63;
        list.addAll(searchMemory(backend, start, end, data));
        if(list.size()>0){
            for (Pointer pointer : list) {
                System.out.println("Search matches: " + pointer);
            }
            emulator.attach().debug();
        }
    }


    private Collection<Pointer> searchMemory(Backend backend, long start, long end, byte[] data) {
        List<Pointer> pointers = new ArrayList<>();
        for (long i = start, m = end - data.length; i < m; i++) {
            byte[] oneByte = backend.mem_read(i, 1);
            if (data[0] != oneByte[0]) {
                continue;
            }
            if (Arrays.equals(data, backend.mem_read(i, data.length))) {
                pointers.add(UnidbgPointer.pointer(emulator, i));
                i += (data.length - 1);
            }
        }
        return pointers;
    }
}
