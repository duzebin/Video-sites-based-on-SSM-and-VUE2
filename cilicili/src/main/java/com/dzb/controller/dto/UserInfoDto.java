package com.dzb.controller.dto;

import com.dzb.entity.User;
import com.dzb.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoDto extends UserInfo {
    //token
    private String token;
    //用户状态
    private String state;
    private int stateId;
    //升级所需经验
    private int levelUpExperience;
    //最后登录
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;


}
