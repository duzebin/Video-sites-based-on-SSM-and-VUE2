package com.dzb.service;


import com.dzb.controller.dto.VideoFilesDto;
import com.dzb.entity.FileChunks;
import com.dzb.entity.UserInfo;
import com.dzb.entity.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileChunksService {
    String upAvatar(MultipartFile file, String uid);

    List<Integer> getChunksNumListByMd5(String md5);

    boolean uploadVideoChunkFile(MultipartFile file, String md5, int num);


    int insertFileChunksInfo(FileChunks fileChunks);

    List<FileChunks> getFileChunkListByMd5(String md5);

    Map<String, Object> uploadFileChunkMergeByMd5AndUid(String md5, int uid);


    void deleteFileChunkInfo(String md5);

    VideoFilesDto getTempVideoInfoByMd5AndUid(String md5, int uid);

    String moveTempVideoCoverToVideoCoverPath(String tempVideoCoverPath, int uid, String md5);

    String uploadVideoCover(MultipartFile file, int uid, String md5);

    String moveTempVideoToVideoPath(String tempVideoPath, int uid, String md5);

    void deleteVideoFileByVideo(Video video);

    String updateVideoCover(MultipartFile file, String filePath, int uid);

    boolean deleteVideoFileByUid(Integer uid);

    boolean deleteUserAvatar(Integer uid);

    String reviseAvatar(MultipartFile file, UserInfo userInfo);
}
