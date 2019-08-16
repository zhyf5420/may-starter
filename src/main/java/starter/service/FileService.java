package starter.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import starter.base.config.AppConfig;
import starter.base.utils.CollectionUtil;
import starter.base.utils.RandomUtil;
import starter.dto.file.CreateExcelRequest;
import starter.dto.file.FileBean;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/10/22 0022.
 *
 * @author zhyf
 */
@Slf4j
@Service
public class FileService {

    @Resource
    private AppConfig appConfig;

    /**
     * 上传文件
     *
     * @return 文件url
     */
    public String upload(String filename, byte[] data) {
        String uuid = RandomUtil.uuid();
        String path = appConfig.getFilePath() + File.separator + uuid + File.separator + filename;
        FileUtil.writeBytes(data, path);
        return uuid + "/" + filename;
    }

    /**
     * 保存临时文件
     *
     * @return 本地临时文件
     */
    public File saveTempFile(byte[] data) {
        File tempFile = new File(appConfig.getTempPath() + File.separator + RandomUtil.uuid());
        FileUtil.writeBytes(data, tempFile);
        return tempFile;
    }

    /**
     * 保存临时文件
     *
     * @return 本地临时文件
     */
    public File saveTempFile(Collection<String> lines) {
        File tempFile = new File(appConfig.getTempPath() + File.separator + RandomUtil.uuid());
        FileUtil.writeUtf8Lines(lines, tempFile);
        return tempFile;
    }

    /**
     * 通用excel导出
     */
    public FileBean createExcel(CreateExcelRequest requestBean, String fileName) {
        String url = RandomUtil.uuid() + File.separator + fileName + ".xlsx";
        File file = new File(appConfig.getTempPath() + File.separator + url);
        ExcelWriter writer = ExcelUtil.getWriter(file);
        if (CollectionUtil.isNotEmpty(requestBean.getMapContent())) {
            List<Map<String, String>> rows = requestBean.getMapContent();
            if (requestBean.getHeadName() != null) {
                // 合并单元格后的标题行，使用默认标题样式
                writer.merge(rows.get(0).size() - 1, requestBean.getHeadName());
            }

            // 一次性写出内容，使用默认样式
            writer.write(rows, true);
        } else if (CollectionUtil.isNotEmpty(requestBean.getListContent())) {
            List<List<String>> rows = requestBean.getListContent();

            if (requestBean.getHeadName() != null) {
                // 合并单元格后的标题行，使用默认标题样式
                writer.merge(rows.get(0).size() - 1, requestBean.getHeadName());
            }

            // 一次性写出内容，使用默认样式
            writer.write(rows, true);
        }
        // 关闭writer，释放内存
        writer.close();

        return new FileBean().setFile(file)
                             .setUrl(url);
    }

    public FileBean createTempFile(String fileName) {
        String url = RandomUtil.uuid() + File.separator + fileName;
        String path = appConfig.getTempPath() + File.separator + url;
        return new FileBean().setFile(new File(path))
                             .setUrl(url);
    }

}
