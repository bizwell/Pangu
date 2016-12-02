/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.codec;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileId;

/**
 * 删除请求
 *
 * @author liulongbiao
 */
public class FileDeleteEncoder extends FileIdOperationEncoder {

    public FileDeleteEncoder(FileId fileId) {
        super(fileId);
    }

    @Override
    protected byte cmd() {
        return FastdfsConstants.Commands.FILE_DELETE;
    }
}
