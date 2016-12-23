package com.joindata.inf.common.support.sso.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户模型
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 20, 2016 5:06:26 PM
 */
public class AuthInfo implements UserDetails
{
    private static final long serialVersionUID = -187475053559529082L;

    /** 用户 ID */
    private String userId;

    /** 用户名 */
    private String username;

    /** 角色列表 */
    private RoleSet roleSet;

    /** 是否已过期 */
    private boolean expired;

    /** 是否锁定 */
    private boolean locked;

    /** 是否禁用 */
    private boolean disabled;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public RoleSet getRoleList()
    {
        return roleSet;
    }

    public void setRoleList(RoleSet roleSet)
    {
        this.roleSet = roleSet;
    }

    public boolean isExpired()
    {
        return expired;
    }

    public void setExpired(boolean expired)
    {
        this.expired = expired;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    /** 获取权限（角色）列表 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return this.getRoleList();
    }

    @Override
    public String getPassword()
    {
        return null;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return !this.isExpired();
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return !this.isLocked();
    }

    /** 密码不过期 */
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return !this.isDisabled();
    }
}
