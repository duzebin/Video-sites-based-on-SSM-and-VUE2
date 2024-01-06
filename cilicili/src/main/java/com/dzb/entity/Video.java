package com.dzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
//视频信息
public class Video {
    //视频id
    private int vid;
    //视频所有者用户信息
    private int uid;
    //视频标题
    private String title;
    //视频简介
    private String synopsis;
    //视频封面链接
    private String cover;
    //视频链接
    private String src;
    //播放量
    private int play;
    //点赞数
    private int like;
    //收藏数
    private int favorites;
    //上传时间
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date uptime;
    //隶属分区
    private String partition;
    //隶属分集
    private int episode;
    //视频状态
    private int state;
}
