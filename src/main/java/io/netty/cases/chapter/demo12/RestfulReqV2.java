package io.netty.cases.chapter.demo12;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Arrays;

/**
 * Created by 鏉庢灄宄� on 2018/8/26.
 */
public class RestfulReqV2 {

    private HttpHeaders header;

    private HttpMethod method;

    private HttpVersion version;

    private byte [] body;

    public RestfulReqV2(byte [] body)
    {
        this.body = body;
    }


    public byte [] body()
    {
        return this.body;
    }

    public byte [] bodyCopy() {
        if (this.body != null)
            return Arrays.copyOf(this.body, this.body.length);
        return null;
    }
}
