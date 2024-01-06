package com.dzb.service;

import com.dzb.entity.FileChunks;
import com.dzb.entity.VideoFiles;

import java.util.List;
import java.util.Map;

public interface VideoFilesService {

    void insertVideo(FileChunks fileChunks);

    void updateVideoFilesStateByVideoFile(VideoFiles videoFiles);


    VideoFiles getVideoByMd5AndCount(String md5, int count);

    void updateVideoFilesVidByMd5AndUid(String md5, int uid,int vid);

    int deleteVideoFileByVid(int vid);


    List<VideoFiles> getVideoFileListByUid(Integer uid);


}
