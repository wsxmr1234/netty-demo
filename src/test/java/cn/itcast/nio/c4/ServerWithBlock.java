package cn.itcast.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static cn.itcast.nio.c2.ByteBufferUtil.debugRead;

/**
 * 尝试阻塞模式下进行单线程网络编程-服务端
 */
@Slf4j
public class ServerWithBlock {
    public static void main(String[] args) throws IOException {
        // 创建一个ByteBuffer，用以接收客户端传来的信息
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 创建一个服务器
        ServerSocketChannel server = ServerSocketChannel.open();
        // 绑定监听端口
        server.bind(new InetSocketAddress(8080));
        // 连接集合
        List<SocketChannel> channels = new ArrayList<>();

        while (true) {
            log.debug("等待连接中。。。");
            // 建立连接
            SocketChannel sc = server.accept();
            log.debug("和 {} 建立了连接", sc);
            // 将连接放入连接集合
            channels.add(sc);
            // 接收各个连接的客户端发送的数据
            for (SocketChannel channel : channels) {
                log.debug("等待读取 {} 的数据", channel);
                channel.read(buffer);
                buffer.flip(); // 将ByteBuffer置为读模式
                debugRead(buffer);
                buffer.clear(); // 将ByteBuffer置为写模式
                log.debug("接收来自 {} 的数据", channel);
            }

        }
    }
}
