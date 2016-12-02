package com.joindata.inf.common.support.fastdfs.dependency.client.exchange;

import java.util.List;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

/**
 * @author siuming
 */
public abstract class RequestorSupport implements Requestor {

    @Override
    public void request(Channel channel) {
        List<Object> requests = writeRequests(channel.alloc());
        requests.forEach(channel::write);
        channel.flush();
    }

    /**
     * @param alloc
     */
    protected abstract List<Object> writeRequests(ByteBufAllocator alloc);
}
