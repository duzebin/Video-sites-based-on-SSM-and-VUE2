package com.dzb.controller.request;

import com.dzb.controller.dto.VideoTagInfoDto;
import com.dzb.entity.Video;
import com.dzb.entity.VideoTag;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class VideoUpdate extends Video {
    private MultipartFile file;
    private List<String> tagNameList;
    private int state;

}
