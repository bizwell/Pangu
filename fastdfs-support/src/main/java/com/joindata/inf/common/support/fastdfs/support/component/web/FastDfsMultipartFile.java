package com.joindata.inf.common.support.fastdfs.support.component.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsClient;
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

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException
    {
        if(dest == null)
        {
            throw new FileNotFoundException("保存路径不能为 null");
        }

        if(dest instanceof FastDfsFile)
        {
            Builder metaBuilder = FileMetadata.newBuilder();
            metaBuilder.put("fileName", super.getOriginalFilename());
            ((FastDfsFile)dest).setUploadFuture(client.upload(dest.getPath(), super.getBytes(), metaBuilder.build()));
        }
        else
        {
            super.transferTo(dest);
        }
    }

}
