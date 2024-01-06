package com.dzb.service;

import com.dzb.controller.request.VideoLikeOperate;

import java.util.List;

public interface VideoLikeService {



    int getVideoLikeStateByVidAndUid(int vid, int uid);


    int insertVideoLikeByVidAndUid(int vid, int uid);

    int deleteVideoLikeByVidAndUid(int vid, int uid);

    int deleteVideoLikeByVid(Integer vid);

    List<Integer> getVideoIdByUid(Integer uid);

    int deleteVideoLikeByUid(Integer uid);
}
