package starter.base.utils;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.poi.excel.ExcelUtil;
import starter.base.constants.ResponseCode;
import starter.base.exception.BusinessException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 文件工具类
 *
 * @author zhyf
 */
public class FileUtil {

    /**
     * 读取txt
     *
     * @return 每一行的列表
     */
    public static List<String> readTxt(File file) {
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtil.isEmpty(line)) {
                    continue;
                }
                result.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<List<String>> readCsv(File tempFile) {
        CsvData read = new CsvReader().read(tempFile, CharsetUtil.CHARSET_UTF_8);
        if (read.getRowCount() == 0) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_ERROR, "文件内容为空");
        }

        return read.getRows().stream()
                   .map(CsvRow::getRawList)
                   .collect(Collectors.toList());
    }

    public static List<List<String>> readExcel(File tempFile) {
        List<List<Object>> dataList = ExcelUtil.getReader(tempFile).read();
        if (CollectionUtil.isEmpty(dataList)) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_ERROR, "文件内容为空");
        }

        return dataList.stream()
                       .map(line -> line.stream()
                                        .map(o -> o == null ? "" : o + "")
                                        .collect(toList()))
                       .collect(toList());
    }

}
