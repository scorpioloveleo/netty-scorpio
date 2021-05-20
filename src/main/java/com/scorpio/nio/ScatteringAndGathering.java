package com.scorpio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGathering {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel open = ServerSocketChannel.open();
        open.bind(new InetSocketAddress(6666));

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel accept = open.accept();
        int length = 8;
        while (true){
            int readLength = 0;
            while (readLength < length){
                long read = accept.read(byteBuffers);
                readLength+=read;
                System.out.println("readLength: "+ read);
                Arrays.asList(byteBuffers).stream().map(buffer -> "position: "+ buffer.position() + ", limit: "+ buffer.limit())
                        .forEach(System.out::println);
            }

            Arrays.asList(byteBuffers).stream().forEach(e -> e.flip());


            int writeLength = 0;
            while (writeLength < length){
                long write = accept.write(byteBuffers);
                writeLength+=write;
                System.out.println("writeLength: "+ writeLength);
            }
            Arrays.asList(byteBuffers).stream().forEach(e -> e.clear());
        }

    }
}
