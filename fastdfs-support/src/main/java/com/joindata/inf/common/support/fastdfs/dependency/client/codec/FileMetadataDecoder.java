/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.codec;

import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.FDFS_FIELD_SEPERATOR;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsConstants.FDFS_RECORD_SEPERATOR;
import static com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsUtils.readString;

import java.util.HashMap;
import java.util.Map;

import com.joindata.inf.common.support.fastdfs.dependency.client.FileMetadata;
import com.joindata.inf.common.support.fastdfs.dependency.client.exchange.Replier;

import io.netty.buffer.ByteBuf;

/**
 * 文件属性解码器
 *
 * @author liulongbiao
 */
public enum FileMetadataDecoder implements Replier.Decoder<FileMetadata> {
    INSTANCE;

    @Override
    public FileMetadata decode(ByteBuf buf) {
        String content = readString(buf);

        Map<String, String> values = new HashMap<>();
        String[] pairs = content.split(FDFS_RECORD_SEPERATOR);
        for (String pair : pairs) {
            String[] kv = pair.split(FDFS_FIELD_SEPERATOR, 2);
            if (kv.length == 2) {
                values.put(kv[0], kv[1]);
            }
        }
        return new FileMetadata(values);
    }

}
