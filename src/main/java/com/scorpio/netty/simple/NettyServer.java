package com.scorpio.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //创建服务器启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        //设置
        bootstrap.group(bossGroup, workGroup) //设置2个线程组
                 .channel(NioServerSocketChannel.class) //设置服务器通道
                 .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列的连接个数
                 .childOption(ChannelOption.SO_KEEPALIVE, true) //设置连接活动状态
                 .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象（匿名对象）
                     //给pipeline 设置处理器
                     @Override
                     protected  void  initChannel(SocketChannel ch){
                         ch.pipeline().addLast(null);
                     }
                 });
        System.out.println("服务器已启动");
        //绑定一个端口并且同步， 生成一个ChannelFuture 对象
        //启动服务器（并绑定端口）
        ChannelFuture cf = bootstrap.bind(6668).sync();

        //对关闭通道进行监听
        cf.channel().closeFuture().sync();

}
}
