package starter.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhyf
 */
@Component
@Data
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    /***
     * 0 api接口显示 1 不显示
     */
    private int swaggerShow = 0;

    /**
     * 缓存文件存放路径
     */
    private String tempPath;

    /**
     * 上传文件存放路径
     */
    private String filePath;

}
