/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.codec;

import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.Commands.SERVICE_QUERY_UPDATE;

import com.joindata.inf.common.support.fastdfs.dependency.client.FileId;

/**
 * 获取可更新的存储服务器
 *
 * @author liulongbiao
 */
public class UpdateStorageGetEncoder extends FileIdOperationEncoder {

    public UpdateStorageGetEncoder(FileId fileId) {
        super(fileId);
    }

    @Override
    protected byte cmd() {
        return SERVICE_QUERY_UPDATE;
    }

}
