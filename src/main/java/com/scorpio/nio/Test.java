package com.scorpio.nio;

import io.netty.util.NettyRuntime;

import java.util.HashMap;

public class Test {

    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors());

    }


    public static int minDistance(String word1, String word2) {

        //状态转移方程
        // dp[x][y] = dp[x-1][y-1]   dp[x-1][y]+1  dp[x][y-1]+1
        char[] word11 = word1.toCharArray();
        char[] word22 = word2.toCharArray();
        int x = word11.length;
        int y = word22.length;
        int[][] dp = new int[x+1][y+1];


        for (int i = 1; i < x; i++) {
            for (int j = 0; j < y; j++) {
                //四种情况
                if(word11[i] != word22[j]){
                    //1.替换
                    dp[i][j] =dp[i-1][j-1]+1;
                    //2.删除

                    //3.增加



                    dp[i][j];
                }else {
                    //4.跳过
                    dp[i][j++] = dp[i-1][j-1];
                }
            }
        }


    }
}
