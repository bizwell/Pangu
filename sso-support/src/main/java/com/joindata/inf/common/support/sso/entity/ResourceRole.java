package com.joindata.inf.common.support.sso.entity;

import java.util.HashMap;

/**
 * 资源-角色对应关系
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 23, 2016 12:45:19 PM
 */
public class ResourceRole extends HashMap<Resource, RoleSet>
{
    private static final long serialVersionUID = 8397481421362965591L;

    public void put(Resource resource, Role... roles)
    {
        super.put(resource, new RoleSet(roles));
    }

    /**
     * 获取拥有该资源的角色
     * 
     * @param resource 资源
     * @return 对应的角色列表
     */
    public RoleSet getRoleSet(Resource resource)
    {
        return this.get(resource);
    }

    /**
     * 获取拥有该资源的角色
     * 
     * @param resourceValue 资源值（URL）
     * @return 对应的角色列表
     */
    public RoleSet getRoleSet(String resourceValue)
    {
        return this.get(resourceValue);
    }
}