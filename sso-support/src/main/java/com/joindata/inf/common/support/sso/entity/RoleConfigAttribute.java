package com.joindata.inf.common.support.sso.entity;

import org.springframework.security.access.ConfigAttribute;

/**
 * 角色权限属性
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 21, 2016 2:12:37 PM
 */
public class RoleConfigAttribute implements ConfigAttribute
{
    private static final long serialVersionUID = -1191869521718241378L;

    private Role role;

    public RoleConfigAttribute(Role role)
    {
        this.role = role;
    }

    /**
     * 返回的是角色 ID
     */
    @Override
    public String getAttribute()
    {
        return role.getRoleId();
    }

}
