package com.joindata.test;

import com.joindata.inf.boot.Bootstrap;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.fastdfs.EnableFastDfs;
import com.joindata.inf.common.support.mybatis.EnableMyBatis;

@JoindataWebApp
@EnableDisconf
@EnableFastDfs
@EnableMyBatis
public class App
{
    public static void main(String[] args)
    {
        Bootstrap.boot();
        // System.out.println(Bootstrap.boot().getBean(FastDfsProperties.class).toString());
    }
}