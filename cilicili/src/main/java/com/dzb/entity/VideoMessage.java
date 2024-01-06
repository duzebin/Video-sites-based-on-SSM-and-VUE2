package com.dzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class VideoMessage {
    private int id;
    private int aid;
    private int uid;
    private int vid;
    private String msg;
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date time;
    private int state;
}
