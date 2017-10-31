package com.joindata.inf.zipkin.properties;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by Rayee on 2017/10/23.
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "zipkin.properties")
public class ZipkinProperties {

    /**
     * zipkin host
     */
    private String server;

    @DisconfFileItem(name = "server", associateField = "server")
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
