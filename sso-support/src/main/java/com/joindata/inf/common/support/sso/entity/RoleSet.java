package com.joindata.inf.common.support.sso.entity;

import java.util.HashSet;

import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * 角色列表
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 20, 2016 5:09:43 PM
 */
public class RoleSet extends HashSet<Role>
{
    private static final long serialVersionUID = -1247454620428195574L;

    public RoleSet()
    {
        super();
    }

    public RoleSet(Role... roles)
    {
        super(CollectionUtil.newHashSet(roles));
    }
}
