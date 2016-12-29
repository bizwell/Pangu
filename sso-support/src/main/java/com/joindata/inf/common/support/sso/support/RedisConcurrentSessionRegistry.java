package com.joindata.inf.common.support.sso.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.sso.entity.AuthInfo;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

@Component
public class RedisConcurrentSessionRegistry implements SessionRegistry, ApplicationListener<SessionDestroyedEvent>
{
    private static final Logger log = Logger.get();
    
    @Autowired
    private RedisOperationsSessionRepository repository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private String keyPrefix;

    private static final String DEFAULT_SPRING_SESSION_REDIS_PREFIX = "spring:session:";

    private static final String SESSION_ATTR_PREFIX = "sessionAttr:";

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private static final String LAST_ACCESSED_ATTR = "lastAccessedTime";

    private RedisSerializer<Object> defaultSerializer = new JdkSerializationRedisSerializer();

    public RedisConcurrentSessionRegistry()
    {
        this.keyPrefix = DEFAULT_SPRING_SESSION_REDIS_PREFIX + BootInfoHolder.getAppId() + ":";
    }

    @Override
    public List<Object> getAllPrincipals()
    {
        return null;
    }

    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions)
    {
        if(principal == null)
        {
            return CollectionUtil.newList();
        }

        AuthInfo info = (AuthInfo)principal;

        log.debug("获取用户 {} 的全部 session{}", info.getUsername(), includeExpiredSessions ? ", 包括已过期的" : "");

        // 如果 Session 为空，返回空的
        List<String> sessionIds = getAllSessionIds(info.getUsername());
        if(CollectionUtil.isNullOrEmpty(sessionIds))
        {
            return CollectionUtil.newList();
        }

        // 根据 sessionid 获取 session 信息
        List<SessionInformation> session = new ArrayList<>(sessionIds.size());
        for(String sessionId: sessionIds)
        {
            SessionInformation sessionInformation = getSessionInformation(sessionId);
            if(sessionInformation != null)
            {
                session.add(sessionInformation);
            }
        }

        log.info("用户 {} 当前 session 数: {}", info.getUsername(), session.size());
        return session;
    }

    /**
     * 获取用户全部 session id
     * 
     * @param username 用户名
     * @return 全部 SessionID
     */
    private List<String> getAllSessionIds(String username)
    {

        log.debug("获取用户 {} 的全部 sessionId: ", username);

        if(StringUtils.isEmpty(username))
        {
            return Collections.emptyList();
        }
        
        final byte[] key = getPrincipalKey(username).getBytes();
        return redisTemplate.execute(new RedisCallback<List<String>>()
        {
            @Override
            public List<String> doInRedis(RedisConnection connection) throws DataAccessException
            {
                Set<byte[]> vals = connection.sMembers(key);
                List<String> sessionIds = new ArrayList<>();
                if(null != vals && !vals.isEmpty())
                {
                    for(byte[] bytes: vals)
                    {
                        sessionIds.add((String)defaultSerializer.deserialize(bytes));
                    }
                }
                return sessionIds;
            }
        });
    }

    @Override
    public SessionInformation getSessionInformation(final String sessionId)
    {
        log.debug("获取 session 信息，session id: {}", sessionId);

        final byte[] key = getSessionKey(sessionId).getBytes();
        return redisTemplate.execute(new RedisCallback<SessionInformation>()
        {
            @Override
            public SessionInformation doInRedis(RedisConnection connection) throws DataAccessException
            {
                byte[] vals = connection.hGet(key, getSessionAttrNameKey(SPRING_SECURITY_CONTEXT).getBytes());
                byte[] lastAccessedAttr = connection.hGet(key, LAST_ACCESSED_ATTR.getBytes());
                Long lastRequestTime = (Long)defaultSerializer.deserialize(lastAccessedAttr);

                SecurityContextImpl securityContext = (SecurityContextImpl)defaultSerializer.deserialize(vals);
                if(null == securityContext || null == securityContext.getAuthentication())
                {
                    return null;
                }
                return new SessionInformation(securityContext.getAuthentication().getPrincipal(), sessionId, new Date(lastRequestTime));
            }
        });
    }

    @Override
    public void refreshLastRequest(String sessionId)
    {
        log.info("refresh last request [{}].", sessionId);
    }

    @Override
    public void registerNewSession(String sessionId, Object principal)
    {
        log.info("Registering session {}, for principal {}", sessionId, principal);
    }

    @Override
    public void removeSessionInformation(String sessionId)
    {
        log.info("Removing session {} from set of registered sessions", sessionId);
    }

    /**
     * Session 销毁时，删除 session
     */
    @Override
    public void onApplicationEvent(SessionDestroyedEvent event)
    {
        repository.delete(event.getId());
        
        String sessionId = event.getId();
        removeSessionInformation(sessionId);
    }

    String getSessionKey(String sessionId)
    {
        return this.keyPrefix + "sessions:" + sessionId;
    }

    String getPrincipalKey(String principalName)
    {
        return this.keyPrefix + "index:" + FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME + ":" + principalName;
    }

    String getSessionAttrNameKey(String attributeName)
    {
        return SESSION_ATTR_PREFIX + attributeName;
    }

    String getExpiredKey(String sessionId)
    {
        return this.keyPrefix + "sessions:" + "expires:" + sessionId;
    }

}
