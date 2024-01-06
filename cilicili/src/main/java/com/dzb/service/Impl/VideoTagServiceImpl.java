package com.dzb.service.Impl;

import com.dzb.controller.dto.VideoInfoDto;
import com.dzb.controller.dto.VideoTagInfoDto;
import com.dzb.dao.VideoTagDao;
import com.dzb.service.VideoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VideoTagServiceImpl implements VideoTagService {

    @Autowired private VideoTagDao videoTagDao;
    public List<VideoTagInfoDto> getVideoTagByVid(int vid) {
        return this.videoTagDao.getVideoTagByVid(vid);
    }

    @Override
    public void insertVideoTagByTagIdListAndVid(List<Integer> tagIdList, int vid) {
        if (tagIdList==null||tagIdList.size()==0){
            return;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("tagIdList",tagIdList);
        map.put("vid",vid);
        this.videoTagDao.insertVideoTagByTagIdListAndVid(map);
    }

    @Override
    public int deleteVideoTagByVid(Integer vid) {
        return this.videoTagDao.deleteVideoTagByVid(vid);
    }

    @Override
    public List<Integer> getVideoIdListPageByTid(Map<String, Object> map) {
        return this.videoTagDao.getVideoIdListPageByTid(map);
    }

    @Override
    public int getVideoListCountByTid(Map<String, Object> map) {
        return this.videoTagDao.getVideoListCountByTid(map);
    }


}
