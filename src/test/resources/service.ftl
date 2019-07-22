<#assign v1 = "<">
<#assign v2 = ">">

package ${pkg};

import com.github.wenhao.jpa.Specifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starter.base.constants.ResponseCode;
import starter.base.exception.BusinessException;
import starter.base.utils.ObjectUtil;
import starter.base.utils.StringUtil;
import starter.base.dto.PageRequest;
import starter.base.dto.PageResponse;
import starter.entity.${modelNameUpperCamel};

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* ${module}
*
* @author ${author}
* @date ${date}
*/
@Slf4j
@Service
public class ${modelNameUpperCamel}Service {

@Resource
private ${modelNameUpperCamel}Repository ${modelNameLowerCamel}Repository;

private ${modelNameUpperCamel}Response convert2Response(${modelNameUpperCamel} ${modelNameLowerCamel}) {
${modelNameUpperCamel}Response response = ObjectUtil.getCopyBean(${modelNameLowerCamel}, new ${modelNameUpperCamel}Response());
//todo
return response;
}

private void convert2Entity(${modelNameUpperCamel}Request requestBean, ${modelNameUpperCamel} ${modelNameLowerCamel}) {
ObjectUtil.copyNotNullBean(requestBean, ${modelNameLowerCamel});
//todo
}

/**
* 添加
*/
@Transactional
public Long add(${modelNameUpperCamel}Request requestBean) {
${modelNameUpperCamel} ${modelNameLowerCamel} = new ${modelNameUpperCamel}();
convert2Entity(requestBean, ${modelNameLowerCamel});
${modelNameLowerCamel} = ${modelNameLowerCamel}Repository.save(${modelNameLowerCamel});
return ${modelNameLowerCamel}.getId();
}

/**
* 修改
*/
@Transactional
public Long modify(Long id, ${modelNameUpperCamel}Request requestBean) {
${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Repository.getAndCheck(id);
convert2Entity(requestBean, ${modelNameLowerCamel});
${modelNameLowerCamel} = ${modelNameLowerCamel}Repository.save(${modelNameLowerCamel});
return ${modelNameLowerCamel}.getId();
}

/**
* id删除
*/
@Transactional
public void deleteById(Long id) {
${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Repository.getAndCheck(id);
${modelNameLowerCamel}Repository.delete(${modelNameLowerCamel});
}

/**
* 批量删除
*/
@Transactional
public void deleteByIds(List${v1}Long${v2} ids) {
ids.forEach(this::deleteById);
}

/**
* id查询
*/
public ${modelNameUpperCamel}Response findById(Long id) {
${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Repository.getAndCheck(id);
return convert2Response(${modelNameLowerCamel});
}

/**
* 分页查询
*/
public PageResponse${v1}${modelNameUpperCamel}Response${v2} getPage(${modelNameUpperCamel}PageRequest requestBean) {
// 1、构建分页请求
Pageable pageable = PageRequest.of(requestBean);

// 2、构建查询条件
Specification${v1}${modelNameUpperCamel}${v2} specification = Specifications.${v1}${modelNameUpperCamel}${v2}and()
//todo
.build();

// 3、查询结果集
Page${v1}${modelNameUpperCamel}Response${v2} responsePage = ${modelNameLowerCamel}Repository.findAll(specification, pageable)
.map(this::convert2Response);
return PageResponse.of(responsePage);
}

/**
* 查询列表
*/
public List${v1}${modelNameUpperCamel}Response${v2} getList() {
Specification${v1}${modelNameUpperCamel}${v2} specification = Specifications.${v1}${modelNameUpperCamel}${v2}and()
//todo
.build();

return ${modelNameLowerCamel}Repository.findAll(specification).stream()
.map(this::convert2Response)
.collect(Collectors.toList());
}

}
