package com.dzb.dao;

import com.dzb.controller.request.VideoLikeOperate;

import java.util.List;
import java.util.Map;

public interface VideoLikeDao {

    int getVideoLikeStateByVidAndUid(Map<String, Object> map);

    int deleteVideoLikeByVidAndUid(Map<String, Object> map);

    int insertVideoLikeByVidAndUid(Map<String, Object> map);

    int deleteVideoLikeByVid(Integer vid);

    List<Integer> getVideoIdByUid(Integer uid);

    int deleteVideoLikeByUid(Integer uid);
}
