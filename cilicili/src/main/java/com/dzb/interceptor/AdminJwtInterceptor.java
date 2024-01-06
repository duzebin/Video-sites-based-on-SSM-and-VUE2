package com.dzb.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dzb.entity.Admin;
import com.dzb.exception.ServiceException;
import com.dzb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminJwtInterceptor implements HandlerInterceptor {

    private static final String ERROR_CODE_401 = "401";

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }

        if (StrUtil.isBlank(token)) {
            throw new ServiceException(ERROR_CODE_401, "无token，请重新登录");
        }

        String adminAccount;
        Admin admin;
        try {
            adminAccount = JWT.decode(token).getAudience().get(0);
            admin = adminService.getAdminByAccount(adminAccount);

        } catch (Exception e) {
            String errMsg = "token验证失败，请重新登录";
            throw new ServiceException(ERROR_CODE_401, errMsg);
        }
        if (admin == null) {
            throw new ServiceException(ERROR_CODE_401, "用户不存在，请重新登录");
        }
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(admin.getPassword())).build();
            jwtVerifier.verify(token);
            request.removeAttribute("user");
            request.removeAttribute("admin");
            request.setAttribute("admin", admin);
        } catch (JWTVerificationException e) {
            throw new ServiceException(ERROR_CODE_401, "token验证失败，请重新登录");
        }

        return true;
    }
}
