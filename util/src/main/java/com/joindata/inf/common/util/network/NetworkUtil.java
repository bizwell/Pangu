package com.joindata.inf.common.util.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Set;

import com.joindata.inf.common.basic.entities.StringMap;
import com.joindata.inf.common.basic.errors.ResourceErrors;
import com.joindata.inf.common.basic.exceptions.ResourceException;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.basic.ValidateUtil;
import com.joindata.inf.common.util.network.entity.HostPort;
import com.joindata.inf.common.util.network.entity.JdbcConn;
import com.joindata.inf.common.util.network.entity.ProtocolHostPort;
import com.xiaoleilu.hutool.util.NetUtil;

/**
 * 网络工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年12月1日 上午11:58:56
 */
public class NetworkUtil
{
    /**
     * 检查指定的本地端口是否可用
     * 
     * @param port 端口号
     * @return true，如果可用
     */
    public static final boolean isUsableLocalPort(int port)
    {
        return NetUtil.isUsableLocalPort(port);
    }

    /**
     * 在指定区间中寻找一个可用端口号
     * 
     * @param startPort 起始端口号
     * @param endPort 结束端口号
     * @return 最小可用端口号
     * @throws ResourceException 如果指定范围内没有可用端口号，抛出该异常
     */
    public static final int nextUsableLocalPort(int startPort, int endPort)
    {
        int port = startPort;
        while(!NetUtil.isUsableLocalPort(port))
        {
            port++;
        }

        if(port > endPort)
        {
            throw new ResourceException(ResourceErrors.NOT_FOUND, startPort + " ~ " + endPort + " 之间没有可用端口");
        }

        return port;
    }

    /**
     * 构建InetSocketAddress<br>
     * <b>当host中包含端口时（用“：”隔开），使用host中的端口，否则使用默认端口</b><br />
     * <b>给定host为空时使用本地host（127.0.0.1）</b>
     * 
     * @param host Host
     * @param defaultPort 默认端口
     */
    public static final InetSocketAddress defineInetAddress(String host, int defaultPort)
    {
        return NetUtil.buildInetSocketAddress(host, defaultPort);
    }

    /**
     * 获取本机 IP，并 调用 grepIpv4ByPrefix(Set<String>, String) 方法过滤出符合指定前缀的第一个元素
     * 
     * @param prefix 过滤开头
     * @return 本机 IP
     */
    public static String getLocalIpv4(String prefix)
    {
        return grepIpv4ByPrefix(NetUtil.localIpv4s(), prefix);
    }

    /**
     * 获取本机所有的 IPv4 地址
     * 
     * @return 地址列表
     */
    public static Set<String> getLocalIpv4s()
    {
        return NetUtil.localIpv4s();
    }

    /**
     * 过滤 IPv4 集合中第一个以指定段开头的元素
     * 
     * @param ipSet IPv4 集合
     * @param startsWith 过滤开头
     * @return 第一个匹配的元素。如果没有匹配，返回 null
     */
    public static final String grepIpv4ByPrefix(Collection<String> ips, String startsWith)
    {
        if(ips == null)
        {
            return null;
        }

        for(String ip: ips)
        {

            if(StringUtil.startsWith(StringUtil.trim(ip), startsWith))
            {
                return ip;
            }
        }

        return null;
    }

    /***
     * 判断目标端口是否开放<br />
     * <b>超时时间是1s</b>
     * 
     * @param host 主机名或 IP
     * @param port 端口号
     * @return true，如果是开放的
     */
    public static boolean isReachable(String host, int port)
    {
        try
        {
            InetAddress theAddress = InetAddress.getByName(host);
            Socket socket = new Socket(theAddress, port);
            socket.setSoTimeout(1000);
            boolean connected = socket.isConnected();
            socket.close();
            return connected;
        }
        catch(IOException e)
        {
            return false;
        }
    }

    /***
     * 判断目标端口是否开放<br />
     * <b>这个方法可以指定重试次数，如果第一次连接不同，就多测几次，如果实在不通，那就返回不通</b>
     * 
     * @param host 主机名或 IP
     * @param port 端口号
     * @param retryTimes 重试几次
     * @param timeout 每次重试间隔多少毫秒
     * @return true，如果是开放的
     */
    public static boolean isReachable(String host, int port, int retryTimes, int timeout)
    {
        try
        {
            InetAddress theAddress = InetAddress.getByName(host);
            Socket socket = new Socket(theAddress, port);
            socket.setSoTimeout(timeout);
            boolean connected = socket.isConnected();

            if(!connected)
            {
                for(int i = 0; i < retryTimes; i++)
                {
                    if(socket.isConnected())
                    {
                        connected = true;
                        break;
                    }
                }
            }

            socket.close();
            return connected;
        }
        catch(IOException e)
        {
            return false;
        }
    }

    /**
     * 解析主机名+端口号串<br />
     * <i>如果 port 没写，当成 80 处理了</i>
     * 
     * @param hostPortStr 可以是 host1:port1,host2:port2 这样的，也可以是 host:port 这样的
     * @return 主机名端口号对，如果是逗号分割的多个主机名端口号，返回的数组数量就是多个；如果是 1 个，数组数量就是 1 个
     */
    public static HostPort[] parseHostPort(String hostPortStr)
    {
        String[] strs = StringUtil.splitToArray(hostPortStr, ",");

        HostPort[] hostPort = new HostPort[strs.length];

        int i = 0;
        for(String str: strs)
        {
            String[] pair = str.split(":");
            hostPort[i++] = new HostPort(pair[0], StringUtil.isBlank(pair[1]) ? 80 : Integer.parseInt(pair[1]));
        }

        return hostPort;
    }

    /**
     * 解析协议+主机名+端口号串<br />
     * <i>如果 port 没写，当成 80 处理了。如果 protocol 没写，当 http 处理了。如果 protocol 和 port 都没写，也 OK，就用前面两个默认值</i>
     * 
     * @param protocolHostPortStr 可以是 protocol1://host1:port1,protocol2://host2:port2 这样的，也可以是 protocol://host:port 这样的
     * @return 协议、主机名端口号对，如果是逗号分割的多个主机名端口号，返回的数组数量就是多个；如果是 1 个，数组数量就是 1 个
     */
    public static ProtocolHostPort[] parseProtocolHostPort(String protocolHostPortStr)
    {
        String[] strs = StringUtil.splitToArray(protocolHostPortStr, ",");

        ProtocolHostPort[] protocolHostPorts = new ProtocolHostPort[strs.length];

        int i = 0;
        for(String str: strs)
        {
            String part1 = null;
            String part2 = null;
            int part3 = 80;

            // 出现两次冒号代表有端口号和协议
            if(StringUtil.countMatches(str, ":") == 2)
            {
                part1 = StringUtil.substringBeforeFirst(str, ":");
                part2 = StringUtil.substringBeforeLast(StringUtil.substringAfterFirst(str, "//"), ":");
                part3 = Integer.parseInt(StringUtil.substringAfterLast(str, ":"));
            }

            // 出现一次冒号，需要判断是协议还是端口号
            if(StringUtil.countMatches(str, ":") == 1)
            {
                // 如果是数字，一定是端口号，默认给 HTTP 协议
                if(ValidateUtil.isPureNumberText(StringUtil.substringAfterLast(str, ":")))
                {
                    part1 = "http";
                    part2 = StringUtil.substringBeforeLast(str, ":");
                    part3 = Integer.parseInt(StringUtil.substringAfterLast(str, ":"));
                }
                // 否则，一定是没有带端口号的，默认给 80 端口号
                else
                {
                    part1 = StringUtil.substringBeforeFirst(str, ":");
                    part2 = StringUtil.substringAfterFirst(str, ":");
                    part3 = 80;
                }
            }

            // 也可以是个奇葩，啥也没就直接当成只有主机名了
            if(StringUtil.countMatches(str, ":") == 0)
            {
                part1 = "http";
                part2 = str;
                part3 = 80;
            }

            protocolHostPorts[i++] = new ProtocolHostPort(part1, part2, part3);
        }

        return protocolHostPorts;
    }

    /**
     * 解析 JDBC 连接串
     * 
     * @param connStr 连接串
     * @return 连接串
     */
    public static JdbcConn parseJdbcConn(String srcStr)
    {
        if(StringUtil.isNullOrEmpty(srcStr) || !srcStr.startsWith("jdbc"))
        {
            return null;
        }

        String connStr = StringUtil.replaceFirst(srcStr, "jdbc:", "");

        JdbcConn conn = new JdbcConn();
        conn.setDbType(StringUtil.substringBeforeFirst(connStr, "://"));

        connStr = StringUtil.replaceFirst(connStr, conn.getDbType() + "://", "");

        conn.setHostPort(NetworkUtil.parseHostPort(StringUtil.substringBeforeFirst(connStr, "/"))[0]);

        connStr = StringUtil.replaceFirst(connStr, conn.getHostPort() + "/", "");

        conn.setDbName(StringUtil.substringBeforeFirst(connStr, "?"));

        StringMap params = new StringMap();

        String[] paramStr = StringUtil.splitToArray(StringUtil.substringAfterFirst(connStr, "?"), "&");
        for(String param: paramStr)
        {
            String[] kv = StringUtil.splitToArray(param, "=");
            params.put(kv[0], kv.length == 2 ? kv[1] : "");
        }

        conn.setParams(params);

        return conn;
    }

    public static void main(String[] args) throws ResourceException, UnknownHostException, IOException
    {
        System.out.println(nextUsableLocalPort(1000, 2000));

        System.out.println(defineInetAddress("localhost", 8088));

        System.out.println(getLocalIpv4("192"));

        System.out.println(getLocalIpv4s());

        System.out.println(isReachable("baidu.com", 80));

        System.out.println(parseJdbcConn("jdbc:mysql://localhost:3306/order_store_2?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=false"));
    }

}
