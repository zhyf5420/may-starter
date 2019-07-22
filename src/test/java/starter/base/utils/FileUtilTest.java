package starter.base.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created on 2019/5/16.
 *
 * @author zhyf
 */
public class FileUtilTest {

    @Test
    public void readExcel() throws IOException {
        File file = new File("d:/aaa.txt");
        List<String> strings = FileUtil.readTxt(file);
        strings.forEach(s -> System.out.println(s));
    }

}