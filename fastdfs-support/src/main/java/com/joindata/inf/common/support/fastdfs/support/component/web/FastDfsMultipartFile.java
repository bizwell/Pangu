package com.joindata.inf.common.support.fastdfs.support.component.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.joindata.inf.common.support.fastdfs.dependency.client.FileId;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileMetadata;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileMetadata.Builder;
import com.joindata.inf.common.support.fastdfs.support.component.FastDfsClient;
import com.joindata.inf.common.util.log.Logger;

/**
 * FastDFS 的 MultipartFile 实现
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月1日 下午12:50:47
 */
public class FastDfsMultipartFile extends CommonsMultipartFile
{
    private static final Logger log = Logger.get();

    private static final long serialVersionUID = -5839543693444498974L;

    private FastDfsClient client;

    public FastDfsMultipartFile(FileItem fileItem, FastDfsClient client)
    {
        super(fileItem);
        this.client = client;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException
    {
        if(dest == null)
        {
            log.error("上传失败，上传目标为 null");
            throw new FileNotFoundException("保存路径不能为 null");
        }

        if(dest instanceof FastDfsFile)
        {
            log.info("上传文件到服务器, 文件名 : {}, 上传到: {}", super.getOriginalFilename(), dest.getPath());

            Builder metaBuilder = FileMetadata.newBuilder();
            metaBuilder.put("fileName", super.getOriginalFilename());
            ((FastDfsFile)dest).setUploadFuture(client.upload(dest.getPath(), super.getBytes(), metaBuilder.build()));
        }
        else
        {
            log.info("保存文件到本地, 文件名 : {}, 保存到: {}", super.getOriginalFilename(), dest.getPath());

            super.transferTo(dest);
        }
    }

    /**
     * 上传文件
     * 
     * @return 远程文件 ID
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String upload() throws InterruptedException, ExecutionException
    {
        log.info("上传文件到服务器, 文件名 : {}", super.getOriginalFilename());

        Builder metaBuilder = FileMetadata.newBuilder();
        metaBuilder.put("fileName", super.getOriginalFilename());

        CompletableFuture<FileId> fileId = client.upload(this.getOriginalFilename(), this.getBytes(), metaBuilder.build());
        return fileId.get().toString();
    }

}
