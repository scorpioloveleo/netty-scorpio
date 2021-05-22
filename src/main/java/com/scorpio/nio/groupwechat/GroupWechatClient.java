package com.scorpio.nio.groupwechat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

public class GroupWechatClient {
    private Selector selector;
    private SocketChannel socketChannel;
    private static final Integer port = 6667;
    private static final String ipAddress = "127.0.0.1";
    private InetSocketAddress inetSocketAddress;
    private String username;

    public GroupWechatClient() throws IOException {
        inetSocketAddress  = new InetSocketAddress(ipAddress, port);
        selector = Selector.open();
        socketChannel = SocketChannel.open(inetSocketAddress);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
       // socketChannel.connect(inetSocketAddress);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "is ok...");
    }

    //发送消息
    public void sendInfo(String msg){
        msg = username +"  说:  "+msg;
        /*int select = selector.select();
        if(select > 0){
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                SocketChannel channel = (SocketChannel)key.channel();
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                channel.write(wrap);
            }
        }else {
            System.out.println("");
        }*/
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //接收消息
    public void readInfo(){
        int select = 0;
        try {
            select = selector.select();
            if(select > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer msg = ByteBuffer.allocate(1024);
                        channel.read(msg);
                        System.out.println(new String(msg.array()).trim());
                    }
                    iterator.remove();
                }
            }else {
                //System.out.println("没有可用的通道！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        GroupWechatClient client = new GroupWechatClient();

        //发送消息
        new Thread() {
            @Override
            public void run() {
                while (true){
                    client.readInfo();
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String msg = scanner.nextLine();
            System.out.println("输入了:"+msg);
            client.sendInfo(msg);
        }

    }
}
