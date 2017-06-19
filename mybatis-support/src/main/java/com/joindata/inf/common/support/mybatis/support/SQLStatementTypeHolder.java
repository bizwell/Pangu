package com.joindata.inf.common.support.mybatis.support;

import com.dangdang.ddframe.rdb.sharding.parser.result.router.SQLStatementType;

public class SQLStatementTypeHolder
{
    private static final ThreadLocal<SQLStatementType> SQL_STATMENT_TYPE_HOLDER = new ThreadLocal<>();

    public static void set(SQLStatementType sqlStatementType)
    {
        SQL_STATMENT_TYPE_HOLDER.set(sqlStatementType);
    }

    public static void clear()
    {
        SQL_STATMENT_TYPE_HOLDER.remove();
    }

    public static SQLStatementType get()
    {
        return SQL_STATMENT_TYPE_HOLDER.get();
    }

}
