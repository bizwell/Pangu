/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.codec;

import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.Commands.SERVICE_QUERY_FETCH_ONE;

import com.joindata.inf.common.support.fastdfs.dependency.client.FileId;

/**
 * 获取可下载的存储服务器
 *
 * @author liulongbiao
 */
public class DownloadStorageGetEncoder extends FileIdOperationEncoder
{

    public DownloadStorageGetEncoder(FileId fileId)
    {
        super(fileId);
    }

    @Override
    protected byte cmd()
    {
        return SERVICE_QUERY_FETCH_ONE;
    }

}
