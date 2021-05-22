package com.scorpio.nio.groupwechat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupWechatServer {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final Integer port = 6667;
    private InetSocketAddress inetSocketAddress;

    public GroupWechatServer() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            inetSocketAddress = new InetSocketAddress(port);
            serverSocketChannel.bind(inetSocketAddress);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("初始化失败！");
        }

    }

   private void server() throws IOException {
        while (true){
            if(selector.select(2000) == 0){
                System.out.println("服务器等待中....");
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //判断事件
                if(key.isAcceptable()){
                    //创建通道
                    SocketChannel channel = serverSocketChannel.accept();
                    //设置为非阻塞
                    channel.configureBlocking(false);
                    //注册
                    channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println(channel.getRemoteAddress() + "上线了！");
                }

                if(key.isReadable()){
                    //读取消息
                    readData(key);
                }

                iterator.remove();
            }
        }
   }
   //读取消息
    private void readData(SelectionKey key){
        SocketChannel channel = (SocketChannel)key.channel();
        ByteBuffer attachment = (ByteBuffer)key.attachment();
        try {
            int read = channel.read(attachment);
            if(read>0){
                String msg = new String(attachment.array());
                System.out.println("from 客户端: "+ msg);
                //转发
                sendMsgToOthers(msg, channel);
                //要清除之前的文本，不然之前的文本都在
                attachment.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println(channel.getRemoteAddress()+" 离线了！");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();

            } catch (IOException ex) {

            }
        }

    }

   //转发
    private void sendMsgToOthers(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发中...");
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey selectionKey:keys) {
            Channel channel = selectionKey.channel();
            if(channel instanceof SocketChannel && channel!=self){
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                ((SocketChannel) channel).write(wrap);
            }
        }


    }

    public static void main(String[] args) throws IOException {
        GroupWechatServer groupWechatServer = new GroupWechatServer();
        groupWechatServer.server();
    }
}
