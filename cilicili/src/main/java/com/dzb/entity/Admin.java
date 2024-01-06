package com.dzb.entity;

import lombok.Data;

@Data
public class Admin {
    private int id;
    private String name;
    private String account;
    private String password;
    private int privileges;
}
