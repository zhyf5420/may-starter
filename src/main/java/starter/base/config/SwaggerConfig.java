package starter.base.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * Created on 2018/12/18 0018.
 *
 * @author zhyf
 */
@Configuration
public class SwaggerConfig {

    @Resource
    private AppConfig appConfig;

    @Resource
    private TypeResolver typeResolver;

    /**
     * 生成API文档的入口
     */
    @Bean
    public Docket generateApi() {
        //添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("Auth")
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(tokenPar.build());
        //添加head参数end

        Docket docket;
        // 可以根据配置决定不做任何API生成
        if (appConfig.getSwaggerShow() == 1) {
            docket = new Docket(DocumentationType.SWAGGER_2).select()
                                                            .apis(RequestHandlerSelectors.none())
                                                            .build();
            return docket;
        }

        docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                                                        .globalOperationParameters(pars)
                                                        .select()
                                                        // 标示只有被 @Api 标注的才能生成API.
                                                        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                                                        .paths(PathSelectors.any())
                                                        .build()
                                                        .pathMapping("/")
                                                        // 遇到 LocalDate时，输出成String
                                                        .directModelSubstitute(LocalDate.class, String.class)
                                                        .genericModelSubstitutes(ResponseEntity.class)
                                                        .alternateTypeRules(
                                                                newRule(
                                                                        typeResolver.resolve(DeferredResult.class,
                                                                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                                                        typeResolver.resolve(WildcardType.class)))
                                                        .useDefaultResponseMessages(false);
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("数据交互RESTFul API")
                .description("主要提供统一对外接口访问")
                .termsOfServiceUrl("仅供研发人员使用，内部资料！！")
                .version("V.1.0")
                .build();
    }

}
