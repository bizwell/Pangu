package com.joindata.inf.common.sterotype.jdbc.exception;

import com.joindata.inf.common.basic.entities.ErrorEntity;
import com.joindata.inf.common.basic.errors.ResourceErrors;
import com.joindata.inf.common.basic.exceptions.BizException;

/**
 * 没有找到数据源，抛出该异常
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jun 9, 2017 12:37:14 PM
 */
public class NoSuchDatasourceException extends BizException
{
    private static final long serialVersionUID = 3375018717957005489L;

    public NoSuchDatasourceException(String key)
    {
        super("没有 " + key + " 这个数据源, 也没有默认数据源");
    }

    @Override
    public ErrorEntity getErrorEntity()
    {
        return ResourceErrors.NOT_FOUND;
    }

}
