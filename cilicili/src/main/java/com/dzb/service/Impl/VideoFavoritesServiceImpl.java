package com.dzb.service.Impl;

import com.dzb.dao.VideoFavoritesDao;
import com.dzb.service.VideoFavoritesService;
import com.dzb.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VideoFavoritesServiceImpl implements VideoFavoritesService {

    @Autowired private VideoFavoritesDao videoFavoritesDao;
    @Autowired private VideoService videoService;

    @Override
    public int getVideoFavoritesStateByVidAndUid(int vid, int uid) {
        Map<String,Object> map=new HashMap<>();
        map.put("vid",vid);
        map.put("uid",uid);
        return this.videoFavoritesDao.getVideoFavoritesStateByVidAndUid(map);
    }

    @Override
    public int insertFavoritesByVidAndUid(int vid, int uid) {
        Map<String,Object> map=new HashMap<>();
        map.put("vid",vid);
        map.put("uid",uid);
        return this.videoFavoritesDao.insertFavoritesByVidAndUid(map);
    }

    @Override
    public int deleteFavoritesByVidAndUid(int vid, int uid) {
        Map<String,Object> map=new HashMap<>();
        map.put("vid",vid);
        map.put("uid",uid);
        return this.videoFavoritesDao.deleteFavoritesByVidAndUid(map);
    }

    @Override
    public int deleteFavoritesByVid(Integer vid) {
        return this.videoFavoritesDao.deleteFavoritesByVid(vid);
    }

    @Override
    public List<Integer> getVideoIdByUid(Integer uid) {
        return this.videoFavoritesDao.getVideoIdByUid(uid);
    }

    @Override
    public int deleteFavoritesByUid(Integer uid) {
        return this.videoFavoritesDao.deleteFavoritesByUid(uid);
    }

    @Override
    public List<Integer> getVideoIdListPage(Map<String, Object> map) {
        return this.videoFavoritesDao.getVideoIdListPage(map);
    }

    @Override
    public int getVideoIdListCount(Map<String, Object> map) {
        return this.videoFavoritesDao.getVideoIdListCount(map);
    }


}
