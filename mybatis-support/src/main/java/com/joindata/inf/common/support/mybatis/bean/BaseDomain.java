package com.joindata.inf.common.support.mybatis.bean;

import com.joindata.inf.common.support.mybatis.support.IdPolicy;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created by likanghua on 2017/2/28.
 * 单表实体操作的基础bean
 */
@Data
@EqualsAndHashCode(callSuper = true)
@IdPolicy
public class BaseDomain extends BasePojo {

    /**
     * 主键id 采用uuid做主键策略
     */
    @Id
    private String id;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最近一次的修改时间
     */
    private Date updateTime;

    /**
     * 逻辑删除标志位
     */
    private Boolean isAvailable;


    /**
     * 创建资源的用户
     */
    private String createUser;


    /**
     * 修改资源的用户
     */
    private String updateUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public Boolean getIsAvailable() {
        return this.isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }


}
