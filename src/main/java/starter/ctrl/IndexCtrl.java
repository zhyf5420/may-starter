package starter.ctrl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import starter.base.dto.ResponseEntity;
import starter.base.utils.acs.ACS;

/**
 * Created on 2019/10/17.
 *
 * @author zhyf
 */
@RestController
@Api(produces = "application/json", tags = "xx")
public class IndexCtrl {

    @RequestMapping("/")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        return modelAndView;
    }

}