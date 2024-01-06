package com.dzb.common;

import com.dzb.entity.Admin;
import com.dzb.entity.User;
import com.dzb.exception.ServiceException;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Data
public class RequestInfo {

    private static final String ERROR_CODE_401 = "401";

    public static User getUser(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User user= (User) request.getAttribute("user");
        return user;
    }

    public static Admin getAdmin(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Admin admin= (Admin) request.getAttribute("admin");
        return admin;
    }

    public static void judgeUid(int uid){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User user= (User) request.getAttribute("user");
        Admin admin= (Admin) request.getAttribute("admin");
        if (admin!=null){
            return;
        }
        if (user.getUid()!=uid){
            throw new ServiceException(ERROR_CODE_401,"当前用户信息错误请重新登录");
        }
    }

    public static void judgeUid(int uid1,int uid2){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        User user= (User) request.getAttribute("user");
        Admin admin= (Admin) request.getAttribute("admin");
        if (admin!=null){
            return;
        }
        if (user.getUid()!=uid1&&user.getUid()!=uid2){
            throw new ServiceException(ERROR_CODE_401,"当前用户信息错误请重新登录");
        }
    }
    public static void judgePrivileges(List<Integer> privilegesIdList){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Admin admin= (Admin) request.getAttribute("admin");
        boolean flag=false;
        for (int i=0;i< privilegesIdList.size();i++){
            if (privilegesIdList.get(i)==admin.getPrivileges()){
                flag=true;
                break;
            }
        }
        if (!flag){
            throw new ServiceException("权限不足！！！");
        }
    }
}
