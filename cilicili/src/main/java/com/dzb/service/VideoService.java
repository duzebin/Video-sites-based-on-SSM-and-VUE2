package com.dzb.service;

import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.controller.dto.VideoFilesDto;
import com.dzb.controller.dto.VideoInfoDto;
import com.dzb.controller.dto.VideoTagInfoDto;
import com.dzb.controller.request.*;
import com.dzb.entity.Video;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VideoService {
    List<VideoInfoDto> getAllVideo();

    VideoInfoDto getVideoByVid(int vid);

    List<VideoTagInfoDto> getVideoTagByVid(int vid);

    List<Video> getEpisodeListByEpisodeId(int episodeId);

    int getVideoUidByVid(Integer vid);

    boolean getVideoFavoritesStateByVid(int vid);

    int updateVideoFavoritesByFavoritesOperate(VideoFavoritesOperate videoFavoritesOperate);

    boolean getVideoLikeStateByVid(int vid);

    int updateVideoLikeByVideoLikeOperate(VideoLikeOperate videoLikeOperate);


    VideoFilesDto getVideoByMd5AndCount(String md5,int count);

    boolean uploadVideoChunkFile(FileChunksInfo fileChunksInfo);

    Map<String,Object> uploadFileChunkMergeByMd5(String md5);

    boolean uploadVideo(VideoUpload videoUpload);

    Map<String,Object> getUserUploadVideoInfoPageList(UserUploadVideoInfoPage userUploadVideoInfoPage);

    Map<String,Object> getUserAllVideoStateCount();

    boolean deleteVideoByVid(Integer vid);

    VideoInfoDto getVideoDetailByVid(int vid);

    boolean updateVideo(VideoUpdate videoUpdate);

    int getAllVideoCount(AdminVideoSearchPage adminVideoSearchPage);

    List<VideoInfoDto> getVideoPageList(AdminVideoSearchPage adminVideoSearchPage);

    boolean updateVideoStateByVid(Map<String,Object> map);

    VideoInfoDto adminGetVideoInfoByVid(int vid);

    boolean videoComplaint(VideoComplaint videoComplaint);

    List<Integer> getVideoIdListByUid(int uid);

    int updateVideoFavoritesByVideoIdList(List<Integer> videoIdList);

    int updateVideoLikeByVideoIdList(List<Integer> videoIdList);

    Map<String,Object> getVideoInfoListPageByUid(VideoInfoListPage videoInfoListPage);

    int getEpisodeFirstVideoIdByEpisodeId(int id);

    Map<String,Object> getVideoInfoListPageByCollection(CollectionListPage collectionListPage);

    void updateVideoEpisodeByEpisodeId(Integer id);

    Map<String,Object> getConcernUserVideoInfoListPageByUid(ConcernUserListPage concernUserListPage);

    Map<String,Object> getVideoInfoListPage(VideoListPageBySearch videoListPageBySearch);

    Map<String,Object> getVideoInfoListPageByPartition(VideoListPageBySearch videoListPageBySearch);

    Map<String,Object> getVideoInfoListPageByVideoTag(VideoListPageBySearch videoListPageBySearch);

    int getUserVideoAllLikeCount();

    int getTheVideoForReviewByTime(Date time);

    int getTheVideoForReview();

    List<DateAndCountDto> getVideoUploadDataByTimeRange(String timeRange);

    Map<String,Object> getVideoMessageListPageByUid(VideoMessageListPage videoMessageListPage);

    Map<String,Object> getUserVideoStatisticsByTimeRange(String timeRange);
}
