package starter.bussiness.ctrl_rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import starter.base.dto.ResponseEntity;
import starter.base.utils.RandomUtil;
import starter.base.utils.acs.ACS;
import starter.bussiness.service.FileService;
import starter.bussiness.dto.file.ExcelRequest;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created on 2019/3/10.
 *
 * @author zhyf
 */
@RestController
@Api(produces = "application/json", tags = "文件管理")
public class FileCtrl {

    @Resource
    private FileService fileService;

    @ACS(allowAnonymous = true)
    @ApiOperation("上传文件")
    @PostMapping("/file/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String url = fileService.upload(file.getOriginalFilename(), file.getBytes());
        return ResponseEntity.ok(url);
    }

    @ACS
    @ApiOperation(value = "excel导出通用接口")
    @PostMapping("/file/excel-gen")
    public ResponseEntity<String> createExcel(@Valid @RequestBody ExcelRequest requestBean) {
        String url = fileService.createExcel(requestBean, RandomUtil.uuid()).getUrl();
        return ResponseEntity.ok(url);
    }

}
