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
package io.netty.cases.chapter.demo13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.SingleThreadEventExecutor;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 李林峰 on 2018/8/19.
 */
public class ServiceTraceServerHandlerV2 extends ChannelInboundHandlerAdapter {
    AtomicInteger totalSendBytes = new AtomicInteger(0);
    static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    static ScheduledExecutorService kpiExecutorService = Executors.newSingleThreadScheduledExecutor();
    static ScheduledExecutorService writeQueKpiExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduledExecutorService.scheduleAtFixedRate(()->
        {
            int qps = totalSendBytes.getAndSet(0);
            System.out.println("The server write rate is : " + qps + " bytes/s");
        },0,1000, TimeUnit.MILLISECONDS);
        kpiExecutorService.scheduleAtFixedRate(()->
        {
            Iterator<EventExecutor> executorGroups = ctx.executor().parent().iterator();
            while (executorGroups.hasNext())
            {
                SingleThreadEventExecutor executor = (SingleThreadEventExecutor)executorGroups.next();
                int size = executor.pendingTasks();
                if (executor == ctx.executor())
                    System.out.println(ctx.channel() + "--> " + executor + " pending size in queue is : --> " + size);
                else
                    System.out.println(executor + " pending size in queue is : --> " + size);
            }
        },0,1000, TimeUnit.MILLISECONDS);
        writeQueKpiExecutorService.scheduleAtFixedRate(()->
        {
            long pendingSize = ((NioSocketChannel)ctx.channel()).unsafe().outboundBuffer().totalPendingWriteBytes();
            System.out.println(ctx.channel() + "--> " + " ChannelOutboundBuffer's totalPendingWriteBytes is : "
                    + pendingSize + " bytes");
        },0,1000, TimeUnit.MILLISECONDS);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        int sendBytes = ((ByteBuf)msg).readableBytes();
        ChannelFuture writeFuture = ctx.write(msg);
        writeFuture.addListener((f) ->
        {
            totalSendBytes.getAndAdd(sendBytes);
        });
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
