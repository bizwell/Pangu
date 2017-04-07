package com.joindata.inf.common.support.fastdfs.support.component.web;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.joindata.inf.common.support.fastdfs.dependency.client.FileId;
import com.joindata.inf.common.util.log.Logger;

/**
 * FastDFS 文件
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月7日 上午11:34:59
 */
public class FastDfsFile extends File
{
    private static final Logger log = Logger.get();

    private static final long serialVersionUID = -8873842185535273355L;

    /** 上传后文件 ID */
    private CompletableFuture<FileId> fileIdFuture;

    /** 组 */
    private String group;

    /**
     * 创建一个 FastDFS 文件， 使用默认 group
     * 
     * @param savePath 保存路径
     */
    public FastDfsFile(String savePath)
    {
        super(savePath);
        log.info("定义 FastDFS 文件, group: 默认, 远程路径: {}", savePath);
    }

    /**
     * 创建一个 FastDFS 文件，指定 group
     * 
     * @param group 文件保存在哪个组
     * @param savePath 保存路径
     */
    public FastDfsFile(String group, String savePath)
    {
        super(savePath);
        this.group = group;
        log.info("定义 FastDFS 文件, group: {}, 远程路径: {}", group, savePath);
    }

    void setUploadFuture(CompletableFuture<FileId> fileIdFuture)
    {
        this.fileIdFuture = fileIdFuture;
    }

    /**
     * 获取上传后的远程文件 ID
     * 
     * @return 文件 ID
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public String getId() throws InterruptedException, ExecutionException
    {
        return fileIdFuture.get().toString();
    }

    public String getGroup()
    {
        return group;
    }

    /**
     * 获取上传后的远程文件 ID 对象
     * 
     * @return 文件 ID 对象
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public FileId getFileId() throws InterruptedException, ExecutionException
    {
        return fileIdFuture.get();
    }
}
