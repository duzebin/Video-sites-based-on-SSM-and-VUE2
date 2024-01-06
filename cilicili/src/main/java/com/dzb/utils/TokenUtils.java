package com.dzb.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dzb.entity.Admin;
import com.dzb.entity.User;
import com.dzb.service.AdminService;
import com.dzb.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {

    private static UserService staticUserService;
    private static AdminService staticAdminService;
    @Resource private UserService userService;
    @Resource private AdminService adminService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    @PostConstruct
    public void setAdminService() {
        staticAdminService = adminService;
    }

    public static String getToken(String account, String password) {
        return JWT.create().withAudience(account)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(password));
    }


    public static User getCurrentUser() {
        String token = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                return null;
            }
            String userAccount = JWT.decode(token).getAudience().get(0);
            return staticUserService.getUserByAccount(userAccount);
        } catch (Exception e) {
            return null;
        }
    }

    public static Admin getCurrentAdmin() {
        String token = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                return null;
            }
            String userAccount = JWT.decode(token).getAudience().get(0);
            return staticAdminService.getAdminByAccount(userAccount);
        } catch (Exception e) {
            return null;
        }
    }
}
