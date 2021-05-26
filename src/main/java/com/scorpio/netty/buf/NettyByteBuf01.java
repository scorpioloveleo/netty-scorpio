package com.scorpio.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf01 {
    public static void main(String[] args) {

        ByteBuf byteBuf = Unpooled.copiedBuffer("hello.world!", CharsetUtil.UTF_8);

        //使用相关方法

        if(byteBuf.hasArray()){
            byte[] content = byteBuf.array();
            System.out.println(new String(content, CharsetUtil.UTF_8));
            System.out.println("byteBuf = " + byteBuf);

            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

            System.out.println(byteBuf.readableBytes());
        }


    }
}
