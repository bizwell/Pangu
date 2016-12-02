/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.codec;

import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.FDFS_GROUP_LEN;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsUtils.readString;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsException;
import com.joindata.inf.common.support.fastdfs.dependency.client.FileId;
import com.joindata.inf.common.support.fastdfs.dependency.client.exchange.Replier;

import io.netty.buffer.ByteBuf;

/**
 * 存储路径解码器
 *
 * @author liulongbiao
 */
public enum FileIdDecoder implements Replier.Decoder<FileId> {

    INSTANCE;

    @Override
    public FileId decode(ByteBuf in) {
        int length = in.readableBytes();
        if (length <= FDFS_GROUP_LEN) {
            throw new FastdfsException("body length : " + length + ", is lte required group name length 16.");
        }
        String group = readString(in, FDFS_GROUP_LEN);
        String path = readString(in);
        return new FileId(group, path);
    }

}