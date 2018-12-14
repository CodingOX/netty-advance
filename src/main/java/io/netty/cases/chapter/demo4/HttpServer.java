/**
 * 
 */
package io.netty.cases.chapter.demo4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Created by 李林峰 on 2018/8/11.
 */
public class HttpServer {

	private void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(new HttpServerCodec());
                                    ch.pipeline().addLast(new HttpObjectAggregator(Short.MAX_VALUE));
                                    ch.pipeline().addLast(new HttpServerHandler());
                                }
                            }).option(ChannelOption.SO_BACKLOG, 128);
            ChannelFuture f = b.bind("127.0.0.1",port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer();
    	int port = 18084;
    	System.out.println("HTTP server listening on " + port);
        server.bind(port);
    }
}
