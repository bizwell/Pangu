/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.codec;

import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.FDFS_GROUP_LEN;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.FDFS_HOST_LEN;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.FDFS_STORAGE_STORE_LEN;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsUtils.readString;

import com.joindata.inf.common.support.fastdfs.dependency.client.StorageServer;
import com.joindata.inf.common.support.fastdfs.dependency.client.exchange.Replier;

import io.netty.buffer.ByteBuf;

/**
 * 存储服务器信息解码器
 *
 * @author liulongbiao
 */
public enum StorageServerDecoder implements Replier.Decoder<StorageServer> {

    INSTANCE;

    @Override
    public long expectLength() {
        return FDFS_STORAGE_STORE_LEN;
    }

    @Override
    public StorageServer decode(ByteBuf in) {
        String group = readString(in, FDFS_GROUP_LEN);
        String host = readString(in, FDFS_HOST_LEN);
        int port = (int) in.readLong();
        byte idx = in.readByte();
        return new StorageServer(group, host, port, idx);
    }

}
