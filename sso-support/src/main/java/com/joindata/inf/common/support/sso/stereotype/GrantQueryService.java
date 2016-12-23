package com.joindata.inf.common.support.sso.stereotype;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;

import com.joindata.inf.common.support.sso.entity.Resource;
import com.joindata.inf.common.support.sso.entity.ResourceRole;
import com.joindata.inf.common.support.sso.entity.RoleSet;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * 查询资源-角色关系的服务<br />
 * 请实现该服务以查询资源与角色的关系
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 22, 2016 2:47:02 PM
 */
public abstract class GrantQueryService
{
    /**
     * 查询角色-资源关系<br />
     * <b>请实现此方法！</b>
     * 
     * @return 角色-资源关系对象
     */
    protected abstract ResourceRole queryResourceRole();

    /** 查询结果缓存 */
    private static ResourceRole RESOURCE_ROLE;

    /** 符合 SpringSecurity 要求的资源-角色关系 */
    private static Map<String, Collection<ConfigAttribute>> PATH_ATTRIBUTES_MAP = CollectionUtil.newMap();

    /**
     * 获取缓存的资源-角色关系
     * 
     * @return 资源-角色关系
     */
    public ResourceRole getResourceRole()
    {
        if(RESOURCE_ROLE == null)
        {
            refresh();
        }

        return RESOURCE_ROLE;
    }

    /**
     * 获取资源对应的角色列表
     * 
     * @param resource 资源
     * @return 角色列表
     */
    public RoleSet getRoleSet(Resource resource)
    {
        if(RESOURCE_ROLE == null)
        {
            refresh();
        }

        return RESOURCE_ROLE.get(resource);
    }

    /**
     * 根据资源（URL）获取对应的角色集合
     * 
     * @param path 资源（URL）
     * @return 角色集合
     */
    public Collection<ConfigAttribute> getConfigAttributes(String path)
    {
        // 很奇怪的 RoleList 不会被当做 Collection<ConfigAttribute>，那就只能用这个别扭的方式了
        if(PATH_ATTRIBUTES_MAP.get(path) == null)
        {
            Collection<ConfigAttribute> attrs = CollectionUtil.newHashSet();
            for(ConfigAttribute attr: RESOURCE_ROLE.getRoleSet(path))
            {
                attrs.add(attr);
            }

            PATH_ATTRIBUTES_MAP.put(path, attrs);
        }

        return PATH_ATTRIBUTES_MAP.get(path);
    }

    /**
     * 调用此方法会调用 queryResourceRole() 从而刷新权限-资源关系的缓存<br />
     * 如果调整了权限，最好调用一下
     */
    public void refresh()
    {
        RESOURCE_ROLE = queryResourceRole();
        PATH_ATTRIBUTES_MAP.clear();
    }
}
