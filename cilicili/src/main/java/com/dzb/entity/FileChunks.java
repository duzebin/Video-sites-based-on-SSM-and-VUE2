package com.dzb.entity;

import lombok.Data;

@Data
public class FileChunks {
    private int uid;
    private String md5;
    private int num;
    private int count;
}
