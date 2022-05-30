package cn.itcast.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

import static cn.itcast.nio.c2.ByteBufferUtil.debugAll;

@Slf4j
public class Server_xmr {
    public static void main(String[] args) throws IOException {
        // 1.创建selector
        Selector selector = Selector.open();
        // 2.创建SocketServerChannel,监听端口,设置为非阻塞
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        // 3. 将ssc注册到register,并关联关注事件类型
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select(); // 阻塞方法
            // 4.获取所有当前的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            // 5.遍历处理事件
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove(); // 删除key
                if (selectionKey.isAcceptable()) { // 如果是accept事件
                    // 获取socketChannel
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    // 注册register,并为每个线程添加各自的bytebuffer，来处理边界问题
                    ByteBuffer byteBuffer = ByteBuffer.allocate(16);
                    SelectionKey scKey = sc.register(selector, SelectionKey.OP_READ, byteBuffer);
                } else if (selectionKey.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        int read = channel.read(buffer);
                        if (read == -1) {
                            selectionKey.cancel();
                        } else {
                            split(buffer);
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                selectionKey.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        selectionKey.cancel();
                    }


                }

            }

        }
    }

    private static void split(ByteBuffer buffer) {
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            int length = i - buffer.position() + 1;
            ByteBuffer byteBuffer = ByteBuffer.allocate(length);
            if (buffer.get(i) == '\n') {
                for (int i1 = 0; i1 < i; i1++) {
                    byteBuffer.put(buffer.get(i));
                }
                debugAll(byteBuffer);
            }
        }
        buffer.compact();
    }

}
