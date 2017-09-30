package com.joindata.inf.common.support.mybatis.bootconfig;

import com.github.pagehelper.PageHelper;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.sterotype.jdbc.support.RoutingDataSource;
import com.joindata.inf.common.support.mybatis.EnableMyBatis;
import com.joindata.inf.common.support.mybatis.annotation.MyBatisPlugin;
import com.joindata.inf.common.support.mybatis.sterotype.PluginTemplate;
import com.joindata.inf.common.support.mybatis.support.CustomVfs;
import com.joindata.inf.common.support.mybatis.support.InsertEntityInterceptor;
import com.joindata.inf.common.support.mybatis.support.SQLStatementTypeInterceptor;
import com.joindata.inf.common.support.mybatis.support.annotation.EnablePageHelper;
import com.joindata.inf.common.support.mybatis.support.typehandler.DateTimeTypeHandler;
import com.joindata.inf.common.support.mybatis.support.typehandler.DateTypeHandler;
import com.joindata.inf.common.support.mybatis.support.typehandler.TimeTypeHandler;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.log.Logger;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * MyBatis 配置
 *
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月30日 上午10:32:18
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MyBatisConfig {
    private static final Logger log = Logger.get();

    @Autowired
    private RoutingDataSource dataSource;

    @Bean
    public org.apache.ibatis.session.Configuration sqlSessionFactoryConfiguration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(Utils.autoCamel());

        log.info("MyBatis 是否支持自动驼峰命名: {}", Utils.autoCamel());
        return configuration;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setVfs(CustomVfs.class);
        bean.setConfiguration(sqlSessionFactoryConfiguration());
        bean.setTypeHandlers(Utils.typeHandlers());

        Interceptor[] plugins = Utils.instantPlugins();

        if (!ArrayUtil.isEmpty(plugins)) {
            bean.setPlugins(plugins);
        }

        return bean;
    }

    /**
     * 实用工具
     */
    private static final class Utils {
        /**
         * 实例化自定义插件
         */
        public static final Interceptor[] instantPlugins() {
            Set<Class<?>> clzes = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), MyBatisPlugin.class);
            List<Interceptor> interceptors = new ArrayList<>();
            // 用于获取运行期间sql的类型： update or select
            interceptors.add(new SQLStatementTypeInterceptor());
            interceptors.add(new InsertEntityInterceptor());
            EnablePageHelper enablePageHelp = BootInfoHolder.getBootAnno(EnablePageHelper.class);
            if (null != enablePageHelp) {
                EnablePageHelper.PageHelpProperties[] pageHelpProperties = enablePageHelp.value();
                PageHelper pageHelper = new PageHelper();
                if (!ArrayUtil.isEmpty(pageHelpProperties)) {
                    Properties properties = new Properties();
                    for (EnablePageHelper.PageHelpProperties pageHelptmp : pageHelpProperties) {
                        properties.put(pageHelptmp.key(), pageHelptmp.value());
                    }
                    pageHelper.setProperties(properties);
                }
                interceptors.add(pageHelper);
            }
            for (Class<?> clz : clzes) {
                Object bean = SpringContextHolder.getBean(clz);
                if (!(bean instanceof PluginTemplate)) {
                    log.error("该类不是 PluginTemplate 的示例: {}, 插件将不会生效", clz.getCanonicalName());
                    continue;
                }

                Interceptor interceptor = ((PluginTemplate) bean).instantPlugin();

                interceptors.add(interceptor);
                log.info("MyBatis 使用插件: {}", interceptor.getClass().getCanonicalName());
            }

            return interceptors.toArray(new Interceptor[interceptors.size()]);
        }

        /**
         * 获取自定义类型转换器
         *
         * @return 类型转换器数组
         */
        public static final TypeHandler<?>[] typeHandlers() {
            return new TypeHandler<?>[]{new DateTimeTypeHandler(), new DateTypeHandler(), new TimeTypeHandler()};
        }

        /**
         * 是否使用自动驼峰转换
         */
        public static final boolean autoCamel() {
            return BootInfoHolder.getBootClass().getAnnotation(EnableMyBatis.class).autoCamel();
        }
    }
}