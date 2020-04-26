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
package io.netty.cases.chapter.demo15;

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
public class EventTriggerClientHandler extends ChannelInboundHandlerAdapter {

    static final String ECHO_REQ = "Hi,welcome to Netty ";
    static final String DELIMITER = "$_";
    static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        scheduledExecutorService.scheduleAtFixedRate(()
                -> {
            int counter = SEQ.incrementAndGet();
            if (counter % 10 == 0) {
                ctx.writeAndFlush(Unpooled.copiedBuffer((ECHO_REQ + DELIMITER).getBytes()));
            } else
                ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
