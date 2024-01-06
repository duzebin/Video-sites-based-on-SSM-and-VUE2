package com.dzb.service;

import com.dzb.controller.request.EpisodeListPage;
import com.dzb.entity.Episode;

import java.util.List;
import java.util.Map;

public interface EpisodeService {

    Episode getEpisodeInfoByEpisodeId(int episodeId);

    List<Episode> getUserAllEpisode();

    Episode insertEpisodeByEpisode(Episode episode);

    int deleteEpisodeByUid(Integer uid);


    Map<String,Object> getEpisodeListPage(EpisodeListPage episodeListPage);

    boolean deleteEpisodeById(Integer id);
}
