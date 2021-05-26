package com.scorpio.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {

    private Integer port;

    public GroupChatServer(Integer port) {
        this.port = port;
    }

    public void run() throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup(8);

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /*加入解码器*/
                            pipeline.addLast("decoder",new StringDecoder());
                            /*加入编码器*/
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new GroupChatServerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            //这么写，客户端无法连接服务器，为什么？
            /*if(channelFuture.isSuccess()){
                System.out.println("netty 服务器启动成功！");
            }*/

            System.out.println("netty 服务器启动成功！");
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        GroupChatServer groupChatServer = new GroupChatServer(6667);
        groupChatServer.run();
    }




}
