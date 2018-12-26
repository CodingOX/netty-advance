package io.netty.cases.chapter.demo0;

import io.netty.channel.ChannelOption;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOptions;
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
public class NioStudyRepeat {

    public static void main(String[] args) throws IOException {

        Selector acceptSelector = Selector.open();
        Selector readableSelector = Selector.open();

        Thread acceptThread = new Thread(() -> {
            try {
                SocketChannel serverSocketChannel = SocketChannel.open();
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.socket().bind(new InetSocketAddress(23118));
                serverSocketChannel.register(acceptSelector,SelectionKey.OP_ACCEPT);

                while (true){
                    if (acceptSelector.select(1)==0) {
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = acceptSelector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        try {
                            //将可读的内容注册到 readableSelector 上
                            SocketChannel acceptChannel = (SocketChannel) iterator.next().channel();
                            acceptChannel.configureBlocking(false);
                            acceptChannel.register(readableSelector,SelectionKey.OP_READ);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            iterator.remove();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }

}
