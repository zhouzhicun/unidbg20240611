package zz.app.base;

import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.linux.AndroidFileIO;
import com.github.unidbg.linux.file.ByteArrayFileIO;
import com.github.unidbg.linux.file.SimpleFileIO;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class IOResolverUtil {

    //返回byteFile
    public static FileResult<AndroidFileIO> createByteFile(String pathname, int oflags, String content) {
        return createByteFile(pathname, oflags, content.getBytes(StandardCharsets.UTF_8));
    }

    //返回byteFile
    public static FileResult<AndroidFileIO> createByteFile(String pathname, int oflags, byte[] bytes) {
        return FileResult.success(new ByteArrayFileIO(oflags, pathname, bytes));
    }

    //返回本地文件
    public static FileResult<AndroidFileIO> createLocalFile(String pathname, int oflags, String localFilePath) {
        File localFile = loadLocalFile(localFilePath);
        if(localFile == null) {
            return null;
        }
        return FileResult.success(new SimpleFileIO(oflags, localFile, pathname));
    }

    private static File loadLocalFile(String filePath) {
        File localFile = new File(filePath);
        if (!localFile.exists()) {
            System.out.println("本地文件不存在：path = " + filePath);
            return null;
        }
        return localFile;
    }


}
