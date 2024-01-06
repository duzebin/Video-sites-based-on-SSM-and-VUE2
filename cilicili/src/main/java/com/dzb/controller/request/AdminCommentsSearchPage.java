package com.dzb.controller.request;

import lombok.Data;

@Data
public class AdminCommentsSearchPage extends BasePage{
    private Integer uid;
    private Integer vid;
    private Integer state;
    private String text;
}
