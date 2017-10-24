package com.joindata.inf.common.support.restTemplate.properties;

/**
 * Created by likanghua on 2017/9/25.
 */

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import org.springframework.stereotype.Component;

@DisconfFile(filename = "restTemplate.properties")
@EnableDisconf
@Component
public class RestTemplateProperties {

    private Integer maxTotal;

    private Integer defaultMaxPerRoute;

    private Integer connectTimeout;


    private Integer retryCount;

    private Integer readTimeout;

    private String routers;


    @DisconfFileItem(associateField = "routers", name = "restTemplate.routers")
    public String getRouters() {
        return routers;
    }

    @DisconfFileItem(associateField = "maxTotal", name = "restTemplate.maxTotal")
    public Integer getMaxTotal() {
        return maxTotal;
    }

    @DisconfFileItem(associateField = "defaultMaxPerRoute", name = "restTemplate.defaultMaxPerRoute")
    public Integer getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    @DisconfFileItem(associateField = "connectTimeout", name = "restTemplate.connectTimeout")
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    @DisconfFileItem(associateField = "readTimeout", name = "restTemplate.readTimeout")
    public Integer getReadTimeout() {
        return readTimeout;
    }

    @DisconfFileItem(associateField = "retryCount", name = "restTemplate.retryCount")
    public Integer getRetryCount() {
        return retryCount;
    }
}
