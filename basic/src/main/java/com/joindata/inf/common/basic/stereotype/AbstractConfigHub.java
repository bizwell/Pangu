package com.joindata.inf.common.basic.stereotype;

import com.joindata.inf.common.basic.exceptions.SystemException;

/**
 * 公共组件配置器抽象类，提供一些可能需要继承的方法模板
 * 
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午4:40:27
 */
public abstract class AbstractConfigHub
{
    /**
     * 检查环境是否完备<br />
     * <i>如果环境不完备，可抛出 SystemException，并在异常中指定缘由</i>
     * 
     * @throws SystemException 如果环境不完备，可抛出该异常并在异常中指定缘由
     */
    protected abstract void check() throws SystemException;

    public void executeCheck()
    {
        try
        {
            check();
        }
        catch(SystemException e)
        {
            System.err.println("错误：环境检查 >> " + e.getMessage());
            System.exit(0);
        }
    }
}
