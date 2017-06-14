package com.joindata.inf.common.support.mybatis.properties;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.joindata.inf.common.sterotype.jdbc.sterotype.SlaveDataSourceProperties;
import com.joindata.inf.common.support.disconf.util.DisconfUtil;
import com.joindata.inf.common.support.mybatis.properties.inner.ShardingDataSourceProperties;
import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.network.NetworkUtil;
import com.joindata.inf.common.util.network.entity.JdbcConn;

import lombok.Data;

@Data
@Service
@Scope("singleton")
@DisconfFile(filename = DatasourceConf.FILENAME)
public class DatasourceConf implements InitializingBean
{
    static final String FILENAME = "datasource.json";

    /** 数据源列表 */
    private Map<String, ShardingDataSourceProperties> dataSources;

    /** 配置文件是否存在 */
    private boolean exists;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.toDatasourceMap();
    }

    /**
     * 当前配置生成可用的数据源 Map
     * 
     * @return 数据源 Map
     */
    public Map<String, DataSource> toDatasourceMap()
    {
        parse();

        Map<String, DataSource> map = CollectionUtil.newMap();

        if(this.dataSources != null)
        {
            this.dataSources.forEach((name, ds) ->
            {
                map.put(name, ds.toDataSource());
            });
        }

        return map;
    }

    private void parse()
    {
        if(DisconfUtil.exists(FILENAME))
        {
            if(this.dataSources == null)
            {
                DatasourceConf conf = DisconfUtil.readJson(FILENAME, DatasourceConf.class);
                BeanUtil.copyProperties(conf, this);
            }
            this.exists = true;
        }
    }

    @Override
    public String toString()
    {
        parse();

        Map<String, JdbcConn> desc = CollectionUtil.newMap();

        if(this.dataSources != null)
        {
            this.dataSources.forEach((name, props) ->
            {
                JdbcConn connProps = NetworkUtil.parseJdbcConn(props.getUrl());
                desc.put(name, connProps);
                if(props.getSlaves() != null)
                {
                    int i = 0;
                    for(SlaveDataSourceProperties slave: props.getSlaves())
                    {
                        desc.put(name + ".slave-" + (++i), NetworkUtil.parseJdbcConn(slave.getUrl()));
                    }
                }
            });
        }

        return desc.toString();
    }
}