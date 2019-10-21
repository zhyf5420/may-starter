package starter.ctrl.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import starter.base.dto.ResponseEntity;
import starter.base.utils.RandomUtil;
import starter.base.utils.acs.ACS;
import starter.dto.file.CreateExcelRequest;
import starter.service.FileService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * Created on 2019/3/10.
 *
 * @author zhyf
 */
@RestController
@RequestMapping("/file")
@Api(produces = "application/json", tags = "文件管理")
public class FileCtrl {

    @Resource
    private FileService fileService;

    @ACS(allowAnonymous = true)
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String url = fileService.upload(file.getOriginalFilename(), file.getBytes());
        return ResponseEntity.ok(url);
    }

    @ACS
    @ApiOperation(value = "excel导出通用接口")
    @PostMapping("/excel-gen")
    public ResponseEntity<String> createExcel(@Valid @RequestBody CreateExcelRequest requestBean) {
        String url = fileService.createExcel(requestBean, RandomUtil.uuid()).getUrl();
        return ResponseEntity.ok(url);
    }

}
