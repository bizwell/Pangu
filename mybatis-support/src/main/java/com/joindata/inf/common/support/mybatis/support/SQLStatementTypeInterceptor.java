package com.joindata.inf.common.support.mybatis.support;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.alibaba.druid.sql.parser.Lexer;
import com.dangdang.ddframe.rdb.sharding.exception.SQLParserException;
import com.dangdang.ddframe.rdb.sharding.parser.result.router.SQLStatementType;

/**
 * mybatis插件用于获取运行时执行的sql的类型（select， update）
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年6月19日
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SQLStatementTypeInterceptor implements Interceptor
{
    @Override
    public void setProperties(Properties properties)
    {
        // DO NOTHING
    }

    @Override
    public Object plugin(Object target)
    {
        return Plugin.wrap(target, this);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        Object parameter = null;
        if(invocation.getArgs().length > 1)
        {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        SQLStatementType sqlStatementType = getTypeByStart(boundSql.getSql());
        SQLStatementTypeHolder.set(sqlStatementType);
        Object result = invocation.proceed();
        SQLStatementTypeHolder.clear();
        return result;
    }

    /**
     * 根据SQL第一个单词判断SQL类型.
     * 
     * @param sql SQL语句
     * @return SQL类型
     */
    private SQLStatementType getTypeByStart(final String sql)
    {
        Lexer lexer = new Lexer(sql);
        lexer.nextToken();
        while(true)
        {
            switch(lexer.token())
            {
                case SELECT:
                    return SQLStatementType.SELECT;
                case INSERT:
                    return SQLStatementType.INSERT;
                case UPDATE:
                    return SQLStatementType.UPDATE;
                case DELETE:
                    return SQLStatementType.DELETE;
                case EOF:
                    throw new SQLParserException("Unsupported SQL statement: [%s]", sql);
                default:
                    lexer.nextToken();
            }
        }
    }

}
