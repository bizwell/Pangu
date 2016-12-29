package com.joindata.inf.common.support.fastdfs.support.component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletResponse;

import com.joindata.inf.common.support.fastdfs.dependency.client.AbstractFastDfsClient;

public class FastDfsClient extends AbstractFastDfsClient
{
    public FastDfsClient(Builder builder)
    {
        super(builder);
    }

    /**
     * 下载文件
     * 
     * @param fileId 文件 ID
     * @param out 输出流
     */
    public void download(String fileId, OutputStream out) throws InterruptedException, ExecutionException
    {
        super.download(fileId, out).get();
    }

    /**
     * 下载文件
     * 
     * @param fileId 文件 ID
     * @param response Servlet 的 HTTP Response
     */
    public void download(String fileId, ServletResponse response) throws InterruptedException, ExecutionException, IOException
    {
        download(fileId, response.getOutputStream());
    }
}
