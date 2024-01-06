package com.dzb.controller.dto;

import com.dzb.entity.Tag;
import com.dzb.entity.VideoTag;
import lombok.Data;

@Data
public class VideoTagInfoDto extends VideoTag {
    private Tag tag;
}
