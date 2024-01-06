package com.dzb.service;

import com.dzb.controller.dto.VideoInfoDto;
import com.dzb.controller.dto.VideoTagInfoDto;

import java.util.List;
import java.util.Map;

public interface VideoTagService {

    List<VideoTagInfoDto> getVideoTagByVid(int vid);

    void insertVideoTagByTagIdListAndVid(List<Integer> tagList, int vid);

    int deleteVideoTagByVid(Integer vid);


    List<Integer> getVideoIdListPageByTid(Map<String, Object> map);

    int getVideoListCountByTid(Map<String, Object> map);
}
