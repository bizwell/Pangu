package com.joindata.inf.common.support.mybatis.support;


import com.joindata.inf.common.support.mybatis.bean.BaseDomain;
import com.joindata.inf.common.util.tools.UUIDUtil2;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by likanghua on 2017/3/2.
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class InsertEntityPlugin implements Interceptor {

    private final static Map<Class, IdPolicy.Policy> cache = new ConcurrentHashMap<>();

    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object object = invocation.getArgs()[1];

        if (object instanceof BaseDomain) {
            BaseDomain domain = (BaseDomain) object;
            if (ms.getSqlCommandType() == SqlCommandType.UPDATE) {
                domain.setUpdateTime(new Date());


            } else if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
                processDomain(domain);
            }
        }
        // 是集合类且为insert
        else if (object instanceof Collection && ms.getSqlCommandType() == SqlCommandType.INSERT) {
            Collection collection = (Collection) object;
            for (Object tmp : collection) {
                if (tmp instanceof BaseDomain) {
                    processDomain((BaseDomain) tmp);
                }
            }
        }
        // DefaultSqlSession调用wrapCollection()方法，将入参放入map，其key为collection和list
        else if (object instanceof StrictMap) {
            if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
                for (Object obj : (Collection) ((StrictMap) object).get("collection")) {
                    if (obj instanceof BaseDomain) {
                        processDomain((BaseDomain) obj);
                    }
                }
            }
        }

        return invocation.proceed();
    }

    private void processDomain(BaseDomain domain) {
        Class clazz = domain.getClass();
        IdPolicy.Policy policy = cache.get(clazz);
        if (policy == null) {
            IdPolicy idPolicy = (IdPolicy) clazz.getAnnotation(IdPolicy.class);
            policy = idPolicy.value();
            cache.put(clazz, policy);
        }
        if (null == domain.getId()) {
            if (policy == IdPolicy.Policy.UUID || null == policy)
                domain.setId(UUIDUtil2.getUUIdString());
            if (policy == IdPolicy.Policy.ZKID) {
                domain.setId(UUIDUtil2.getUUIdString());
            }
        }
        if (null == domain.getIsAvailable())
            domain.setIsAvailable(true);
        if (null == domain.getCreateTime())
            domain.setCreateTime(new Date());
        if (null == domain.getUpdateTime())
            domain.setUpdateTime(new Date());

    }


    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }


    public void setProperties(Properties properties) {
    }


}
