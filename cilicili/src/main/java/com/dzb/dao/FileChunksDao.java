package com.dzb.dao;

import com.dzb.entity.FileChunks;

import java.util.List;
import java.util.Map;

public interface FileChunksDao {
    List<Integer> getChunksNumListByMd5(String md5);

    int insertFileChunksInfo(FileChunks fileChunks);

    List<FileChunks> getFileChunkListByMd5(String md5);

    int deleteFileChunkInfo(String md5);
}
