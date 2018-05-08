package com.liusl.util.impl;

import com.liusl.config.SecuritySetting;
import com.liusl.util.JwtBaseUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * @auther liusl12
 * @date 2018/5/8.
 */
@Component
public final class JwtBaseUtilImpl implements JwtBaseUtil{
    @Autowired
    private SecuritySetting securitySetting;

    @Override
    public String createJwtToken(){
//        Key key = MacProvider.generateKey();    //随机产生一个signing key
        return Jwts.builder().setHeaderParam("typ","JWT").setSubject("woaijiaban").claim("name","liusl").signWith(SignatureAlgorithm.HS256,securitySetting.getSecurityToken()).compact();     //用Jwt方法产生一个token，签名算法用HS256，可以使用其他算法
    }
    /**
     * 认证token是否合法
     */
    @Override
    public String verifyAccessToken(String token){
        try{
            Jwts.parser().setSigningKey(securitySetting.getSecurityToken()).parseClaimsJws("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3b2FpamlhYmFuIiwibmFtZSI6ImxpdXNsIn0.qqfMrX9J93IPTX3wGJHKliiXhvHwE85lEQl_vVZ3FRQ");
            return "Success";
        }
        catch (SignatureException e){
            return "Failture";
        }
    };

    /**
     * 重新生成token
     * @return
     */
    @Override
    public String reGenerateToken(){
        return "1";
    };

    /**
     * 使token失效
     */
    @Override
    public void destoryToken(String token){

    };
}
