package com.dzb.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.dzb.common.RequestInfo;
import com.dzb.controller.dto.*;
import com.dzb.controller.request.*;
import com.dzb.dao.VideoDao;
import com.dzb.entity.*;
import com.dzb.exception.ServiceException;
import com.dzb.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private VideoTagService videoTagService;
    @Autowired
    private VideoFavoritesService videoFavoritesService;
    @Autowired
    private VideoLikeService videoLikeService;
    @Autowired
    private VideoFilesService videoFilesService;
    @Autowired
    private FileChunksService fileChunksService;
    @Autowired
    private TagService tagService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private UserService userService;
    @Autowired
    private VideoMessageService videoMessageService;

    @Override
    public List<VideoInfoDto> getAllVideo() {
        return this.videoDao.getAllVideoByState(1);
    }


    private List<String> dateTimeToDateString(List<DateTime> dateTimeList) {
        List<String> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(dateTimeList)) {
            return list;
        }
        for (DateTime dateTime : dateTimeList) {
            String date = DateUtil.formatDate(dateTime);
            list.add(date);
        }
        return list;
    }

    private List<Integer> countList(List<DateAndCountDto> dateAndCountDtoList, List<String> dateStringRange) {
        List<Integer> retList = new ArrayList<>();
        if (dateAndCountDtoList.size() == 0) {
            return retList;
        }
        for (String s : dateStringRange) {
            boolean flag=true;
            for (DateAndCountDto dateAndCountDto : dateAndCountDtoList) {
                if (s.equals(dateAndCountDto.getDate())) {
                    retList.add(dateAndCountDto.getCount());
                    flag=false;
                    break;
                }
            }
            if (flag){
                retList.add(0);
            }
        }
        return retList;
    }
    @Override
    public VideoInfoDto getVideoByVid(int vid) {
        VideoInfoDto videoInfoDto = this.videoDao.getVideoByVid(vid);
        if (videoInfoDto.getState() != 1) {
            throw new ServiceException("不能查看未审核视频,请先登录");
        }
        return videoInfoDto;
    }

    @Override
    public List<VideoTagInfoDto> getVideoTagByVid(int vid) {
        return this.videoTagService.getVideoTagByVid(vid);
    }

    public List<Video> getEpisodeListByEpisodeId(int episodeId) {
        List<Video> v = this.videoDao.getEpisodeListByEpisodeId(episodeId);
        return v;
    }


    @Override
    public int getVideoUidByVid(Integer vid) {
        return this.videoDao.getVideoUidByVid(vid);
    }

    @Override
    public boolean getVideoFavoritesStateByVid(int vid) {
        return 0 != this.videoFavoritesService.getVideoFavoritesStateByVidAndUid(vid, RequestInfo.getUser().getUid());
    }

    @Override
    public int updateVideoFavoritesByFavoritesOperate(VideoFavoritesOperate videoFavoritesOperate) {
        int x = this.videoFavoritesService.getVideoFavoritesStateByVidAndUid(videoFavoritesOperate.getVid(), RequestInfo.getUser().getUid());
        int n = 0;
        if ("add".equals(videoFavoritesOperate.getType()) && x == 0) {
            n = this.videoFavoritesService.insertFavoritesByVidAndUid(videoFavoritesOperate.getVid(), RequestInfo.getUser().getUid());
        } else if ("cancel".equals(videoFavoritesOperate.getType()) && x != 0) {
            n = this.videoFavoritesService.deleteFavoritesByVidAndUid(videoFavoritesOperate.getVid(), RequestInfo.getUser().getUid());
        } else {
            throw new ServiceException("数据错误");
        }
        if (n != 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("vid", videoFavoritesOperate.getVid());
            map.put("type", videoFavoritesOperate.getType());
            this.videoDao.updateVideoFavoritesCountByVidAndType(map);
        }
        return n;
    }

    @Override
    public boolean getVideoLikeStateByVid(int vid) {
        return 0 != this.videoLikeService.getVideoLikeStateByVidAndUid(vid, RequestInfo.getUser().getUid());
    }

    @Override
    public int updateVideoLikeByVideoLikeOperate(VideoLikeOperate videoLikeOperate) {
        int x = this.videoLikeService.getVideoLikeStateByVidAndUid(videoLikeOperate.getVid(), RequestInfo.getUser().getUid());
        int n = 0;
        if ("add".equals(videoLikeOperate.getType()) && x == 0) {
            n = this.videoLikeService.insertVideoLikeByVidAndUid(videoLikeOperate.getVid(), RequestInfo.getUser().getUid());
        } else if ("cancel".equals(videoLikeOperate.getType()) && x != 0) {
            n = this.videoLikeService.deleteVideoLikeByVidAndUid(videoLikeOperate.getVid(), RequestInfo.getUser().getUid());
        } else {
            throw new ServiceException("数据错误");
        }
        if (n != 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("vid", videoLikeOperate.getVid());
            map.put("type", videoLikeOperate.getType());
            this.videoDao.updateVideoLikeCountByVidAndType(map);
        }
        return n;
    }

    @Override
    public VideoFilesDto getVideoByMd5AndCount(String md5, int count) {
        VideoFilesDto videoFilesDto = new VideoFilesDto();
        VideoFiles videoFiles = this.videoFilesService.getVideoByMd5AndCount(md5, count);
        if (videoFiles == null) {
            return videoFilesDto;
        }
        videoFilesDto.setState(true);
        if (videoFiles.getUid() != RequestInfo.getUser().getUid()) {
            return videoFilesDto;
        }
        videoFilesDto.setMe(true);
        if (videoFiles.getState() != 1) {
            videoFilesDto.setUploadedChunksNumList(this.fileChunksService.getChunksNumListByMd5(md5));
            return videoFilesDto;
        }
        videoFilesDto.setVideo(true);
        if (videoFiles.getVid() != 0) {
            videoFilesDto.setMaster(true);
            return videoFilesDto;
        }
        if (RequestInfo.getUser().getUid() == videoFiles.getUid()) {
            VideoFilesDto videoFilesDto1 = this.fileChunksService.getTempVideoInfoByMd5AndUid(md5, RequestInfo.getUser().getUid());
            videoFilesDto.setTempVideoPath(videoFilesDto1.getTempVideoPath());
            videoFilesDto.setTempCoverPath(videoFilesDto1.getTempCoverPath());
        }
        return videoFilesDto;
    }

    @Override
    public boolean uploadVideoChunkFile(FileChunksInfo fileChunksInfo) {
        this.fileChunksService.uploadVideoChunkFile(fileChunksInfo.getFile(), fileChunksInfo.getMd5(), fileChunksInfo.getNum());
        FileChunks fileChunks = new FileChunks();
        try {
            BeanUtils.copyProperties(fileChunks, fileChunksInfo);
            fileChunks.setUid(RequestInfo.getUser().getUid());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if (fileChunksInfo.getNum() == 1 && this.videoFilesService.getVideoByMd5AndCount(fileChunksInfo.getMd5(), fileChunks.getCount()) == null) {
            this.videoFilesService.insertVideo(fileChunks);
        }
        this.fileChunksService.insertFileChunksInfo(fileChunks);
        return true;
    }

    @Override
    public Map<String, Object> uploadFileChunkMergeByMd5(String md5) {
        List<FileChunks> fileChunksList = this.fileChunksService.getFileChunkListByMd5(md5);
        if (fileChunksList != null) {
            if (fileChunksList.get(0).getCount() == fileChunksList.size()) {
                return this.fileChunksService.uploadFileChunkMergeByMd5AndUid(md5, RequestInfo.getUser().getUid());
            }
        }
        return null;
    }

    @Override
    public boolean uploadVideo(VideoUpload videoUpload) {
        User user = RequestInfo.getUser();
        VideoFiles videoFiles = this.videoFilesService.getVideoByMd5AndCount(videoUpload.getMd5(), videoUpload.getCount());
        if (videoFiles != null && videoFiles.getVid() != 0) {
            throw new ServiceException("当前视频已经上传");
        }
        if (ObjectUtils.isEmpty(videoUpload.getFile()) || videoUpload.getFile().getSize() <= 0) {
            videoUpload.setCover(this.fileChunksService.moveTempVideoCoverToVideoCoverPath(videoUpload.getTempVideoCoverPath(), user.getUid(), videoUpload.getMd5()));
        } else {
            videoUpload.setCover(this.fileChunksService.uploadVideoCover(videoUpload.getFile(), user.getUid(), videoUpload.getMd5()));
        }
        if (videoUpload.getCover() == null) {
            throw new ServiceException("上传视频失败，请联系管理员");
        }
        videoUpload.setUid(user.getUid());
        videoUpload.setSrc(this.fileChunksService.moveTempVideoToVideoPath(videoUpload.getTempVideoPath(), user.getUid(), videoUpload.getMd5()));
        videoUpload.setUptime(new Date());
        this.videoDao.insertVideoByVideo(videoUpload);
        Video currentVideo = this.videoDao.getLastVideoInfoByUid(user.getUid());
        this.videoFilesService.updateVideoFilesVidByMd5AndUid(videoUpload.getMd5(), user.getUid(), currentVideo.getVid());
        List<Integer> tagIdList = this.tagService.insertAndGetTagIdByTagList(videoUpload.getTagList());
        if (tagIdList.size() > 0) {
            this.videoTagService.insertVideoTagByTagIdListAndVid(tagIdList, currentVideo.getVid());
        }
        return true;
    }

    @Override
    public Map<String, Object> getUserUploadVideoInfoPageList(UserUploadVideoInfoPage userUploadVideoInfoPage) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", RequestInfo.getUser().getUid());
        map.put("pageStart", (userUploadVideoInfoPage.getPageNum() - 1) * userUploadVideoInfoPage.getPageSize());
        map.put("pageSize", userUploadVideoInfoPage.getPageSize());
        map.put("state", userUploadVideoInfoPage.getState());
        Map<String, Object> retmap = new HashMap<>();
        retmap.put("videoList", this.videoDao.getUserUploadVideoInfoPageList(map));
        retmap.put("count", this.videoDao.getVideoCountByStateAndUid(map));
        return retmap;
    }

    @Override
    public Map<String, Object> getUserAllVideoStateCount() {
        Map<String, Object> map = new HashMap<>();
        List<Audit> auditList = this.auditService.getAllAudit();
        for (int i = 0; i < auditList.size(); i++) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("state", auditList.get(i).getId());
            map1.put("uid", RequestInfo.getUser().getUid());
            map.put(String.valueOf(auditList.get(i).getId()), this.videoDao.getVideoCountByStateAndUid(map1));
        }
        return map;
    }

    @Override
    public boolean deleteVideoByVid(Integer vid) {
        User user = RequestInfo.getUser();
        Video video = this.videoDao.getVideoByVid(vid);
        if (video == null) {
            throw new ServiceException("数据错误，请刷新后再试");
        }
        RequestInfo.judgeUid(video.getUid());

        this.videoMessageService.deleteVideoMessageByVid(vid);
        this.videoTagService.deleteVideoTagByVid(vid);
        this.videoFavoritesService.deleteFavoritesByVid(vid);
        this.videoLikeService.deleteVideoLikeByVid(vid);
        this.commentsService.deleteCommentsByVid(vid);
        this.fileChunksService.deleteVideoFileByVideo(video);
        this.videoDao.deleteVideoByVid(vid);
        return true;
    }

    @Override
    public VideoInfoDto getVideoDetailByVid(int vid) {
        VideoInfoDto videoInfoDto = this.videoDao.getVideoByVid(vid);
        RequestInfo.judgeUid(videoInfoDto.getUid());
        return videoInfoDto;
    }

    @Override
    public boolean updateVideo(VideoUpdate videoUpdate) {
        Video video = this.getVideoByVid(videoUpdate.getVid());
        RequestInfo.judgeUid(video.getUid());
        List<Integer> tagIdList = new ArrayList<>();
        if (videoUpdate.getTagNameList() != null) {
            tagIdList = this.tagService.insertAndGetTagIdByTagList(videoUpdate.getTagNameList());
        }

        if (tagIdList.size() > 0) {
            this.videoTagService.deleteVideoTagByVid(videoUpdate.getVid());
            this.videoTagService.insertVideoTagByTagIdListAndVid(tagIdList, videoUpdate.getVid());
        }
        if (!ObjectUtils.isEmpty(videoUpdate.getFile()) && videoUpdate.getFile().getSize() >= 0) {
            videoUpdate.setCover(this.fileChunksService.updateVideoCover(videoUpdate.getFile(), video.getCover(), RequestInfo.getUser().getUid()));
        }
        videoUpdate.setState(2);
        this.videoDao.updateVideoByVideo(videoUpdate);
        return true;
    }

    @Override
    public int getAllVideoCount(AdminVideoSearchPage adminVideoSearchPage) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", adminVideoSearchPage.getUid());
        map.put("vid", adminVideoSearchPage.getVid());
        map.put("state", adminVideoSearchPage.getState());
        map.put("partition", adminVideoSearchPage.getPartition());
        map.put("pageStart", (adminVideoSearchPage.getPageNum() - 1) * adminVideoSearchPage.getPageSize());
        map.put("pageSize", adminVideoSearchPage.getPageSize());
        return this.videoDao.getAllVideoCount(map);
    }

    @Override
    public List<VideoInfoDto> getVideoPageList(AdminVideoSearchPage adminVideoSearchPage) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", adminVideoSearchPage.getUid());
        map.put("vid", adminVideoSearchPage.getVid());
        map.put("state", adminVideoSearchPage.getState());
        map.put("partition", adminVideoSearchPage.getPartition());
        map.put("pageStart", (adminVideoSearchPage.getPageNum() - 1) * adminVideoSearchPage.getPageSize());
        map.put("pageSize", adminVideoSearchPage.getPageSize());
        return this.videoDao.getVideoPageList(map);
    }

    @Override
    public boolean updateVideoStateByVid(Map<String, Object> map) {
        return 0 != this.videoDao.updateVideoStateByVid(map);
    }

    @Override
    public VideoInfoDto adminGetVideoInfoByVid(int vid) {
        return this.videoDao.getVideoByVid(vid);
    }

    @Override
    public boolean videoComplaint(VideoComplaint videoComplaint) {
        Video video = this.videoDao.getVideoByVid(videoComplaint.getVid());
        RequestInfo.judgeUid(video.getUid());

        Map<String, Object> map = new HashMap<>();
        map.put("state", 2);
        map.put("vid", videoComplaint.getVid());
        this.videoDao.updateVideoStateByVid(map);
        return true;
    }

    @Override
    public List<Integer> getVideoIdListByUid(int uid) {
        return this.videoDao.getVideoIdListByUid(uid);
    }

    @Override
    public int updateVideoFavoritesByVideoIdList(List<Integer> videoIdList) {
        if (videoIdList.size() == 0) {
            return 0;
        }
        return this.videoDao.updateVideoFavoritesByVideoIdList(videoIdList);
    }

    @Override
    public int updateVideoLikeByVideoIdList(List<Integer> videoIdList) {
        if (videoIdList.size() == 0) {
            return 0;
        }
        return this.videoDao.updateVideoLikeByVideoIdList(videoIdList);
    }

    @Override
    public Map<String, Object> getVideoInfoListPageByUid(VideoInfoListPage videoInfoListPage) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (videoInfoListPage.getPageNum() - 1) * videoInfoListPage.getPageSize());
        map.put("pageSize", videoInfoListPage.getPageSize());
        map.put("uid", RequestInfo.getUser().getUid());
        map.put("sort", videoInfoListPage.getSort());
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("videoList", this.videoDao.getVideoInfoListPageByUid(map));
        retMap.put("videoCount", this.videoDao.getVideoInfoListPageCountByUid(map));
        return retMap;
    }

    @Override
    public int getEpisodeFirstVideoIdByEpisodeId(int id) {
        return this.videoDao.getEpisodeFirstVideoIdByEpisodeId(id);
    }

    @Override
    public Map<String, Object> getVideoInfoListPageByCollection(CollectionListPage collectionListPage) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (collectionListPage.getPageNum() - 1) * collectionListPage.getPageSize());
        map.put("pageSize", collectionListPage.getPageSize());
        map.put("uid", RequestInfo.getUser().getUid());
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("videoList", this.videoDao.getVideoByVideoIdList(this.videoFavoritesService.getVideoIdListPage(map)));
        retMap.put("videoCount", this.videoFavoritesService.getVideoIdListCount(map));
        return retMap;
    }

    @Override
    public void updateVideoEpisodeByEpisodeId(Integer id) {
        this.videoDao.updateVideoEpisodeByEpisodeId(id);
    }

    @Override
    public Map<String, Object> getConcernUserVideoInfoListPageByUid(ConcernUserListPage concernUserListPage) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (concernUserListPage.getPageNum() - 1) * concernUserListPage.getPageSize());
        map.put("pageSize", concernUserListPage.getPageSize());
        map.put("uid", RequestInfo.getUser().getUid());
        map.put("uidList", this.userService.getConcernUidListByUid(RequestInfo.getUser().getUid()));
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("videoList", this.videoDao.getConcernUserVideoInfoListPageByUidList(map));
        retMap.put("videoCount", this.videoDao.getConcernUserVideoInfoListCountByUidList(map));
        return retMap;
    }

    @Override
    public Map<String, Object> getVideoInfoListPage(VideoListPageBySearch videoListPageBySearch) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (videoListPageBySearch.getPageNum() - 1) * videoListPageBySearch.getPageSize());
        map.put("pageSize", videoListPageBySearch.getPageSize());
        map.put("name", videoListPageBySearch.getName());
        map.put("sort", videoListPageBySearch.getSort());
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("videoList", this.videoDao.getVideoInfoListPage(map));
        retMap.put("videoCount", this.videoDao.getVideoInfoListCount(map));
        return retMap;
    }

    @Override
    public Map<String, Object> getVideoInfoListPageByPartition(VideoListPageBySearch videoListPageBySearch) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (videoListPageBySearch.getPageNum() - 1) * videoListPageBySearch.getPageSize());
        map.put("pageSize", videoListPageBySearch.getPageSize());
        map.put("partition", videoListPageBySearch.getTypeName());
        map.put("sort", videoListPageBySearch.getSort());
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("videoList", this.videoDao.getVideoInfoListPageByPartition(map));
        retMap.put("videoCount", this.videoDao.getVideoInfoListCountByPartition(map));
        return retMap;
    }

    @Override
    public Map<String, Object> getVideoInfoListPageByVideoTag(VideoListPageBySearch videoListPageBySearch) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (videoListPageBySearch.getPageNum() - 1) * videoListPageBySearch.getPageSize());
        map.put("pageSize", videoListPageBySearch.getPageSize());
        map.put("tid", Integer.valueOf(videoListPageBySearch.getTypeName()));
        Map<String, Object> retMap = new HashMap<>();
        List<Integer> videoIdList = this.videoTagService.getVideoIdListPageByTid(map);
        retMap.put("videoList", this.videoDao.getVideoByVideoIdList(videoIdList));
        retMap.put("videoCount", this.videoTagService.getVideoListCountByTid(map));
        return retMap;
    }

    @Override
    public int getUserVideoAllLikeCount() {
        return this.videoDao.getUserVideoAllLikeCount(RequestInfo.getUser().getUid());
    }

    @Override
    public int getTheVideoForReviewByTime(Date time) {
        return this.videoDao.getTheVideoForReviewByTime(time);
    }

    @Override
    public int getTheVideoForReview() {
        return this.videoDao.getTheVideoForReview();
    }

    @Override
    public List<DateAndCountDto> getVideoUploadDataByTimeRange(String timeRange) {
        return this.videoDao.getVideoUploadDataByTimeRange(timeRange);
    }

    @Override
    public Map<String, Object> getVideoMessageListPageByUid(VideoMessageListPage videoMessageListPage) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (videoMessageListPage.getPageNum() - 1) * videoMessageListPage.getPageSize());
        map.put("pageSize", videoMessageListPage.getPageSize());
        map.put("uid", RequestInfo.getUser().getUid());

        Map<String, Object> retMap = new HashMap<>();
        List<VideoMessageDto> videoMessageDtoList=this.videoMessageService.getVideoMessageListPageByUid(map);
        if (videoMessageDtoList.size()>0){
            List<Integer> vIdList=new ArrayList<>();
            for (int i=0;i<videoMessageDtoList.size();i++){
                vIdList.add(videoMessageDtoList.get(i).getVid());
            }
            List<Audit> auditList=this.auditService.getAllAudit();
            Map<Integer,String> auditMap=new HashMap<>();
            for (int i=0;i<auditList.size();i++){
                auditMap.put(auditList.get(i).getId(),auditList.get(i).getName());
            }
            Map<Integer,Map<Integer,String>> videoIdAndTitleList=this.videoDao.getVideoIdAndVideoTitleByVidList(vIdList);
            for (int i=0;i<videoMessageDtoList.size();i++){
                videoMessageDtoList.get(i).setVideoTitle(videoIdAndTitleList.get(videoMessageDtoList.get(i).getVid()).get("title"));
                videoMessageDtoList.get(i).setStateName(auditMap.get(videoMessageDtoList.get(i).getState()));
            }
            retMap.put("videoMessageList", videoMessageDtoList);
            retMap.put("videoMessageCount", this.videoMessageService.getVideoMessageListCountByUid(map));
        }
        return retMap;
    }

    @Override
    public Map<String, Object> getUserVideoStatisticsByTimeRange(String timeRange) {
        Date today = new Date();
        List<DateTime> dateTimeList = new ArrayList<>();
        if ("Week".equals(timeRange)) {
            dateTimeList = DateUtil.rangeToList(DateUtil.offsetDay(today, -6), today, DateField.DAY_OF_WEEK);
        } else if ("Month1".equals(timeRange)) {
            dateTimeList = DateUtil.rangeToList(DateUtil.offsetDay(today, -29), today, DateField.DAY_OF_MONTH);
        } else if ("Month2".equals(timeRange)) {
            dateTimeList = DateUtil.rangeToList(DateUtil.offsetDay(today, -59), today, DateField.DAY_OF_MONTH);
        } else if ("Month3".equals(timeRange)) {
            dateTimeList = DateUtil.rangeToList(DateUtil.offsetDay(today, -89), today, DateField.DAY_OF_MONTH);
        }
        List<String> dateStringRange = dateTimeToDateString(dateTimeList);

        Map<String, Object> map = new HashMap<>();
        map.put("uid",RequestInfo.getUser().getUid());
        map.put("timeRange",timeRange);
        List<DateAndCountDto> videoUploadData = this.videoDao.getUserVideoUploadDataByTimeRange(map);
        map.put("state",1);
        List<DateAndCountDto> videoPassData = this.videoMessageService.getUserVideoAuditDataByTimeRangeAndAuditId(map);
        map.put("state",2);
        List<DateAndCountDto> videoAuditData = this.videoMessageService.getUserVideoAuditDataByTimeRangeAndAuditId(map);
        map.put("state",3);
        List<DateAndCountDto> videoNotPassData = this.videoMessageService.getUserVideoAuditDataByTimeRangeAndAuditId(map);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("date", dateStringRange);
        retMap.put("videoUploadData", countList(videoUploadData, dateStringRange));
        retMap.put("videoPassData", countList(videoPassData, dateStringRange));
        retMap.put("videoAuditData", countList(videoAuditData, dateStringRange));
        retMap.put("videoNotPassData", countList(videoNotPassData, dateStringRange));
        return retMap;
    }


}
