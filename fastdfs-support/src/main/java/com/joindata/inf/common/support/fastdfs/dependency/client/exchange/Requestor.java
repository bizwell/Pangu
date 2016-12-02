/**
 *
 */
package com.joindata.inf.common.support.fastdfs.dependency.client.exchange;

import java.util.List;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

/**
 * 发送器
 *
 * @author liulongbiao
 */
public interface Requestor {

    /**
     * @param channel
     */
    void request(Channel channel);

    /**
     *
     */
    interface Encoder {

        /**
         * @param alloc
         */
        List<Object> encode(ByteBufAllocator alloc);
    }
}
