package com.dzb.dao;

import com.dzb.entity.FileChunks;
import com.dzb.entity.VideoFiles;

import java.util.List;
import java.util.Map;

public interface VideoFilesDao {

    void insertVideo(FileChunks fileChunks);

    void updateVideoFilesStateByVideoFile(VideoFiles videoFiles);

    VideoFiles getVideoByMd5AndCount(Map<String,Object> map);


    void updateVideoFilesVidByMd5AndUid(Map<String, Object> map);

    int deleteVideoFileByVid(int vid);

    List<VideoFiles> getVideoFileListByUid(Integer uid);


}
