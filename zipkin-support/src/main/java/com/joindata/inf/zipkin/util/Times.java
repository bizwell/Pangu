package com.joindata.inf.zipkin.util;


/**
 * Author: haolin
 * Email:  haolin.h0@gmail.com
 */
public class Times {

    public static long currentMicros(){
        return System.currentTimeMillis() * 1000;
    }
}
