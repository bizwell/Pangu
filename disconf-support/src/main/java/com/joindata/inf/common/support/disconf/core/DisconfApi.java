package com.joindata.inf.common.support.disconf.core;

import java.io.IOException;

import com.joindata.inf.common.basic.errors.SystemError;
import com.joindata.inf.common.basic.exceptions.SystemException;
import com.joindata.inf.common.support.disconf.bootconfig.DisconfConfig;
import com.joindata.inf.common.support.disconf.core.entity.ZkHostResponse;
import com.joindata.inf.common.support.disconf.core.exception.ApiException;
import com.joindata.inf.common.util.network.HttpUtil;

/**
 * DisconfApi 工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 26, 2017 10:15:04 AM
 */
public class DisconfApi
{
    private static final String DISCONF_ZK_URL = "http://" + DisconfConfig.getDisconfServerHost() + "/api/zoo/hosts";

    /**
     * 获取 Zookeeper 服务器连接串
     * 
     * @return Zookeeper 连接串
     */
    public static final String getZkHost()
    {
        ZkHostResponse response;
        try
        {
            response = HttpUtil.getJson(DISCONF_ZK_URL, ZkHostResponse.class);
        }
        catch(IOException e)
        {
            throw new SystemException(SystemError.DEPEND_RESOURCE_CANNOT_READY, "下载 Disconf 的 Zookeeper 连接串出错: " + e.getMessage());
        }

        if(response.getStatus() != 1)
        {
            throw new ApiException(response.getMessage());
        }
        return response.getValue();
    }
}
