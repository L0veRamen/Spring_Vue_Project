package com.jc666.springboot.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jc666.springboot.entity.User;
import com.jc666.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class TokenUtils {

    private static IUserService staticUserService;

    @Resource
    @Qualifier("userServiceImpl")
    private IUserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    /**
     * generate JWT (JSON web Token)
     */
    public static String genToken(String userId, String sign) {
        return JWT.create().withAudience(userId) // set user id to token as parameter
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2 hour token expire
                .sign(Algorithm.HMAC256(sign)); // set password as token password
    }

    /**
     * get current logIn user info
     *
     * @return User
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
