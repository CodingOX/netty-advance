package io.netty.cases.chapter.demo0;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 描述
 *
 * @author Liu Chunfu
 * @date 2018-12-26 10:08
 **/
public class NioStudy2 {

    public static void main(String[] args) throws IOException {

        //客户端的selector
        Selector readableSelector = Selector.open();
        //注册一个 selector
        Selector acceptSelector = Selector.open();

        Thread serverAcceptThread = new Thread(() -> {
            try {
                //1.连接和注册
                ServerSocketChannel serverChannel = ServerSocketChannel.open();
                //设置为非阻塞
                serverChannel.configureBlocking(false);
                //绑定该服务端端口 23111
                serverChannel.socket().bind(new InetSocketAddress(23111));
                //接受的是 accept 类型.此处直接写会报错！
                serverChannel.register(acceptSelector, SelectionKey.OP_READ);

                //2.处理 accept 的 selector，此部分代码同后续的读取
                while (true) {
                    try {
                        int select = readableSelector.select(1);
                        //没有本次循环就退出
                        if (select == 0) {
                            continue;
                        }
                        Set<SelectionKey> selectionKeys = readableSelector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            try {
                                //可以读
                                if (key.isReadable()) {
                                    SocketChannel clientChannel = (SocketChannel) key.channel();
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    clientChannel.read(byteBuffer);
                                    byteBuffer.flip();
                                    String msg = Charset.defaultCharset().newDecoder().decode(byteBuffer).toString();
                                    System.out.println(msg);
                                }
                            } finally {
                                iterator.remove();
                                //什么意思？
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverAcceptThread.start();
    }

}
