package starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zhyf $2a$10$9wKoH2BcrQ4ZupREqfxSveu3xvNdGf8VojLL4PRej1CCLSWKiId3m
 */
@EnableSwagger2
@SpringBootApplication
public class StarterApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }

}
