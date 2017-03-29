package com.joindata.inf.common.support.idgen.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * ID 生成器配置变量文件
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 1:13:11 PM
 */
@Service
@Scope("singleton")
@DisconfFile(filename = IdgenProperties.FILENAME)
public class IdgenProperties
{
    public static final String FILENAME = "idgen.properties";

    private String zkHosts;
    
    private int rangeSize;

    @DisconfFileItem(name = "zk.hosts", associateField = "zkHosts")
    public String getZkHosts()
    {
        return zkHosts;
    }

    @DisconfFileItem(name = "range.size.max", associateField = "rangeSize")
	public int getRangeSize() {
		return rangeSize;
	}

	public void setRangeSize(int rangeSize) {
		this.rangeSize = rangeSize;
	}

	public void setZkHosts(String zkHosts)
    {
        this.zkHosts = zkHosts;
    }
    
    
}
