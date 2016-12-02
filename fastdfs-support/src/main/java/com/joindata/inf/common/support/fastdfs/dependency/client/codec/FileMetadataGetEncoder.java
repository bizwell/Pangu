/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.codec;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileId;

/**
 * 获取文件属性请求
 *
 * @author liulongbiao
 */
public class FileMetadataGetEncoder extends FileIdOperationEncoder {

    public FileMetadataGetEncoder(FileId fileId) {
        super(fileId);
    }

    @Override
    public byte cmd() {
        return FastdfsConstants.Commands.METADATA_GET;
    }

}