package io.netty.cases.chapter.demo1;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by 李林峰 on 2018/8/3.
 */
public class SignalHandlerTest {

    public static void main(String [] args) throws Exception
    {
        Signal sig = new Signal("INT");//以windows操作系统为例
        Signal.handle(sig, (s)->{
            System.out.println("Signal handle start...");
            try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(()->
        {
            System.out.println("ShutdownHook execute start...");
            System.out.println("Netty NioEventLoopGroup shutdownGracefully...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ShutdownHook execute end...");
        },""));
        new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.DAYS.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();}}
        }, "Daemon-T").start();
    }
}
