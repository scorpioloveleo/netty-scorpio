package com.scorpio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws IOException {

        //服务器通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //多路复用选择器
        Selector selector = Selector.open();

        //绑定SelectKey
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //将该ServerSocketChannel注册到Selector，并设置事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while (true){

            if(selector.select(1000) == 0){
                System.out.println("服务器等待了1s，没有事件发生");
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();

                if(key.isAcceptable()){
                    //该客户端生成一个SocketChannel
                    SocketChannel accept = serverSocketChannel.accept();
                    accept.configureBlocking(false);
                    System.out.println("生成了一个SocketChannel: "+ accept.hashCode());
                    //注册
                    accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    //注册数量
                    System.out.println("注册数量: " + selector.keys().size());
                }

                if(key.isReadable()){
                    //通过Key获取相应的channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //通过channel获取相应的
                    ByteBuffer attachment = (ByteBuffer)key.attachment();
                    channel.read(attachment);
                    System.out.println("接收到的内容:"+new String(attachment.array()));
                }

                //移除当前key防止重复操作
                iterator.remove();

            }



        }



    }


}
