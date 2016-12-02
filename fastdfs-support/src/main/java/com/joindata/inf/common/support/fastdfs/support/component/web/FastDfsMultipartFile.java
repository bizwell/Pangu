package com.joindata.inf.common.support.fastdfs.support.component.web;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsClient;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileId;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileMetadata;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileMetadata.Builder;

/**
 * FastDFS 的 MultipartFile 实现
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月1日 下午12:50:47
 */
public class FastDfsMultipartFile extends CommonsMultipartFile
{
    private static final long serialVersionUID = -5839543693444498974L;

    private FastdfsClient client;

    public FastDfsMultipartFile(FileItem fileItem, FastdfsClient client)
    {
        super(fileItem);
        this.client = client;
    }

    /**
     * 将文件保存到远程服务器
     * 
     * @return 远程保存路径（可认为是文件 ID)
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String save() throws InterruptedException, ExecutionException
    {
        Builder metaBuilder = FileMetadata.newBuilder();
        metaBuilder.put("fileName", super.getOriginalFilename());

        CompletableFuture<FileId> fileId = client.upload(super.getOriginalFilename(), super.getBytes(), metaBuilder.build());

        return fileId.get().toString();
    }
}
