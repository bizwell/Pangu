package com.joindata.inf.common.support.mybatis.bootconfig;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.mybatis.EnableMyBatis;
import com.joindata.inf.common.support.mybatis.mapper.BaseMapper;
import com.joindata.inf.common.support.mybatis.support.MapperHelperProps;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.log.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * MyBatis 通用 Mapper 设置
 *
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Aug 30, 2017 2:39:44 PM
 */
@Configuration
public class MapperHelperConfig {
    private static final Logger log = Logger.get();

    @Bean(name = "mapperHelper")
    public MapperScannerConfigurer mapperHelper() {
        MapperHelperProps props = Util.getMapperHelperProps();

        Properties properties = new Properties();
        properties.setProperty("IDENTITY", props.identity()); // 数据库方言（主要用于：取回主键的方式）
        properties.setProperty("notEmpty", String.valueOf(props.notEmpty())); // insert和update中，是否判断字符串类型!=''，少数方法会用到
        properties.setProperty("style", props.style().name());

        MapperScannerConfigurer scan = new MapperScannerConfigurer();
        scan.setSqlSessionFactoryBeanName("sqlSessionFactory"); // 多数据源时，必须配置
        scan.setBasePackage(ArrayUtil.join(Util.getScanPackage()));// 扫描包
        scan.setAnnotationClass(Repository.class);

        for (Class<?> clz : Util.getExternalMappers()) {
            scan.getMapperHelper().registerMapper(clz);
        }
        scan.setAnnotationClass(Repository.class);
        scan.getMapperHelper().registerMapper(BaseMapper.class);
        scan.setProperties(properties);

        log.info("创建通用 Mapper, 扫描包: {}, 额外 Mapper: {}", ArrayUtil.join(Util.getScanPackage()), ArrayUtil.toString(Util.getExternalMappers()));

        return scan;
    }

    private static final class Util {
        public static final String[] getScanPackage() {
            // 获取启动类的扫描包
            Class<?> bootClz = BootInfoHolder.getBootClass();
            EnableMyBatis anno = bootClz.getAnnotation(EnableMyBatis.class);

            if (ArrayUtil.isEmpty(anno.value())) {
                return new String[]{bootClz.getPackage().getName()};
            }

            return anno.value();
        }

        public static final Class<?>[] getExternalMappers() {
            // 获取启动类的扫描包
            Class<?> bootClz = BootInfoHolder.getBootClass();
            return bootClz.getAnnotation(EnableMyBatis.class).mappers();
        }

        public static final MapperHelperProps getMapperHelperProps() {
            Class<?> bootClz = BootInfoHolder.getBootClass();
            return bootClz.getAnnotation(EnableMyBatis.class).mapperHelperProps();
        }
    }
}