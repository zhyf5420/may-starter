package starter.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * @author zhyf
 */
public class IpUtil {

    public static String INTRANET_IP = getIntranetIp(); // 内网IP

    public static String INTERNET_IP = getInternetIp(); // 外网IP

    private static Logger log = LoggerFactory.getLogger(IpUtil.class);

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!StringUtil.isBlank(ip) && ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获得内网IP
     *
     * @return 内网IP
     */
    private static String getIntranetIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得外网IP
     *
     * @return 外网IP
     */
    private static String getInternetIp() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements()) {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements()) {
                    ip = addrs.nextElement();
                    if (ip instanceof Inet4Address
                            && ip.isSiteLocalAddress()
                            && !ip.getHostAddress().equals(INTRANET_IP)) {
                        return ip.getHostAddress();
                    }
                }
            }

            // 如果没有外网IP，就返回内网IP
            return INTRANET_IP;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取能与远程主机指定端口建立连接的本机ip地址
     */
    public static String getReachableIP(String remoteIp, int port) {
        String retIP = null;
        try {
            InetAddress remoteAddress = InetAddress.getByName(remoteIp);
            retIP = getReachableIP(remoteAddress, port);
        } catch (UnknownHostException e) {
            log.error("Error occurred while listing all the local network addresses:" + e.getMessage());
        }

        return retIP;
    }

    /**
     * 获取能与远程主机指定端口建立连接的本机ip地址
     */
    public static String getReachableIP(InetAddress remoteAddress, int port) {
        String retIP = null;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = netInterfaces.nextElement();
                Enumeration<InetAddress> localAddrs = networkInterface.getInetAddresses();
                while (localAddrs.hasMoreElements()) {
                    InetAddress localAddr = localAddrs.nextElement();
                    if (isReachable(localAddr, remoteAddress, port, 5000)) {
                        retIP = localAddr.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            log.error("Error occurred while listing all the local network addresses:" + e.getMessage());
        }

        return retIP;
    }

    /**
     * 测试 localInetAddress 能否与远程的主机指定端口建立连接相连
     */
    public static boolean isReachable(InetAddress localInetAddress, InetAddress remoteInetAddress, int port, int timeout) {
        boolean isReachable = false;
        try (Socket socket = new Socket()) {
            // 端口号设置为 0 表示在本地挑选一个可用端口进行连接
            SocketAddress localSocketAddr = new InetSocketAddress(localInetAddress, 0);
            socket.bind(localSocketAddr);
            InetSocketAddress endpointSocketAddr = new InetSocketAddress(remoteInetAddress, port);
            socket.connect(endpointSocketAddr, timeout);
            isReachable = true;
        } catch (IOException e) {
            log.error("FAILRE - CAN not connect! Local: " + localInetAddress.getHostAddress() + " remote: " + remoteInetAddress.getHostAddress() + " port" + port);
        }

        return isReachable;
    }

    /**
     * 测试localIp能否与远程的主机指定端口建立连接相连
     */
    public static boolean isReachable(String localIp, String remoteIp, int port, int timeout) {
        boolean isReachable = false;
        try {
            InetAddress localInetAddress = InetAddress.getByName(localIp);
            InetAddress remoteInetAddress = InetAddress.getByName(remoteIp);
            isReachable = isReachable(localInetAddress, remoteInetAddress, port, timeout);
        } catch (IOException e) {
            log.error("FAILRE - CAN not connect! Local: " + localIp + " remote: " + remoteIp + " port" + port);
        }

        return isReachable;
    }

    public static void main(String[] args) {
        System.out.println(isNodeReachable("192.168.3.1"));
        System.out.println(isReachNetworkInterfaces("192.168.3.1"));
        System.out.println(isNodeReachable("192.168.1.166"));
        System.out.println(isReachNetworkInterfaces("192.168.1.166"));
        System.out.println(isNodeReachable("192.168.1.66"));
        System.out.println(isReachNetworkInterfaces("192.168.1.66"));
    }

    public static final boolean isNodeReachable(String hostname) {
        try {
            return 0 == Runtime.getRuntime().exec("ping -c 1 " + hostname).waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 遍历本地所有的网卡地址测试能否ping通 ip
     */
    public static boolean isReachNetworkInterfaces(String ip) {
        boolean isReach = false;
        try {
            // ping this IP
            InetAddress address = InetAddress.getByName(ip);
            if (address.isReachable(5000)) {
                isReach = isReachIp(ip);
            }

            if (!isReach) {
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    if (address.isReachable(ni, 0, 5000)) {
                        isReach = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("error occurs:" + e.getMessage());
        }

        return isReach;
    }

    /**
     * 测试本地能否ping ip
     */
    public static boolean isReachIp(String ip) {
        boolean isReach = false;
        try {
            // ping this IP
            InetAddress address = InetAddress.getByName(ip);
            if (address.isReachable(5000)) {
                isReach = true;
            }
        } catch (Exception e) {
            log.error("error occurs:" + e.getMessage());
        }

        return isReach;
    }

}
