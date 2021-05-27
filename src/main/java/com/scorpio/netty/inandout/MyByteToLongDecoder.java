package com.scorpio.netty.inandout;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("解码器被调用...");
        if(byteBuf.isReadable()){
            if(byteBuf.readableBytes()>=8){
                byteBuf.readLong();
            }
        }
    }
}
