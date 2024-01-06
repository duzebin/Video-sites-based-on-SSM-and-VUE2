package com.dzb.controller.dto;

import lombok.Data;

@Data
public class AdminDto {
    private int id;
    private String name;
    private int privileges;
    private String privilegesName;
    private String token;
}
