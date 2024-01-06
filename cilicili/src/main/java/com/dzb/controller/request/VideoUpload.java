package com.dzb.controller.request;

import com.dzb.entity.Tag;
import com.dzb.entity.Video;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class VideoUpload extends Video {
    private MultipartFile file;
    private String tempVideoPath;
    private String tempVideoCoverPath;
    private Integer count;
    private String md5;
    private List<String> tagList;
}
