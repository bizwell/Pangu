/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.codec;

import java.io.File;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants;

/**
 * 上传追加文件请求
 *
 * @author liulongbiao
 */
public class FileUploadAppenderEncoder extends FileUploadEncoder {

    /**
     * @param file
     * @param pathIdx
     */
    public FileUploadAppenderEncoder(File file, byte pathIdx) {
        super(file, pathIdx);
    }

    /**
     * @param content
     * @param filename
     * @param length
     * @param pathIdx
     */
    public FileUploadAppenderEncoder(Object content, String filename, long length, byte pathIdx) {
        super(content, filename, length, pathIdx);
    }

    @Override
    protected byte cmd() {
        return FastdfsConstants.Commands.APPENDER_FILE_UPLOAD;
    }
}
