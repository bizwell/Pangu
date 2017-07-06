package com.joindata.inf.common.support.fastdfs.support.component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletResponse;

import com.joindata.inf.common.support.fastdfs.dependency.client.AbstractFastDfsClient;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileMetadata;
import com.joindata.inf.common.util.basic.FileUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

public class FastDfsClient extends AbstractFastDfsClient
{
    private static final Logger log = Logger.get();

    public FastDfsClient(Builder builder)
    {
        super(builder);
    }

    /**
     * 上传文件到默认组
     * 
     * @param content 内容
     * @param srcName 原名
     * @param savePath 逻辑远程路径
     * @throws ExecutionException
     * @throws InterruptedException
     * @return 文件 ID（包括组名）
     */
    public String upload(byte[] content, String srcName, String savePath) throws InterruptedException, ExecutionException
    {
        log.debug("上传文件到服务器, 源文件名: {}, 上传到: {}", srcName, savePath);

        String ext = FileUtil.getExtension(savePath);
        if(StringUtil.isNullOrEmpty(ext))
        {
            ext = srcName;
            if(StringUtil.isNullOrEmpty(ext))
            {
                ext = "tmp";
            }
        }

        String tmpFileName = "tmp." + ext;

        com.joindata.inf.common.support.fastdfs.dependency.client.FileMetadata.Builder metaBuilder = FileMetadata.newBuilder();
        metaBuilder.put("fileName", srcName);
        metaBuilder.put("filePath", savePath);

        return super.upload(tmpFileName, content, metaBuilder.build()).get().toString();
    }

    /**
     * 上传文件到指定组
     * 
     * @param group 组名
     * @param content 内容
     * @param srcName 原名
     * @param savePath 逻辑远程路径
     * @throws ExecutionException
     * @throws InterruptedException
     * @return 文件 ID（包括组名）
     */
    public String upload(String group, byte[] content, String srcName, String savePath) throws InterruptedException, ExecutionException
    {
        log.debug("上传文件到服务器, 源文件名: {}, 上传到: {}", srcName, savePath);

        String ext = FileUtil.getExtension(savePath);
        if(StringUtil.isNullOrEmpty(ext))
        {
            ext = srcName;
            if(StringUtil.isNullOrEmpty(ext))
            {
                ext = "tmp";
            }
        }

        String tmpFileName = "tmp." + ext;

        com.joindata.inf.common.support.fastdfs.dependency.client.FileMetadata.Builder metaBuilder = FileMetadata.newBuilder();
        metaBuilder.put("fileName", srcName);
        metaBuilder.put("filePath", savePath);

        return super.upload(group, tmpFileName, content, metaBuilder.build()).get().toString();
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
