package com.scorpio.nio;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接
        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("客户端连接需要时间，所以可以做一些其他事情！");
            }
        }
        //连接完成
        String content = "hello guys!";
        ByteBuffer wrap = ByteBuffer.wrap(content.getBytes());
        socketChannel.write(wrap);
        System.in.read();
    }
}
