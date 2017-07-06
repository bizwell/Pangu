package com.joindata.inf.common.support.mybatis.support.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import com.joindata.inf.common.basic.entities.Date;

@MappedJdbcTypes({JdbcType.DATE, JdbcType.TIME, JdbcType.TIMESTAMP})
@MappedTypes({Date.class})
public class DateTypeHandler implements TypeHandler<Date>
{
    @Override
    public void setParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException
    {
        ps.setDate(i, parameter);
    }

    @Override
    public Date getResult(ResultSet rs, String columnName) throws SQLException
    {
        return Date.valueOf(rs.getDate(columnName));
    }

    @Override
    public Date getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        return Date.valueOf(rs.getDate(columnIndex));
    }

    @Override
    public Date getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        return Date.valueOf(cs.getDate(columnIndex));
    }

}
