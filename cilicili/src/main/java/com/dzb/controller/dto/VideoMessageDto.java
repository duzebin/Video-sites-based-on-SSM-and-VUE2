package com.dzb.controller.dto;

import com.dzb.entity.VideoMessage;
import lombok.Data;

@Data
public class VideoMessageDto extends VideoMessage {
    private String videoTitle;
    private String stateName;
}
