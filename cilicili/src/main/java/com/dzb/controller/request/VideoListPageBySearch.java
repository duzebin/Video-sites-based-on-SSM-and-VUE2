package com.dzb.controller.request;

import lombok.Data;

@Data
public class VideoListPageBySearch extends BasePage{
    private String name;
    private String sort;
    private String type;
    private String typeName;
}
