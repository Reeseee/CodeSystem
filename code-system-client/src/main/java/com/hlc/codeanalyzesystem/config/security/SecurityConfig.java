package com.hlc.codeanalyzesystem.config.security;

import com.hlc.codeanalyzesystem.config.security.filter.JwtTokenFilter;
import com.hlc.codeanalyzesystem.entities.Client;
import com.hlc.codeanalyzesystem.entity.ResultJSON;
import com.hlc.codeanalyzesystem.entity.SystemUser;
import com.hlc.codeanalyzesystem.util.SecurityHandlerUtil;
import com.hlc.codeanalyzesystem.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static com.hlc.codeanalyzesystem.util.ResultArgsUtil.*;
import static com.hlc.codeanalyzesystem.util.ResultArgsUtil.USER_NOT_EXIST_FAILURE_MSG;

@Configuration
@EnableWebSecurity
//添加annotation 支持,包括（prePostEnabled，securedEnabled...）
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private TokenUtils tokenUtils;

    /**
     * 注入数据源
     */
    @Resource
    private DataSource dataSource;


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        // 密码加密
        return new BCryptPasswordEncoder();
    }

    /**
     * 验证账号密码
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //指定service
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * 自定义系统登陆页面
     *
     * @param http
     * @throws Exception
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults()).authorizeRequests()
                //对login路径放行，不需要认证。
                .antMatchers("/login").permitAll()
                //除上面配置的路径外，所有的请求都需要进行认证后才能访问
                .anyRequest().authenticated()
                /* 连接 */
                .and()
                //开启formLogin方式认证，另外一种认证方式是httpBasic()
                .formLogin()
                //假如请求没认证，则会转发到 /login 请求中
                //前后端分离项目 /login 可以是返回一个json字符串
                //前后端不分离项目 /login 一般返回的是一个页面
                //（登录页面或是提示用户未登录的页面）
                //.loginPage("/login")
                //处理登录请求的url
                .loginProcessingUrl("/login")
                //认证通过后的事件处理（在这里返回token）
                .successHandler(successHandler())
                //认证失败后的事件处理
                .failureHandler(failureHandler())
                .and()
                //设置登录注销url，这个无需我们开发，springsecurity已帮我们做好
                .logout().logoutUrl("/logout").permitAll()
                .and()
                //设置访问被拒绝后的事件（用来处理权限不足时的返回）
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                //配置 记住我 功能，
                .rememberMe()
                //后面会说明这个persistentTokenRepository
                .tokenRepository(persistentTokenRepository())
                //设置 记住我 的过期时间。
                .tokenValiditySeconds(60*60*24)
                //记住我 功能的用户校验（后面会提到）
                .userDetailsService(userDetailsService)
                .and()
                //关闭跨域请求访问，防止跨域请求伪造
                .csrf().disable();
        //关闭默认的httpBasic()的认证方式
        //到这里，我们上面配的springsecurity账号密码yml就不起作用了
        http.httpBasic().disable();
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    public AuthenticationSuccessHandler successHandler(){
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                SystemUser systemUser = (SystemUser) authentication.getPrincipal();
                Map<String,Object> claims = new HashMap<>();
                claims.put("userId",systemUser.getId().toString());
                String token = tokenUtils.createToken(claims);
                systemUser.setPassword(null);
                SecurityHandlerUtil.responseHandler(httpServletResponse,new ResultJSON(USER_LOGIN_SUCCESS_CODE,token,systemUser));
            }
        };
    }


    public AuthenticationFailureHandler failureHandler(){
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException, IOException {
                SecurityHandlerUtil.responseHandler(httpServletResponse,new ResultJSON(USER_NOT_EXIST_FAILURE_CODE,USER_NOT_EXIST_FAILURE_MSG));
            }
        };
    }

    public AccessDeniedHandler accessDeniedHandler(){
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                String ok = "权限不足";
                writer.println(ok);
                writer.flush();
            }
        };
    }


    @Bean
    public JwtTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtTokenFilter();
    }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/*").permitAll()
//                //.antMatchers("/*").hasRole("scut")
//                .anyRequest().authenticated()
//                .and().cors().and().csrf().disable()
//                .formLogin().loginProcessingUrl("/login").permitAll()
//                .successHandler(new LoginSuccessHandlerImpl())
//                .failureHandler(new LoginFailureHandlerImpl());
//    }



}
