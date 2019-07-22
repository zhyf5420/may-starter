package codeGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created on 2019/1/8 0008.
 *
 * @author zhyf
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClassBean {

    private String value;

    private String field;

    public ClassBean(String value, String field) {
        this.value = value;
        this.field = field;
    }

}
