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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 鏉庢灄宄� on 2018/9/2
 */
public class TrafficShappingClientHandler extends ChannelInboundHandlerAdapter {

    private static AtomicInteger SEQ = new AtomicInteger(0);

    static final byte[] ECHO_REQ = new byte[1024 * 1024];

    static final String DELIMITER = "$_";

    static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        scheduledExecutorService.scheduleAtFixedRate(()
                -> {
            ByteBuf buf = null;
            for (int i = 0; i < 10; i++) {
                buf = Unpooled.copiedBuffer(ECHO_REQ, DELIMITER.getBytes());
                SEQ.getAndAdd(buf.readableBytes());
                if (ctx.channel().isWritable())
                    ctx.write(buf);
            }
            ctx.flush();
            int counter = SEQ.getAndSet(0);
            System.out.println("The client send rate is : " + counter + " bytes/s");
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
