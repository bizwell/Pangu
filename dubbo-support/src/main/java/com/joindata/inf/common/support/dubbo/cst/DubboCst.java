package com.joindata.inf.common.support.dubbo.cst;

import com.joindata.inf.common.basic.support.BootInfoHolder;

/**
 * Dubbo 用到的一些常量
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 1, 2017 1:35:22 PM
 */
public class DubboCst
{
    public static final String CACHE_FILE = "/data/tmp/" + BootInfoHolder.getAppId() + "/" + BootInfoHolder.getAppVersion() + "/dubbo/dubbo.cache";
}
