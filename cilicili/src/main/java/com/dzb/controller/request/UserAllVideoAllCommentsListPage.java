package com.dzb.controller.request;

import lombok.Data;

@Data
public class UserAllVideoAllCommentsListPage extends BasePage{
    private int vid;
    private int uid;
    private String text;
}
