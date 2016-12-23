package com.joindata.inf.common.support.sso.entity;

import java.util.Collection;
import java.util.HashMap;

import org.springframework.security.access.ConfigAttribute;

/**
 * 角色资源对应关系
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 20, 2016 5:13:37 PM
 */
public class RoleResource extends HashMap<Role, ResourceList>
{
    private static final long serialVersionUID = -2210391718727114948L;

    /**
     * 判断角色和资源有没有对应关系
     * 
     * @param role 角色
     * @param 资源
     * @return true，如果有对应关系
     */
    public boolean hasResource(Role role, Resource resource)
    {
        return this.get(role).contains(resource);
    }

    /**
     * 判断角色集合中有没有至少一个与该资源的对应关系
     * 
     * @param roles 角色集合（权限属性）
     * @param resource 资源（URL）
     * @return true，如果有对应关系
     */
    public boolean hasResource(Collection<ConfigAttribute> roles, String resource)
    {
        for(ConfigAttribute role: roles)
        {
            if(this.get(role).contains(resource))
            {
                return true;
            }
        }

        return false;
    }

}