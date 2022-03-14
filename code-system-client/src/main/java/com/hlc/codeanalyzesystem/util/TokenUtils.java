package com.hlc.codeanalyzesystem.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Lazy
public class TokenUtils implements Serializable {

    private static final long serialVersionUID = -5625635588908941275L;

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    /**
     * 获取token
     * @param claims
     * @return
     */
    public String createToken(Map<String, Object> claims){
        long now = System.currentTimeMillis()+(expireTime * 60 * 1000);
        return Jwts.builder()
                .setIssuer("hlc")
                .addClaims(claims)
                .setExpiration(new Date(now))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public Claims parseToken(String token){
        JwtParser jwtParser = Jwts.parser().setSigningKey(secret);
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body;
    }
}
