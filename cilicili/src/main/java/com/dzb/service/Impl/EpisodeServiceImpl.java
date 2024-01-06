package com.dzb.service.Impl;

import com.dzb.common.RequestInfo;
import com.dzb.controller.request.EpisodeListPage;
import com.dzb.dao.EpisodeDao;
import com.dzb.entity.Episode;
import com.dzb.exception.ServiceException;
import com.dzb.service.EpisodeService;
import com.dzb.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EpisodeServiceImpl implements EpisodeService {
    @Autowired private EpisodeDao episodeDao;
    @Autowired private VideoService videoService;


    public Episode getEpisodeInfoByEpisodeId(int episodeId) {
        return this.episodeDao.getEpisodeInfoByEpisodeId(episodeId);
    }

    @Override
    public List<Episode> getUserAllEpisode() {
        List<Episode> episodeList =this.episodeDao.getUserAllEpisodeByUid(RequestInfo.getUser().getUid());
        if (episodeList==null){
            return new ArrayList<>();
        }
        return episodeList;
    }

    @Override
    public Episode insertEpisodeByEpisode(Episode episode) {
        episode.setUid(RequestInfo.getUser().getUid());
        if (this.episodeDao.getEpisodeInfoByEpisode(episode)!=null){
            throw new ServiceException("已有相同名称的合集！");
        }
        if (0==this.episodeDao.insertEpisodeByEpisode(episode)){
            throw new ServiceException("新增合集错误！");
        }
        return this.episodeDao.getEpisodeInfoByEpisode(episode);
    }

    @Override
    public int deleteEpisodeByUid(Integer uid) {
        return this.episodeDao.deleteEpisodeByUid(uid);
    }

    @Override
    public Map<String, Object> getEpisodeListPage(EpisodeListPage episodeListPage) {
        Map<String,Object> map=new HashMap<>();
        map.put("pageStart", (episodeListPage.getPageNum() - 1) * episodeListPage.getPageSize());
        map.put("pageSize", episodeListPage.getPageSize());
        map.put("uid",RequestInfo.getUser().getUid());

        Map<String,Object> retMap =new HashMap<>();
        retMap.put("episodeList",this.episodeDao.getUserEpisodeListPage(map));
        retMap.put("episodeCount",this.episodeDao.getUserEpisodeListCount(map));
        return retMap;
    }

    @Override
    public boolean deleteEpisodeById(Integer id) {
        this.videoService.updateVideoEpisodeByEpisodeId(id);
        this.episodeDao.deleteEpisodeById(id);
        return true;
    }


}
