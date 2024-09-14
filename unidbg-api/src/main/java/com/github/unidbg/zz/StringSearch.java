package com.github.unidbg.zz;

import com.github.unidbg.Emulator;
import com.github.unidbg.arm.backend.*;
import com.github.unidbg.pointer.UnidbgPointer;
import com.sun.jna.Pointer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * 代码来源：https://www.yuque.com/lilac-2hqvv/rgvr95/xkuidkkwxox0t68u
 */
public class StringSearch {
    private final Emulator<?> emulator;
    private final TreeSet<Long> addressUsed = new TreeSet<>();
    private final Map<Long, Byte> memoryData = new HashMap<>();
    private Set<String> foundStrings = new HashSet<>();
    private int interval;
    private int minLength;
    private long count = 0;
    private final List<long[]> activePlace = new ArrayList<>();
    private PrintWriter output;

    public StringSearch(Emulator<?> emulator, int minLength, String outputFile) {
        this.emulator = emulator;
        this.interval = 100;
        this.minLength = minLength;
        try {
            this.output = new PrintWriter(new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        hookReadAndWrite();
        hookBlock();
    }

    public StringSearch(Emulator<?> emulator, int minLength, int interval, String outputFile){
        this.emulator = emulator;
        this.interval = interval;
        this.minLength = minLength;
        try {
            this.output = new PrintWriter(new FileOutputStream(outputFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        hookReadAndWrite();
        hookBlock();
    }

    private void hookReadAndWrite() {
        WriteHook writeHook = new WriteHook() {
            @Override
            public void onAttach(UnHook unHook) {

            }

            @Override
            public void detach() {

            }

            @Override
            public void hook(Backend backend, long address, int size, long value, Object user) {
                for (int offset = 0; offset < size; offset++) {
                    memoryData.put(address + offset, (byte) (value >>> (offset * 8)));
                }
                updateActivePlace(address, size);
            }
        };

        ReadHook readHook = new ReadHook() {
            @Override
            public void onAttach(UnHook unHook) {

            }

            @Override
            public void detach() {

            }

            @Override
            public void hook(Backend backend, long address, int size, Object user) {
                byte[] readBytes = emulator.getBackend().mem_read(address, size);
                for (int offset = 0; offset < size; offset++) {
                    memoryData.put(address + offset, readBytes[offset]);
                }
                updateActivePlace(address, size);
            }
        };

        emulator.getBackend().hook_add_new(writeHook, 1, 0, null);
        emulator.getBackend().hook_add_new(readHook, 1, 0, null);
    }

    private void hookBlock() {
        BlockHook blockHook = new BlockHook() {
            @Override
            public void onAttach(UnHook unHook) {

            }

            @Override
            public void detach() {

            }

            @Override
            public void hookBlock(Backend backend, long address, int size, Object user) {
                count++;
                if (count % interval == 0){
                    searchString();
                }
            }
        };

        emulator.getBackend().hook_add_new(blockHook, 1, 0, null);
    }


    private void searchString() {
        for (long[] range : activePlace) {
            searchStringInRange(range);
        }
    }

    private void searchStringInRange(long[] range) {
        int length = (int) (range[1] - range[0] + 1);
        byte[] codes = new byte[length];
        for (int i = 0; i < length; i++) {
            codes[i] = memoryData.get(i + range[0]);
        }
        searchVisibleStringsInMemory(codes, range[0]);
    }

    public void searchVisibleStringsInMemory(byte[] memory, long addr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < memory.length; i++) {
            byte b = memory[i];
            if (b >= 32 && b <= 126) {
                sb.append((char) b);
            } else {
                processVisibleString(sb, addr + i - sb.length());
            }
        }

        processVisibleString(sb, addr + memory.length - sb.length());
    }

    private void processVisibleString(StringBuilder sb, long addr) {
        if (sb.length() >= minLength) {
            String visibleString = sb.toString();
            if (foundStrings.add(visibleString)) {
                String result = "Address: 0x" + Long.toHexString(addr) + ", String: " + visibleString;
                output.println(result);
                output.flush();
            }
        }
        sb.setLength(0);
    }

    private void updateActivePlace(long address, int size) {
        for (int i = 0; i < size; i++) {
            long newAddress = address + i;
            if (addressUsed.add(newAddress)) {
                updateActivePlace(newAddress);
            }
        }
    }


    private void updateActivePlace(long address) {
        Long lower = addressUsed.lower(address);
        Long higher = addressUsed.higher(address);

        boolean connectToLower = lower != null && lower + 1 == address;
        boolean connectToHigher = higher != null && address + 1 == higher;

        long[] lowerRange = null;
        long[] higherRange = null;

        for (long[] range : activePlace) {
            if (connectToLower && range[1] == lower) {
                lowerRange = range;
            }
            if (connectToHigher && range[0] == higher) {
                higherRange = range;
            }
        }

        if (connectToLower && connectToHigher) {
            assert lowerRange != null;
            lowerRange[1] = higherRange[1];
            activePlace.remove(higherRange);
        } else if (connectToLower) {
            assert lowerRange != null;
            lowerRange[1] = address;
        } else if (connectToHigher) {
            assert higherRange != null;
            higherRange[0] = address;
        } else {
            activePlace.add(new long[]{address, address});
        }
    }

}