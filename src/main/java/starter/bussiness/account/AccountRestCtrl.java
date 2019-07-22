package starter.bussiness.account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import starter.base.dto.IdListRequest;
import starter.base.dto.PageResponse;
import starter.base.dto.ResponseEntity;
import starter.base.utils.acs.ACS;
import starter.bussiness.account.dto.AccountPageRequest;
import starter.bussiness.account.dto.AccountPasswordRequest;
import starter.bussiness.account.dto.AccountRequest;
import starter.bussiness.account.dto.AccountResponse;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 账户
 *
 * @author zhyf
 */
@RestController
@Api(produces = "application/json", tags = "账户管理")
public class AccountRestCtrl {

    @Resource
    private AccountService accountService;

    @ACS(allowAnonymous = true)
    @ApiOperation("注册 添加账户")
    @PostMapping("/account")
    public ResponseEntity<Long> add(@Valid @RequestBody AccountRequest requestBean) {
        Long id = accountService.save(requestBean);
        return ResponseEntity.ok(id);
    }

    @ACS
    @ApiOperation("修改账户信息")
    @PutMapping("/account/{id}")
    public ResponseEntity<Long> modify(@PathVariable("id") long id,
                                       @Valid @RequestBody AccountRequest requestBean) {
        Long modifyId = accountService.update(id, requestBean);
        return ResponseEntity.ok(modifyId);
    }

    @ACS
    @ApiOperation("修改账户密码")
    @PutMapping("/account/password/{id}")
    public ResponseEntity<Long> modifyPassword(@PathVariable("id") long id,
                                               @Valid @RequestBody AccountPasswordRequest requestBean) {
        Long modifyId = accountService.modifyPassword(id, requestBean);
        return ResponseEntity.ok(modifyId);
    }

    @ACS
    @ApiOperation("初始化账户密码")
    @PutMapping("/account/init-password/{id}")
    public ResponseEntity<String> initPassword(@PathVariable("id") long id) {
        accountService.initPassword(id);
        return ResponseEntity.ok();
    }

    @ACS
    @ApiOperation("根据id删除")
    @DeleteMapping("/account/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        accountService.deleteById(id);
        return ResponseEntity.ok();
    }

    @ACS
    @ApiOperation("批量删除，传入数组")
    @DeleteMapping("/account")
    public ResponseEntity<String> delete(@Valid @RequestBody IdListRequest requestBean) {
        accountService.deleteByIds(requestBean.getIdList());
        return ResponseEntity.ok();
    }

    //==================================查询接口=========================================================================
    @ACS
    @ApiOperation("根据id查找")
    @GetMapping("/account/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable("id") Long id) {
        AccountResponse accountResponse = accountService.findById(id);
        return ResponseEntity.ok(accountResponse);
    }

    @ACS
    @ApiOperation("分页查询")
    @PostMapping("/account/page")
    public ResponseEntity<PageResponse<AccountResponse>> getPage(@Valid @RequestBody AccountPageRequest requestBean) {
        PageResponse<AccountResponse> pageResponse = accountService.getPage(requestBean);
        return ResponseEntity.ok(pageResponse);
    }

}