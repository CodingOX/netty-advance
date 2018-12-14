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
package io.netty.cases.chapter.demo6;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by 李林峰 on 2018/8/11.
 */
public class ApiGatewayServerHandler extends ChannelInboundHandlerAdapter {

    ExecutorService executorService = Executors.newFixedThreadPool(8);
    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ctx.write(msg);
//        char [] req = new char[64 * 1024];
//        executorService.execute(()->
//        {
//            char [] dispatchReq = req;
//            try
//            {
//                TimeUnit.MICROSECONDS.sleep(100);
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        });
//    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
        char [] req = new char[((ByteBuf)msg).readableBytes()];
        executorService.execute(()->
        {
            char [] dispatchReq = req;
//            try
//            {
//                TimeUnit.MICROSECONDS.sleep(500);
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
