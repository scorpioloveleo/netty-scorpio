package com.scorpio.nio;

import java.util.HashMap;

public class Test {

    public static void main(String[] args) {
        int a = 1;
        int b = ++a+a++;
        System.out.println(b);
        int c =a++;
        System.out.println(c);

    }
}
