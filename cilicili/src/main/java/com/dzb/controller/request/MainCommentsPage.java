package com.dzb.controller.request;

import lombok.Data;

@Data
public class MainCommentsPage extends BasePage{
    private int childPageSize=10;
    private int vid;
    private String sort;
}
