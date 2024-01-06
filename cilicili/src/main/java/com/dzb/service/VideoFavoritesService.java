package com.dzb.service;


import java.util.List;
import java.util.Map;

public interface VideoFavoritesService {

    int getVideoFavoritesStateByVidAndUid(int vid, int uid);

    int insertFavoritesByVidAndUid(int vid, int uid);

    int deleteFavoritesByVidAndUid(int vid, int uid);

    int deleteFavoritesByVid(Integer vid);

    List<Integer> getVideoIdByUid(Integer uid);

    int deleteFavoritesByUid(Integer uid);

    List<Integer> getVideoIdListPage(Map<String, Object> map);

    int getVideoIdListCount(Map<String, Object> map);
}
