package com.dzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
//用户信息
public class UserInfo {
    //用户uid
    private int uid;
    //用户名
    private String uname;
    //用户头像链接
    private String avatar;
    //用户当前经验值
    private int experience;
    //用户等级
    private int level;
    //用户个人介绍
    private String describes;
    //用户是否vip
    private boolean vip;
    //vip到期时间
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date vipEndTime;
    //用户关注数
    private int concerns;
    //用户粉丝数
    private int fans;

}
