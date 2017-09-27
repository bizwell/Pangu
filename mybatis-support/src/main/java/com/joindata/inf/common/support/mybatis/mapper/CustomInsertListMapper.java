package com.joindata.inf.common.support.mybatis.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.List;
import java.util.Set;

/**
 * 原版tk.mapper中的InsertListMapper只能针对自增长id
 *
 * @author wangliqiu
 */
public interface CustomInsertListMapper<T> {

	/**
	 * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等
	 */
	@Options //默认keyProperty为id
	@InsertProvider(type = CustomProvider.class, method = "dynamicSQL")
	int insertList(List<T> recordList);

	/**
     * 批量插入
     *
     * @author wangliqiu
     * @implNote 键自动uuid由InsertEntityPlugin实现
     */
	public  static  class CustomProvider extends MapperTemplate {

        public CustomProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
            super(mapperClass, mapperHelper);
        }

        /**
         * sql形式：
         * insert into 表(id,xxx,xxx,...)
         * values
         * <foreach collection="list" item="record" separator=",">
         * (#{record.id},#{record.xxx},...)
         * </foreach>
         *
         * @implSpec 初始化就会调用生成固定sql
         * @implNote 方法名必须和接口CustomInsertListMapper方法名一致
         */
        public String insertList(MappedStatement ms) {
            final Class<?> entityClass = getEntityClass(ms);
            //开始拼sql
            StringBuilder sql = new StringBuilder();
            sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
            //不跳过主键id
            sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
            sql.append(" VALUES ");
            sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
            sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            //获取全部列
            Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
            //加一系列#{propName}
            for (EntityColumn column : columnList) {
                //@Column的insertable属性
                if (column.isInsertable()) {
                    sql.append(column.getColumnHolder("record")).append(",");
                }
            }
            sql.append("</trim>");
            sql.append("</foreach>");

            return sql.toString();
        }


    }
}