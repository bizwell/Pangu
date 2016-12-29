package com.joindata.inf.common.support.sso.entity;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

import com.joindata.inf.common.util.basic.StringUtil;

/**
 * 角色模型
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 20, 2016 5:07:28 PM
 */
public class Role implements GrantedAuthority, ConfigAttribute
{
    private static final long serialVersionUID = 8120289129951922203L;

    /** 角色 ID */
    private String roleId;

    /** 角色名 */
    private String roleName;

    public String getRoleId()
    {
        return roleId;
    }

    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    /** 返回角色 ID */
    @Override
    public String getAuthority()
    {
        return getRoleId();
    }

    @Override
    public int hashCode()
    {
        if(this.roleId == null)
        {
            return super.hashCode();
        }

        return this.roleId.hashCode();
    }

    /**
     * 如果传入的对象是 ConfigAttribute，会根据 roleId 来判定是否相同
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof ConfigAttribute)
        {
            return StringUtil.isEquals(this.roleId, ((ConfigAttribute)obj).getAttribute());
        }

        return super.equals(obj);
    }

    @Override
    public String getAttribute()
    {
        return this.roleId;
    }

    @Override
    public String toString()
    {
        return this.roleId + "(" + this.roleName + ")";
    }

}
