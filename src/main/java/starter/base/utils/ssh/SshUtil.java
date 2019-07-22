package starter.base.utils.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import lombok.extern.slf4j.Slf4j;
import starter.base.constants.ResponseCode;
import starter.base.exception.BusinessException;
import starter.base.utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * ssh 连接工具
 *
 * @author zhyf
 */
@Slf4j
public class SshUtil {

    public static Connection getConnection(String hostname, String username, String password) {
        if (StringUtil.isEmpty(hostname) || StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_ERROR);
        }

        Connection connection = new Connection(hostname);
        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(username, password);
            if (!isAuthenticated) {
                throw new BusinessException(ResponseCode.FAIL, "SSH Login " + hostname + " Authentication failed");
            } else {
                return connection;
            }
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, e.getMessage());
        }
    }

    public static CommandResBean execCommand(Connection connection, String command) {
        if (connection == null) {
            throw new BusinessException(ResponseCode.SYSTEM_DATA_ERROR, "connection==null");
        }

        List<String> resList = new ArrayList<>();
        List<String> errList = new ArrayList<>();
        CommandResBean commandRes = new CommandResBean(resList, errList);
        Session session = null;
        BufferedReader reader = null;
        try {
            session = connection.openSession();
            session.execCommand(command);

            reader = new BufferedReader(new InputStreamReader(new StreamGobbler(session.getStdout())));
            String line;
            while ((line = reader.readLine()) != null) {
                resList.add(line);
            }

            reader = new BufferedReader(new InputStreamReader(new StreamGobbler(session.getStderr())));
            while ((line = reader.readLine()) != null) {
                errList.add(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException(ResponseCode.SYSTEM_DATA_ERROR, e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (session != null) {
                session.close();
            }
            connection.close();
        }
        return commandRes;
    }

    public static void main(String[] args) {
        Connection connection = SshUtil.getConnection("192.168.1.66", "root", "123456");
        CommandResBean commandResBean = SshUtil.execCommand(connection, "ifconfig");
        System.out.println("ResList=" + commandResBean.getResList());
        System.out.println("ErrList=" + commandResBean.getErrList());
    }

}
