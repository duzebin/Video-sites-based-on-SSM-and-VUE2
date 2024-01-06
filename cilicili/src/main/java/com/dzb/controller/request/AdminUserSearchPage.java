package com.dzb.controller.request;

import lombok.Data;

@Data
public class AdminUserSearchPage extends BasePage{
    private int uid;
    private String uname;

}
