package com.dzb.controller.dto;

import com.dzb.entity.Audit;
import com.dzb.entity.UserInfo;
import com.dzb.entity.Video;
import lombok.Data;

import java.util.Date;

@Data
public class VideoInfoDto extends Video {
    private UserInfo userInfo;
    private String stateName;
}
