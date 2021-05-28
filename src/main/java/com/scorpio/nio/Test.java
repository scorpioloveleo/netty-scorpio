package com.scorpio.nio;

import io.netty.util.NettyRuntime;

import java.util.Arrays;
import java.util.HashMap;

public class Test {

    public static void main(String[] args) {
        System.out.println(minDistance("intention","execution"));

    }


    public static int minDistance(String word1, String word2) {

        //状态转移方程
        // dp[x][y] = dp[x-1][y-1]   dp[x-1][y]+1  dp[x][y-1]+1
        int x = word1.length();
        int y = word2.length();
        int[][] dp = new int[x+1][y+1];

        char[] char1 = word1.toCharArray();
        char[] char2 = word2.toCharArray();


        for (int i = 0; i <= x; i++) {
            dp[i][0] = i;
        }
        for (int i = 1; i <= y; i++) {
            dp[0][i] = i;
        }

        for (int i = 1; i <= x; i++) {
            for (int j = 1; j <= y; j++) {
                //四种情况
                if(char1[i-1] != char2[j-1]){
                    //1.替换 2.删除 3.增加
                    dp[i][j] = Math.min(dp[i-1][j-1]+1, Math.min(dp[i][j-1]+1, dp[i-1][j]+1));
                }else {
                    //4.跳过
                    dp[i][j] = dp[i-1][j-1];
                }
                System.out.println(i+" "+j+" "+dp[i][j]);
            }
        }

        System.out.println(Arrays.deepToString(dp));

        return dp[x][y];
    }
}
