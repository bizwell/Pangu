package com.joindata.inf.common.support.fastdfs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsClient;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileId;

@Component
public class TestFastDFS
{
    @Autowired
    private FastdfsClient client;

    public String uploadFile(File file) throws InterruptedException, ExecutionException
    {
        CompletableFuture<FileId> ret = client.upload(file);
        return ret.get().toString();
    }

    public File downloadFile(String fileId, File outFile) throws InterruptedException, ExecutionException, IOException
    {
        OutputStream out = new FileOutputStream(outFile);
        CompletableFuture<Void> result = client.download(FileId.fromString("group1/M00/00/00/wKiJZFhAY1yAG2w8ABFgAK_YJSs210.doc"), out);

        result.get();

        out.close();

        return outFile;
    }
}
