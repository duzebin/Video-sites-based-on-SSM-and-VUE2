package com.dzb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
//视频分集数据
public class Episode {
    //分集id
    private int id;
    //分集所有人
    private int uid;
    //分集名
    private String ename;
    //分集创建时间
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date crtime;
}
