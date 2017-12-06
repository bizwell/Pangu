package com.joindata.inf.zipkin.util;

public class Times {

    public static long currentMicros(){
        return System.currentTimeMillis() * 1000;
    }
}
