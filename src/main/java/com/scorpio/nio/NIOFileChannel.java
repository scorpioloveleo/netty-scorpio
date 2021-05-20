package com.scorpio.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel {

    public static void main(String[] args) throws Exception {
        //写
        //write();

        //读
        //read();

        //读写
        //readAndWrite();

        //图片得拷贝
        //copyPicture();


        //文件堆外内存修改
        heapModify();
    }

    public static void write() throws Exception{
        String str = "你们好，我是五天";
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\hello.txt");
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        allocate.put(str.getBytes());
        allocate.flip();
        channel.write(allocate);
        fileOutputStream.close();
    }

    public static void read() throws Exception{
        File file = new File("d:\\hello.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer allocate = ByteBuffer.allocate((int)file.length());
        channel.read(allocate);
        System.out.println(new String(allocate.array()));
        fileInputStream.close();
    }

    public static void readAndWrite() throws Exception{
        FileInputStream fileInputStream = new FileInputStream(new File("d:\\hello.txt"));
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\hello_copy.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer allocate = ByteBuffer.allocate(512);

        while (true){
            //为什么这里不用转换flip(), 看源码对比flip（）就知道了
            allocate.clear();
            int read = inputStreamChannel.read(allocate);
            if(read == -1){
                break;
            }
            allocate.flip();
            outputStreamChannel.write(allocate);
        }
        fileOutputStream.close();
        fileInputStream.close();
    }


    public static void copyPicture() throws Exception{
        FileInputStream fileInputStream = new FileInputStream(new File("d:\\11.jpg"));
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\22.jpg");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        inputStreamChannel.transferTo(0, inputStreamChannel.size(), outputStreamChannel);

        fileOutputStream.close();
        fileInputStream.close();
    }

    public static void heapModify() throws Exception{
        /**
         * 远古类，传统的File只能从头读写，该文件获取的channel,可以指定位置开始操作
         */
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File("d:\\hello.txt"), "rw");
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 指定channele的mode（类型）为读写，获取文件映射堆的操作bufferl
         * 5是映射到内存的大小（0-4）
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) 'K');

        randomAccessFile.close();

    }


}
