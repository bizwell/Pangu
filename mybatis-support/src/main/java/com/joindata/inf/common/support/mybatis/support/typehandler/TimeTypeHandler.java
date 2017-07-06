package com.joindata.inf.common.support.mybatis.support.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import com.joindata.inf.common.basic.entities.Time;

@MappedJdbcTypes({JdbcType.DATE, JdbcType.TIME, JdbcType.TIMESTAMP})
@MappedTypes({Time.class})
public class TimeTypeHandler implements TypeHandler<Time>
{
    @Override
    public void setParameter(PreparedStatement ps, int i, Time parameter, JdbcType jdbcType) throws SQLException
    {
        ps.setTime(i, parameter);
    }

    @Override
    public Time getResult(ResultSet rs, String columnName) throws SQLException
    {
        if(rs.getTime(columnName) != null)
        {
            return new Time(rs.getTime(columnName).getTime());
        }

        return null;
    }

    @Override
    public Time getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        if(rs.getTime(columnIndex) != null)
        {
            return new Time(rs.getTime(columnIndex).getTime());
        }
        return null;
    }

    @Override
    public Time getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        if(cs.getTime(columnIndex) != null)
        {
            return new Time(cs.getTime(columnIndex).getTime());
        }

        return null;
    }

}
