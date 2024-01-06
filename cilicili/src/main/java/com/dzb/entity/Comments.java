package com.dzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Comments {
    private int id;
    private int vid;
    private int uid;
    private String text;
    private int adhere;
    private int like;
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date uptime;
    private int state;
}
