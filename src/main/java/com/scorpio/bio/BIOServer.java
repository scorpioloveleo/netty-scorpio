package com.scorpio.bio;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    private static  final Logger log = LoggerFactory.getLogger(BIOServer.class);

    /**
     * 客户端连接:
     *  telnet 127.0.0.1 6666
     *  然后按 ctrl+] ，输入 send +发送内容即可
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务端已启动...."+Thread.currentThread().getName());
        while (true){
            //无连接，等待连接,就会阻塞
            final Socket accept = serverSocket.accept();
            System.out.println("连接到一个客户端...."+Thread.currentThread().getName());
            executorService.execute(new Runnable() {
                public void run() {
                    handle(accept);
                }
            });

        }

    }

    public static void handle(Socket socket) {
        byte[] bytes = new byte[1024];

        int read = 0;
        try {
            InputStream inputStream = socket.getInputStream();
            while (true){
                //todo 疑问:这里读取完数据，直接就结束，不会走下一步是否为-1的判断，很奇怪
                //解析：这个地方也会阻塞，无消息，等待接收消息，也会阻塞，同上
                read = inputStream.read(bytes);
                System.out.println("read:"+read);
                if(read!=-1){
                    System.out.println(new String(bytes, 0, read) +Thread.currentThread().getName());
                }else {
                    System.out.println("退出。。。。");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("发生异常...");
            e.printStackTrace();
        }finally {
            System.out.println("关闭连接...");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
