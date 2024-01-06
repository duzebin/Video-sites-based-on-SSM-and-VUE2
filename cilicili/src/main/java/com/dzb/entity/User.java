package com.dzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
//用户账户信息
public class User {
    //用户id
    private int uid;
    //账号
    private String account;
    //密码
    private String password;
    //用户状态
    private int state;
    //用户最后登录时间
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;
}
