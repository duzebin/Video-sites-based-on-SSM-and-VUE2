package com.dzb.dao;

import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.controller.dto.VideoInfoDto;
import com.dzb.controller.request.VideoLikeOperate;
import com.dzb.controller.request.VideoUpdate;
import com.dzb.entity.Video;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VideoDao {
    List<VideoInfoDto> getAllVideo();

    VideoInfoDto getVideoByVid(int vid);

    List<Video> getEpisodeListByEpisodeId(int episodeId);

    int getVideoUidByVid(Integer vid);

    void updateVideoFavoritesCountByVidAndType(Map<String, Object> map);

    void updateVideoLikeCountByVidAndType(Map<String, Object> map);

    int insertVideoByVideo(Video video);

    Video getLastVideoInfoByUid(int uid);

    List<Video> getUserUploadVideoInfoPageList(Map<String, Object> map);

    int getVideoCountByStateAndUid(Map<String, Object> map1);


    int deleteVideoByVid(Integer vid);

    List<VideoInfoDto> getAllVideoByState(int state);


    int updateVideoByVideo(VideoUpdate videoUpdate);

    int getAllVideoCount(Map<String, Object> map);

    List<VideoInfoDto> getVideoPageList(Map<String, Object> map);

    int updateVideoStateByVid(Map<String, Object> map);

    List<Integer> getVideoIdListByUid(int uid);

    int updateVideoFavoritesByVideoIdList(@Param("videoIdList")List<Integer> videoIdList);

    int updateVideoLikeByVideoIdList(@Param("videoIdList")List<Integer> videoIdList);

    List<VideoInfoDto> getVideoInfoListPageByUid(Map<String, Object> map);

    int getVideoInfoListPageCountByUid(Map<String, Object> map);

    int getEpisodeFirstVideoIdByEpisodeId(int id);

    List<VideoInfoDto> getVideoByVideoIdList(@Param("videoIdList")List<Integer> videoIdList);

    int updateVideoEpisodeByEpisodeId(Integer id);


    List<VideoInfoDto> getConcernUserVideoInfoListPageByUidList(Map<String, Object> map);

    int getConcernUserVideoInfoListCountByUidList(Map<String, Object> map);

    List<VideoInfoDto> getVideoInfoListPage(Map<String, Object> map);

    int getVideoInfoListCount(Map<String, Object> map);

    List<VideoInfoDto> getVideoInfoListPageByPartition(Map<String, Object> map);

    int getVideoInfoListCountByPartition(Map<String, Object> map);


    int getUserVideoAllLikeCount(int uid);


    int getTheVideoForReviewByTime(Date time);

    int getTheVideoForReview();

    List<DateAndCountDto> getVideoUploadDataByTimeRange(String timeRange);

    @MapKey("vid")
    Map<Integer,Map<Integer,String>> getVideoIdAndVideoTitleByVidList(@Param("vIdList") List<Integer> vIdList);

    List<DateAndCountDto> getUserVideoUploadDataByTimeRange(Map<String, Object> map);


}
