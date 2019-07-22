<#assign v1 = "<">
<#assign v2 = ">">

package ${pkg};

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;
import starter.base.dto.IdListRequest;
import starter.base.dto.PageResponse;
import starter.base.dto.ResponseEntity;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import starter.base.utils.acs.ACS;

/**
* ${module}
*
* @author ${author}
* @date ${date}
*/
@RestController
@Api(produces = "application/json", tags = "${module}管理")
public class ${modelNameUpperCamel}Ctrl {

@Resource
private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

@ACS
@ApiOperation("添加${module}")
@PostMapping("/${modelNameLowerCamel}")
public ResponseEntity${v1}Long${v2} add(@Valid @RequestBody ${modelNameUpperCamel}Request requestBean) {
Long id = ${modelNameLowerCamel}Service.add(requestBean);
return ResponseEntity.ok(id);
}

@ACS
@ApiOperation("修改${module}信息")
@PutMapping("/${modelNameLowerCamel}/{id}")
public ResponseEntity${v1}Long${v2} modify(@PathVariable("id") long id,
@Valid @RequestBody ${modelNameUpperCamel}Request requestBean) {
Long modifyId = ${modelNameLowerCamel}Service.modify(id, requestBean);
return ResponseEntity.ok(modifyId);
}

@ACS
@ApiOperation("id删除")
@GetMapping("/${modelNameLowerCamel}/delete/{id}")
public ResponseEntity${v1}String${v2} delete(@PathVariable("id") long id) {
${modelNameLowerCamel}Service.deleteById(id);
return ResponseEntity.ok();
}

@ACS
@ApiOperation("批量删除，传入数组")
@PostMapping("/${modelNameLowerCamel}/delete")
public ResponseEntity${v1}String${v2} delete(@Valid @RequestBody IdListRequest requestBean) {
${modelNameLowerCamel}Service.deleteByIds(requestBean.getIdList());
return ResponseEntity.ok();
}

//==================================查询接口=========================================================================

@ACS
@ApiOperation("id查找")
@GetMapping("/${modelNameLowerCamel}/{id}")
public ResponseEntity${v1}${modelNameUpperCamel}Response${v2} findById(@PathVariable("id") Long id) {
${modelNameUpperCamel}Response ${modelNameLowerCamel}Response = ${modelNameLowerCamel}Service.findById(id);
return ResponseEntity.ok(${modelNameLowerCamel}Response);
}

@ACS
@ApiOperation("分页查询")
@PostMapping("/${modelNameLowerCamel}/page")
public ResponseEntity${v1}PageResponse${v1}${modelNameUpperCamel}Response${v2}${v2} getPage(@Valid @RequestBody ${modelNameUpperCamel}PageRequest requestBean) {
PageResponse${v1}${modelNameUpperCamel}Response${v2} res = ${modelNameLowerCamel}Service.getPage(requestBean);
return ResponseEntity.ok(res);
}

@ACS
@ApiOperation("列表查询")
@GetMapping("/${modelNameLowerCamel}/list")
public ResponseEntity${v1}List${v1}${modelNameUpperCamel}Response>> getList() {
List${v1}${modelNameUpperCamel}Response${v2} res = ${modelNameLowerCamel}Service.getList();
return ResponseEntity.ok(res);
}

}