package com.dzb.service.Impl;

import com.dzb.common.RequestInfo;
import com.dzb.controller.request.VideoLikeOperate;
import com.dzb.dao.VideoLikeDao;
import com.dzb.exception.ServiceException;
import com.dzb.service.VideoLikeService;
import com.dzb.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VideoLikeServiceImpl implements VideoLikeService {
    @Autowired private VideoLikeDao videoLikeDao;

    @Override
    public int getVideoLikeStateByVidAndUid(int vid, int uid) {
        Map<String,Object> map=new HashMap<>();
        map.put("vid",vid);
        map.put("uid",uid);
        return this.videoLikeDao.getVideoLikeStateByVidAndUid(map);
    }

    @Override
    public int insertVideoLikeByVidAndUid(int vid, int uid) {
        Map<String,Object> map=new HashMap<>();
        map.put("vid",vid);
        map.put("uid",uid);
        return this.videoLikeDao.insertVideoLikeByVidAndUid(map);
    }

    @Override
    public int deleteVideoLikeByVidAndUid(int vid, int uid) {
        Map<String,Object> map=new HashMap<>();
        map.put("vid",vid);
        map.put("uid",uid);
        return this.videoLikeDao.deleteVideoLikeByVidAndUid(map);
    }

    @Override
    public int deleteVideoLikeByVid(Integer vid) {
        return this.videoLikeDao.deleteVideoLikeByVid(vid);
    }

    @Override
    public List<Integer> getVideoIdByUid(Integer uid) {
        return this.videoLikeDao.getVideoIdByUid(uid);
    }

    @Override
    public int deleteVideoLikeByUid(Integer uid) {
        return this.videoLikeDao.deleteVideoLikeByUid(uid);
    }


}
