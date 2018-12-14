/**
 * 
 */
package io.netty.cases.chapter.demo4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by 李林峰 on 2018/8/11.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			FullHttpRequest request) throws Exception {
		 if (!request.decoderResult().isSuccess()) {
	            sendError(ctx, BAD_REQUEST);
	            return;
	        }
		 System.out.println("Http Server receive the request : " + request);
		 ByteBuf body = request.content().copy();
		 FullHttpResponse response = new DefaultFullHttpResponse(
	                HTTP_1_1, HttpResponseStatus.OK, body);
		 response.headers().set(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());
		 ctx.writeAndFlush(response).sync();
		 System.out.println("Http Server send response succeed : " + response);		 
	}
	
	 @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	        cause.printStackTrace();
	        ctx.close();
	    }
	
	 private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
	        FullHttpResponse response = new DefaultFullHttpResponse(
	                HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
	        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
	        System.out.println(response);
	        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	    }
}
