package com.example.jiaqili.coolchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Jiaqi Li on 2018/4/1.
 */

public class UdpMessageTool {     // DatagramSocket代表UDP协议的Socket,作用就是接收和发送数据报
    private byte[] bytes = new byte[2048];
    private DatagramSocket mDatagramSocket = null;
    public static UdpMessageTool instance;

    // 创建UdpMessageTool对象
    public UdpMessageTool(int port) throws Exception {
        // 初始化DatagramSocket，也可以传入指定端口号
        mDatagramSocket = new DatagramSocket(port);
    }
    // 操作类获取单例实例
    public static UdpMessageTool getInstance(int port) throws Exception {
        instance = new UdpMessageTool(port);
        return instance;
    }
    // 设置超时时间
    public final void setTimeOut(final int timeout) throws Exception {
        mDatagramSocket.setSoTimeout(timeout);
    }
    // 获取DatagramSocket对象
    public final DatagramSocket getDatagramSocket() {
        return mDatagramSocket;
    }
    // 向指定的服务端发送数据信息. 参数介绍： host 服务器主机地址 port 服务端端口 bytes 发送的数据信息
    public final synchronized void send(final String host, final int port, final byte[] bytes) throws IOException {
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(host), port);
        mDatagramSocket.send(dp);
    }
    // 接收从指定的服务端发回的数据. hostName 服务端主机 hostPort 服务端端口 return 服务端发回的数据.
    public final synchronized String receive() {
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
        try {
            mDatagramSocket.receive(dp);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        String result = new String(dp.getData(), 0, dp.getLength());
        return result;
    }
    // 关闭udp连接
    public final void close() {
        if (mDatagramSocket != null) {
            try {
                mDatagramSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mDatagramSocket = null;
        }
    }
}
