package com.dzb.controller.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VideoFilesDto {
    private boolean isState=false;
    private boolean isVideo=false;
    private boolean isMe=false;
    private boolean isMaster=false;
    private List<Integer> uploadedChunksNumList;
    private String tempVideoPath;
    private String tempCoverPath;
}
