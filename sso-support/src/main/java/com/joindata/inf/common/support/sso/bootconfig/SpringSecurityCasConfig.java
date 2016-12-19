package com.joindata.inf.common.support.sso.bootconfig;

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.sso.EnableSso;
import com.joindata.inf.common.support.sso.properties.CasProperties;
import com.joindata.inf.common.support.sso.stereotype.SsoInterceptor;

/**
 * Spring Security 的 CAS 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 19, 2016 11:20:29 AM
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityCasConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private CasProperties properties;

    @Autowired
    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

    /** 设置验证处理服务 */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(casAuthenticationProvider());
    }

    /** 也是一种配置 */
    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers(Util.getIgnorePattern());
    }

    /** 注册过滤器等 */
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // CAS 认证拦截器
        http.addFilterBefore(casAuthenticationFilter(), BasicAuthenticationFilter.class);

        // CAS 异常处理
        http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());

        // 自定义拦截器
        for(Class<? extends SsoInterceptor> clz: Util.getInterceptor())
        {
            http.addFilterBefore(super.getApplicationContext().getBean(clz), FilterSecurityInterceptor.class);
        }

        // 登出设置
        LogoutConfigurer<HttpSecurity> handler = http.logout();
        handler.logoutRequestMatcher(new AntPathRequestMatcher(properties.getLogoutFilterUrl()));
        handler.addLogoutHandler(new SecurityContextLogoutHandler());
        handler.deleteCookies("JSESSIONID", "SESSION");

        // 对所有请求生效
        http.authorizeRequests().anyRequest().authenticated();

        // TODO 以后增加自定义配置 http.rememberMe();
    }

    /**
     * 服务变量
     */
    @Bean
    public ServiceProperties serviceProperties()
    {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(properties.getService());
        serviceProperties.setSendRenew(false);

        return serviceProperties;
    }

    /** CAS 入口配置 */
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint()
    {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(properties.getLoginUrl());
        entryPoint.setServiceProperties(serviceProperties());

        return entryPoint;
    }

    /** CAS 认证管理器 */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider()
    {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties());

        // 设置 CAS 服务器地址
        provider.setTicketValidator(new Cas20ServiceTicketValidator(properties.getCasServerUrlPrefix()));

        // 设置用户信息保存服务
        provider.setAuthenticationUserDetailsService(new UserDetailsByNameServiceWrapper<>(super.getApplicationContext().getBean(Util.getUserDetailService())));

        return provider;
    }

    /** CAS 过滤器 */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception
    {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        // 过滤器处理什么样的 URL
        filter.setFilterProcessesUrl(properties.getFilterProcessesUrl());

        // 设置成功后默认跳转的 URL 等
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setAlwaysUseDefaultTargetUrl(true);
        successHandler.setDefaultTargetUrl(properties.getDefaultTargetUrl());
        filter.setAuthenticationSuccessHandler(successHandler);

        // Session 管理策略（来自 session 组件）
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);

        // 认证管理器，用默认的
        filter.setAuthenticationManager(authenticationManager());
        return filter;

    }

    /**
     * 工具类
     * 
     * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
     * @date Dec 19, 2016 6:12:38 PM
     */
    private static final class Util
    {
        /**
         * 获取被 Security 机制排除的资源
         * 
         * @return 被排除的资源列表
         */
        public static final String[] getIgnorePattern()
        {
            EnableSso enableSso = BootInfoHolder.getBootClass().getAnnotation(EnableSso.class);

            return enableSso.ignorePattern();
        }

        /**
         * 获取用户详情处理服务
         * 
         * @return 用户详情处理服务
         */
        public static final Class<? extends UserDetailsService> getUserDetailService()
        {
            EnableSso enableSso = BootInfoHolder.getBootClass().getAnnotation(EnableSso.class);

            return enableSso.userDetailService();
        }

        /**
         * 获取拦截器
         * 
         * @return 用户详情类
         */
        public static final Class<? extends SsoInterceptor>[] getInterceptor()
        {
            EnableSso enableSso = BootInfoHolder.getBootClass().getAnnotation(EnableSso.class);

            return enableSso.interceptor();
        }
    }
}
