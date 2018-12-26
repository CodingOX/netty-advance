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
public class NioStudy {

    public static void main(String[] args) throws IOException {

        //接收 接下来是 connect，最后才是 readable
        Selector acceptSelector = Selector.open();
        //可读的
        Selector readableSelector = Selector.open();
        Thread serverAcceptThread = new Thread(() -> {
            try {
                //1.连接和注册
                ServerSocketChannel serverChannel = ServerSocketChannel.open();
                //设置为非阻塞
                serverChannel.configureBlocking(false);
                //绑定该服务端端口 23111
                serverChannel.socket().bind(new InetSocketAddress(23111));
                //接受的是 accept 类型
                serverChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
                //2.处理 accept 的 selector，此部分代码同后续的读取
                while (true) {
                    int select = acceptSelector.select(1);
                    if (select == 0) {
                        continue;
                    }
                    //此处肯定有 select 的内容
                    Set<SelectionKey> selectionKeys = acceptSelector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        //此处为什么还要再判定一次？
                        // 因为处理的是 Acceptable 的类型，所以此处判定是 是否可以接收。
                        if (key.isAcceptable()) {
                            try {
                                SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                clientChannel.configureBlocking(false);
                                clientChannel.register(readableSelector, SelectionKey.OP_READ);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                //删除
                                iterator.remove();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread serverReadThread = new Thread(() -> {
            //只需要读取注册器的内容即可
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
                                //byteBuffer 因为只有2个指针所以每次写后若需要读，那么则需要 flip一下！
                                //这个也是 netty 的 bytebuf 的改进。
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
        });
        serverAcceptThread.start();
        serverReadThread.start();
    }

}
