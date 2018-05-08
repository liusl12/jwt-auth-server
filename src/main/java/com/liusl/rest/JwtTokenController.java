package com.liusl.rest;

import com.liusl.util.JwtBaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther liusl12
 * @date 2018/5/8.
 */
@RestController
@RequestMapping("/access")
public class JwtTokenController {
    @Autowired
    private JwtBaseUtil jwtBaseUtil;

    @GetMapping("/token")
    public String getAccessToken(){
        return jwtBaseUtil.createJwtToken();
    }
    @GetMapping("/verify")
    public String verifyAccessToken(){
        return jwtBaseUtil.verifyAccessToken("token");
    }
}
