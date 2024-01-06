package com.dzb.dao;

import java.util.List;
import java.util.Map;

public interface VideoFavoritesDao {

    int getVideoFavoritesStateByVidAndUid(Map<String, Object> map);

    int insertFavoritesByVidAndUid(Map<String, Object> map);

    int deleteFavoritesByVidAndUid(Map<String, Object> map);

    int deleteFavoritesByVid(Integer vid);

    List<Integer> getVideoIdByUid(Integer uid);

    int deleteFavoritesByUid(Integer uid);

    List<Integer> getVideoIdListPage(Map<String, Object> map);

    int getVideoIdListCount(Map<String, Object> map);
}
