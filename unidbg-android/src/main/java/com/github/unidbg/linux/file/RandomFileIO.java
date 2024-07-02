package com.github.unidbg.linux.file;

import com.github.unidbg.Emulator;
import com.github.unidbg.arm.backend.Backend;
import com.github.unidbg.file.linux.IOConstants;
import com.github.unidbg.zz.ZZConfig;
import com.sun.jna.Pointer;

import java.util.concurrent.ThreadLocalRandom;

public class RandomFileIO extends DriverFileIO {

    public RandomFileIO(Emulator<?> emulator, String path) {
        super(emulator, IOConstants.O_RDONLY, path);
    }

    @Override
    public int read(Backend backend, Pointer buffer, int count) {
        int total = 0;
        byte[] buf = new byte[Math.min(0x1000, count)];
        randBytes(buf);
        Pointer pointer = buffer;
        while (total < count) {
            int read = Math.min(buf.length, count - total);
            pointer.write(0, buf, 0, read);
            total += read;
            pointer = pointer.share(read);
        }
        return total;
    }

    protected void randBytes(byte[] bytes) {

        //通过开关固定时间戳
        if(ZZConfig.fix_ramdom_file) {
            //啥也不做，那么返回的字节数组元素就会默认全 0。
        } else {
            ThreadLocalRandom.current().nextBytes(bytes);
        }
    }
}
