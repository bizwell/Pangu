package com.joindata.inf.common.support.kafka.support.dubbo.protocol;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * 拦截支持 Kafka 的服务，并将消息发送到 Kafka 中
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 20, 2017 2:13:03 PM
 */
public class KafkaMessagingServiceFilter implements Filter
{

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException
    {
//  TODO      KafkaMessagingServiceRegistry.supportTopic(invocation);
        return null;
    }

}
