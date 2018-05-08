package com.liusl.util;



/**
 * @auther liusl12
 * @date 2018/5/8.
 */
public interface JwtBaseUtil {
    /**
     * Jwt创建token
     * @return token
     */
    public String createJwtToken();

    /**
     * 认证token是否合法
     */
    public String verifyAccessToken(String token);

    /**
     * 重新生成token
     * @return
     */
    public String reGenerateToken();

    /**
     * 使token失效
     */
    public void destoryToken(String token);
}
