/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.codec;

import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.ERRNO_OK;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.FDFS_GROUP_LEN;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.FDFS_HEAD_LEN;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.Commands.SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.Commands.SERVICE_QUERY_STORE_WITH_GROUP_ONE;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsUtils.isEmpty;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsUtils.writeFixLength;

import java.util.Collections;
import java.util.List;

import com.joindata.inf.common.support.fastdfs.dependency.client.exchange.Requestor;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 获取可上传的存储服务器
 *
 * @author liulongbiao
 */
public class UploadStorageGetEncoder implements Requestor.Encoder {

    private String group;

    public UploadStorageGetEncoder(String group) {
        this.group = group;
    }

    @Override
    public List<Object> encode(ByteBufAllocator alloc) {
        int length = isEmpty(group) ? 0 : FDFS_GROUP_LEN;
        byte cmd = isEmpty(group) ? SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE : SERVICE_QUERY_STORE_WITH_GROUP_ONE;

        ByteBuf buf = alloc.buffer(length + FDFS_HEAD_LEN);
        buf.writeLong(length);
        buf.writeByte(cmd);
        buf.writeByte(ERRNO_OK);
        if (!isEmpty(group)) {
            writeFixLength(buf, group, FDFS_GROUP_LEN);
        }
        return Collections.singletonList(buf);
    }
}
