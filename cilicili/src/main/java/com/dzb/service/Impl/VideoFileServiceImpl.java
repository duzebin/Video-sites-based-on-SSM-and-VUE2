package com.dzb.service.Impl;

import com.dzb.dao.VideoFilesDao;
import com.dzb.entity.FileChunks;
import com.dzb.entity.VideoFiles;
import com.dzb.service.VideoFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VideoFileServiceImpl implements VideoFilesService {

    @Autowired private VideoFilesDao videoFilesDao;

    @Override
    public void insertVideo(FileChunks fileChunks) {
        this.videoFilesDao.insertVideo(fileChunks);
    }

    @Override
    public void updateVideoFilesStateByVideoFile(VideoFiles videoFiles) {
        this.videoFilesDao.updateVideoFilesStateByVideoFile(videoFiles);
    }

    @Override
    public VideoFiles getVideoByMd5AndCount(String md5, int count) {
        Map<String,Object> map =new HashMap<>();
        map.put("md5",md5);
        map.put("count",count);
        return this.videoFilesDao.getVideoByMd5AndCount(map);
    }

    @Override
    public void updateVideoFilesVidByMd5AndUid(String md5, int uid,int vid) {
        Map<String,Object> map = new HashMap<>();
        map.put("md5",md5);
        map.put("uid",uid);
        map.put("vid",vid);
        this.videoFilesDao.updateVideoFilesVidByMd5AndUid(map);
    }

    @Override
    public int deleteVideoFileByVid(int vid) {
        return this.videoFilesDao.deleteVideoFileByVid(vid);
    }

    @Override
    public List<VideoFiles> getVideoFileListByUid(Integer uid) {
        return this.videoFilesDao.getVideoFileListByUid(uid);
    }




}
