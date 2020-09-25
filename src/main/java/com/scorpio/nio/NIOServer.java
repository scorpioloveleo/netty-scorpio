package com.scorpio.nio;

import java.nio.IntBuffer;

public class NIOServer {

    /**
     * buffer的基本使用
     */
    public /*static*/ void main1(String[] args) {
        IntBuffer allocate = IntBuffer.allocate(5);
        for (int i = 0; i < allocate.capacity(); i++) {
            allocate.put(i*2);
        }

        //读写切换,否则无法读取
        allocate.flip();

        while (allocate.hasRemaining()){
            System.out.println(allocate.get());
        }
    }

    /**
     *
     */
}
