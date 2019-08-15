package starter.bussiness.ctrl_rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import starter.base.dto.PageResponse;
import starter.base.dto.ResponseEntity;
import starter.base.utils.CollectionUtil;
import starter.bussiness.dto.account.AccountResponse;

import javax.validation.Valid;
import java.util.List;

/**
 * Created on 2019/8/13.
 *
 * @author zhyf
 */
@Slf4j
@RestController
@Api(produces = "application/json", tags = "xx")
public class DemoCtrl {

    @ApiOperation("demo")
    @PostMapping("/demo/table/user")
    public ResponseEntity<PageResponse<AccountResponse>> demo(@RequestBody @Valid DemoRequest request) {
        log.info("page{} limit{} name{}", request.getPage(), request.getSize(), request.getUserName());

        List<AccountResponse> arrayList = CollectionUtil.newArrayList();

        for (int i = 0; i < request.getSize(); i++) {
            arrayList.add(AccountResponse.getInstance());
        }

        PageResponse<AccountResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(arrayList);
        pageResponse.setTotalElements(1000);
        return ResponseEntity.ok(pageResponse);
    }

}