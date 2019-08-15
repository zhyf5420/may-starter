package starter.bussiness.dto.file;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;

/**
 * Created on 2019/5/22.
 *
 * @author zhyf
 */
@Data
@Accessors(chain = true)
public class FileBean {

    private String url;

    private File file;

}