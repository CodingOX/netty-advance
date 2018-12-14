package io.netty.cases.chapter.demo12;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;

import java.util.concurrent.TimeUnit;

/**
 * Created by 李林峰 on 2018/8/27.
 */
public class MockEdgeService {

    public static void main(String [] args) throws Exception
    {
//        testHotMethod();
        testCopyHotMethod();
//        testReferenceHotMethod();
    }

    static void testHotMethod() throws Exception
    {
        ByteBuf buf = Unpooled.buffer(1024);
        for(int i = 0; i < 1024; i++)
            buf.writeByte(i);
        RestfulReq req = new RestfulReq(buf.array());
        while (true)
        {
            byte [] msgReq = req.body();
            TimeUnit.MICROSECONDS.sleep(1);
        }
    }

    static void testCopyHotMethod() throws Exception
    {
        ByteBuf buf = Unpooled.buffer(1024);
        for(int i = 0; i < 1024; i++)
            buf.writeByte(i);
        RestfulReq req = new RestfulReq(buf.array());
        while (true)
        {
            byte [] msgReq = req.body();
        }
    }

    static void testReferenceHotMethod() throws Exception
    {
        ByteBuf buf = Unpooled.buffer(1024);
        for(int i = 0; i < 1024; i++)
            buf.writeByte(i);
        RestfulReqV2 req = new RestfulReqV2(buf.array());
        while (true)
        {
            byte [] msgReq = req.body();
        }
    }
}
