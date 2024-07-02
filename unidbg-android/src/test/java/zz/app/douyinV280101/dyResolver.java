package zz.app.douyinV280101;

import com.github.unidbg.Emulator;
import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.IOResolver;

public class dyResolver implements IOResolver {

    @Override
    public FileResult resolve(Emulator emulator, String pathname, int oflags) {
        System.out.println("File open: " + pathname);
        return null;
    }

}
