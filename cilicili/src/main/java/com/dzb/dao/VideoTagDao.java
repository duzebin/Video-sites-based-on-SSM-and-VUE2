package com.dzb.dao;

import com.dzb.controller.dto.VideoInfoDto;
import com.dzb.controller.dto.VideoTagInfoDto;

import java.util.List;
import java.util.Map;

public interface VideoTagDao {
    List<VideoTagInfoDto> getVideoTagByVid(int vid);

    void insertVideoTagByTagIdListAndVid(Map<String, Object> map);

    int deleteVideoTagByVid(Integer vid);


    List<Integer> getVideoIdListPageByTid(Map<String, Object> map);

    int getVideoListCountByTid(Map<String, Object> map);
}
