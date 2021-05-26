package com.scorpio.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //发送给其他人上线信息
        channelGroup.writeAndFlush(sdf.format(new Date()) +" :[客户端] "+ctx.channel().remoteAddress()+ "加入聊天室！");
        channelGroup.add(ctx.channel());
    }

    /*断开连接,将xx客户端离开的消息推送给当前在线的客户端*/
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(sdf.format(new Date())+":"+"[客户端]"+channel.remoteAddress()+"离开了！\n");
        System.out.println("channelGroup size"+channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("[客户端]:"+ctx.channel().remoteAddress()+" 说: "+msg);
        Channel self = ctx.channel();
        channelGroup.forEach(ch -> {
            if(ch == self){
                ch.writeAndFlush(sdf.format(new Date())+":"+"[自己] : 说 "+msg);
            }else {
                ch.writeAndFlush(sdf.format(new Date())+":"+"[客户端] : 说 "+msg);
            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(sdf.format(new Date())+":"+"客户端:"+ctx.channel().remoteAddress()+" 上线了！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(sdf.format(new Date())+":"+"客户端:"+ctx.channel().remoteAddress()+" 下线了！");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /*发生异常，关闭通道*/
        ctx.close();
    }
}
