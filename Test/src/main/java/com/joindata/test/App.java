package com.joindata.test;

import com.joindata.inf.boot.Bootstrap;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.fastdfs.EnableFastDfs;
import com.joindata.inf.common.support.mybatis.EnableMyBatis;
import com.joindata.inf.common.support.swagger.EnableSwagger;

@JoindataWebApp(8089)
@EnableDisconf
@EnableFastDfs
@EnableMyBatis
@EnableSwagger
public class App
{
    public static void main(String[] args)
    {
        Bootstrap.boot();
    }
}