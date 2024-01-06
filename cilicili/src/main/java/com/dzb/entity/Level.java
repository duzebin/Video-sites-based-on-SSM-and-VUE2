package com.dzb.entity;

import lombok.Data;

@Data
//用户等级
public class Level {
    //等级
    private int level;
    //升级所需经验
    private int experience;
}
