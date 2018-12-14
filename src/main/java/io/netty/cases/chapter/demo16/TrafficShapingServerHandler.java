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
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014骞�2鏈�14鏃�
 */
@Sharable
public class TrafficShapingServerHandler extends ChannelInboundHandlerAdapter {

    AtomicInteger counter = new AtomicInteger(0);

    static ScheduledExecutorService es = Executors.newScheduledThreadPool(1);

    public TrafficShapingServerHandler() {
        es.scheduleAtFixedRate(() ->
        {
            System.out.println("The server receive client rate is : " + counter.getAndSet(0) + " bytes/s");
        }, 0, 1000, TimeUnit.MILLISECONDS);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        String body = (String) msg;
        counter.addAndGet(body.getBytes().length);
        body += "$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 鍙戠敓寮傚父锛屽叧闂摼璺�
    }
}
