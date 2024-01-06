package com.dzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Notice {
    private int id;
    private int aid;
    private String msg;
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date uptime;
    private String title;
}
