package com.scorpio.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

public class GroupChatClient {

    private String localhost;
    private Integer port;

    public GroupChatClient(String localhost, Integer port) {
        this.localhost = localhost;
        this.port = port;
    }

    public void run() throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /*加入解码器*/
                            pipeline.addLast("decoder",new StringDecoder());
                            /*加入编码器*/
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(localhost, port).sync();

            Channel channel = channelFuture.channel();
            System.out.println("----------"+channel.localAddress()+"------------");
            /*客户端需要输入消息，所以，要创建一个扫描器*/
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                /*通过channel发送到服务器*/
                channel.writeAndFlush(msg+"\r\n");

            }
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        GroupChatClient client = new GroupChatClient("127.0.0.1", 6667);
        client.run();
    }
}
