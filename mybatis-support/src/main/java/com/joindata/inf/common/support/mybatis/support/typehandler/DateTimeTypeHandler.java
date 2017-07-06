package com.joindata.inf.common.support.mybatis.support.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import com.joindata.inf.common.basic.entities.DateTime;

@MappedJdbcTypes({JdbcType.DATE, JdbcType.TIME, JdbcType.TIMESTAMP})
@MappedTypes({DateTime.class})
public class DateTimeTypeHandler implements TypeHandler<DateTime>
{
    @Override
    public void setParameter(PreparedStatement ps, int i, DateTime parameter, JdbcType jdbcType) throws SQLException
    {
        ps.setTimestamp(i, parameter);
    }

    @Override
    public DateTime getResult(ResultSet rs, String columnName) throws SQLException
    {
        return new DateTime(rs.getTimestamp(columnName));
    }

    @Override
    public DateTime getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        return new DateTime(rs.getTimestamp(columnIndex));
    }

    @Override
    public DateTime getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        return new DateTime(cs.getTimestamp(columnIndex));
    }

}
