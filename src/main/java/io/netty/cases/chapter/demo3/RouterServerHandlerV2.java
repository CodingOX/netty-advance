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
package io.netty.cases.chapter.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 鏉庢灄宄� on 2018/8/5.
 */
public class RouterServerHandlerV2 extends SimpleChannelInboundHandler<ByteBuf> {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();
    PooledByteBufAllocator allocator;
    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        byte [] body = new byte[msg.readableBytes()];
        executorService.execute(()->
        {
            if(allocator == null)
                allocator = new PooledByteBufAllocator(false);
            //瑙ｆ瀽璇锋眰娑堟伅锛屽仛璺敱杞彂锛屼唬鐮佺渷鐣�...
            //杞彂鎴愬姛锛岃繑鍥炲搷搴旂粰瀹㈡埛绔�
            ByteBuf respMsg = allocator.heapBuffer(body.length);
            respMsg.writeBytes(body);//浣滀负绀轰緥锛岀畝鍖栧鐞嗭紝灏嗚姹傝繑鍥�
            ctx.writeAndFlush(respMsg);
        });
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
