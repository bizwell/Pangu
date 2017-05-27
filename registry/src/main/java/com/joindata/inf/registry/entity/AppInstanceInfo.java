package com.joindata.inf.registry.entity;

import java.io.Serializable;
import java.util.Map;

import com.joindata.inf.common.basic.entities.DateTime;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.basic.support.BootInfoHolder.Env;
import com.joindata.inf.common.util.basic.DateUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.basic.SystemUtil;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AppInstanceInfo implements Serializable
{
    private static final long serialVersionUID = 761298146262075570L;

    private String instanceId;

    private String env;

    private String sys;

    private String app;

    /** 系统ID.应用名 */
    private String appId;

    private String version;

    /** 系统ID.应用名-版本号 */
    private String appSign;

    private String pid;

    private long bootTime;

    private Map<String, Serializable> params;

    public AppInstanceInfo()
    {
        this.env = Env.get();
        this.sys = StringUtil.substringBeforeFirst(BootInfoHolder.getAppId(), ".");
        this.app = StringUtil.substringAfterLast(BootInfoHolder.getAppId(), ".");
        this.version = BootInfoHolder.getAppVersion();
        this.appId = BootInfoHolder.getAppId();
        this.appSign = BootInfoHolder.getAppId() + "-" + this.version;
        this.pid = SystemUtil.getProcessId();
        this.bootTime = System.currentTimeMillis();
        this.instanceId = SystemUtil.getRuntimeSignature(this.pid, DateUtil.formatDateTime(new DateTime(this.bootTime)));
        this.params = BootInfoHolder.getBootInfoMap();
    }
}
