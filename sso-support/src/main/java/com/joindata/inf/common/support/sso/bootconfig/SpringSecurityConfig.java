package com.joindata.inf.common.support.sso.bootconfig;

import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
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
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.sso.EnableSso;
import com.joindata.inf.common.support.sso.properties.CasProperties;
import com.joindata.inf.common.support.sso.stereotype.GrantQueryService;
import com.joindata.inf.common.support.sso.stereotype.UserAuthInfoQueryService;
import com.joindata.inf.common.support.sso.support.CustomAccessDecisionManager;
import com.joindata.inf.common.support.sso.support.CustomConcurrentSessionControlAuthenticationStrategy;
import com.joindata.inf.common.support.sso.support.CustomSecurityInterceptor;
import com.joindata.inf.common.support.sso.support.CustomSecurityMetadataSource;
import com.joindata.inf.common.support.sso.support.RedisConcurrentSessionRegistry;

/**
 * Spring Security 的 CAS 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 19, 2016 11:20:29 AM
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private CasProperties properties;

    @Autowired
    private RedisConcurrentSessionRegistry redisConcurrentSessionRegistry;

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

        // 权限处理
        http.addFilterBefore(customSecurityInterceptor(), FilterSecurityInterceptor.class);

        // 登出设置
        LogoutConfigurer<HttpSecurity> handler = http.logout();
        handler.logoutRequestMatcher(new AntPathRequestMatcher(properties.getLogoutFilterUrl()));
        handler.addLogoutHandler(new SecurityContextLogoutHandler());
        handler.deleteCookies(properties.getCookies(), "SESSION");

        // 对所有请求生效
        http.authorizeRequests().anyRequest().authenticated();

        // TODO 以后增加自定义配置 http.rememberMe();
    }

    /** 自定义的权限拦截器 */
    @Bean
    public CustomSecurityInterceptor customSecurityInterceptor()
    {
        SecurityMetadataSource metadataSource = new CustomSecurityMetadataSource(super.getApplicationContext().getBean(Util.getGrantQuery()));
        AccessDecisionManager manager = new CustomAccessDecisionManager();
        CustomSecurityInterceptor interceptor = new CustomSecurityInterceptor(metadataSource, manager);
        return interceptor;
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
        // TODO 后续根据张凯写的下面的注释做支持 REST 方式的 entryPoint 处理器
        // 如果此处设置成重定向CAS的登录地址，那么必须解决调用方和服务方的跨域访问，否则调用方会出现No Access Control Allow Origin 的错误。
        // 或者自定CasAuthenticationEntryPoint，直接返回一个错误码到前端。让前端自己去重定向到CAS服务端，但是重定向时必须设置service等于服务端的地址，
        // 如：http://server:port/context/j_spring_cas_security_check
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

        provider.setKey(properties.getKey());

        // 设置用户信息保存服务
        provider.setAuthenticationUserDetailsService(new UserDetailsByNameServiceWrapper<>(super.getApplicationContext().getBean(Util.getUserAuthQueryService())));

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
        filter.setSessionAuthenticationStrategy(new CustomConcurrentSessionControlAuthenticationStrategy(redisConcurrentSessionRegistry));

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
         * 获取用户认证信息查询服务 Class
         * 
         * @return 用户认证信息查询服务 Class
         */
        public static final Class<? extends UserAuthInfoQueryService> getUserAuthQueryService()
        {
            EnableSso enableSso = BootInfoHolder.getBootClass().getAnnotation(EnableSso.class);

            return enableSso.userService();
        }

        /**
         * 获取权限关系查询服务 Class
         * 
         * @return 权限关系查询服务 Class
         */
        public static final Class<? extends GrantQueryService> getGrantQuery()
        {
            EnableSso enableSso = BootInfoHolder.getBootClass().getAnnotation(EnableSso.class);

            return enableSso.grantService();
        }

    }
}
