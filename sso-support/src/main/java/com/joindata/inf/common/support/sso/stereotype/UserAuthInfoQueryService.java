package com.joindata.inf.common.support.sso.stereotype;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.joindata.inf.common.support.sso.entity.AuthInfo;

/**
 * 查询用户认证信息的服务<br />
 * 请实现该服务以查询用户认证信息
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 22, 2016 2:44:02 PM
 */
public abstract class UserAuthInfoQueryService implements UserDetailsService
{
    protected abstract AuthInfo queryUser(String username);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return queryUser(username);
    }

}
