package com.joindata.inf.common.support.disconf.cst;

import com.joindata.inf.common.basic.support.BootInfoHolder;

/**
 * Disconf 常量
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 11:20:37 AM
 */
public class DisconfCst
{
    /** Disconf 的文件下载目录 */
    public static final String DOWNLOAD_DIR = "/data/tmp/" + BootInfoHolder.getAppId() + "/" + BootInfoHolder.getAppVersion() + "/disconf";
}
