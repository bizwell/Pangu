package com.joindata.inf.common.util.algorithm.entity;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 树节点
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Feb 17, 2017 4:31:55 PM
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TreeNode
{
    @XmlAttribute
    private String code;

    @XmlAttribute
    private String parentCode;

    @XmlElementWrapper(name = "children")
    @XmlElement(name = "node")
    private Collection<TreeNode> children;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Collection<TreeNode> getChildren()
    {
        return children;
    }

    public void setChildren(Collection<TreeNode> children)
    {
        this.children = children;
    }

    public String getParentCode()
    {
        return parentCode;
    }

    public void setParentCode(String parentCode)
    {
        this.parentCode = parentCode;
    }

}
