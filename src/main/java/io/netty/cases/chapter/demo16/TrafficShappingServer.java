/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.cases.chapter.demo16;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

/**
 * Created by 鏉庢灄宄� on 2018/9/2
 */
public class TrafficShappingServer {
    public static void main(String[] args) throws Exception {
        int port = 18091;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 閲囩敤榛樿鍊�
            }
        }
        new TrafficShappingServer().bind(port);
    }

    public void bind(int port) throws Exception {
        // 閰嶇疆鏈嶅姟绔殑NIO绾跨▼缁�
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast("Channel Traffic Shaping", new ChannelTrafficShapingHandler(1024 * 1024, 1024 * 1024, 1000));
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_"
                                    .getBytes());
                            ch.pipeline().addLast(
                                    new DelimiterBasedFrameDecoder(2048 * 1024,
                                            delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new TrafficShapingServerHandler());
                        }
                    });
            // 缁戝畾绔彛锛屽悓姝ョ瓑寰呮垚鍔�
            ChannelFuture f = b.bind(port).sync();
            // 绛夊緟鏈嶅姟绔洃鍚鍙ｅ叧闂�
            f.channel().closeFuture().sync();
        } finally {
            // 浼橀泤閫�鍑猴紝閲婃斁绾跨▼姹犺祫婧�
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
