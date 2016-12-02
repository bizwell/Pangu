package com.joindata.inf.common.support.fastdfs.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsClient;
import com.joindata.inf.common.support.fastdfs.support.FastDfsClientHolder;

/**
 * 配置 FastDFS
 * 
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午5:29:31
 */
@Configuration
public class FastDfsConfig
{
    @Autowired
    private FastDfsClientHolder holder;

    @Bean
    public FastdfsClient buildClient()
    {
        return holder.getClient();
    }
}