package com.hlc.codeanalyzesystem.config.security.filter;

import com.hlc.codeanalyzesystem.entities.Client;
import com.hlc.codeanalyzesystem.service.ClientService;
import com.hlc.codeanalyzesystem.util.TokenUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    //这里继承了OncePerRequestFilter过滤器
    private Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    private TokenUtils tokenUtils;//工具类
    @Autowired
    private ClientService clientService;//普通的service

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authorization");//上面配置的自定义标识 Authorization
        if (token != null && token.startsWith("Bearer ")) {//Bearer （有个空格）标识
            //生成的token中带有Bearer 标识，去掉标识后就剩纯粹的token了。
            String substring = token.substring(7);
            //解析token拿到我们生成token的时候存进去的userId
            Claims claims = tokenUtils.parseToken(substring);
            Object userId = claims.get("userId");
            Client user = clientService.selectById(Integer.parseInt(userId.toString()));
            if (user != null){
                //将查询到的用户信息取其账号（登录凭证）以及密码去生成一个Authentication对象
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
                //将Authentication对象放进springsecurity上下文中（进行认证操作）
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        //走下一条过滤器
        chain.doFilter(request,response);
    }
}
