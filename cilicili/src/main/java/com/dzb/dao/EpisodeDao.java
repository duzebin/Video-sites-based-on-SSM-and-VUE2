package com.dzb.dao;

import com.dzb.entity.Episode;

import java.util.List;
import java.util.Map;

public interface EpisodeDao {
    Episode getEpisodeInfoByEpisodeId(int episodeId);

    List<Episode> getUserAllEpisodeByUid(int uid);

    int insertEpisodeByEpisode(Episode episode);

    Episode getEpisodeInfoByEpisode(Episode episode);

    int deleteEpisodeByUid(Integer uid);


    List<Episode> getUserEpisodeListPage(Map<String, Object> map);

    int getUserEpisodeListCount(Map<String, Object> map);

    int deleteEpisodeById(Integer id);
}
