package starter.base.utils.ssh;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommandResBean {

    private List<String> resList;

    private List<String> errList;

}