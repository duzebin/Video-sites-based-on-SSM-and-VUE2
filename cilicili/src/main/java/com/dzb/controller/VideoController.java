package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.controller.request.*;
import com.dzb.entity.FileChunks;
import com.dzb.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Video")
public class VideoController {
    @Autowired VideoService videoService;

    @GetMapping("/getAllVideo")
    public Result getAllVideo(){
        return Result.success(this.videoService.getAllVideo());
    }

    @GetMapping("/getVideoByVid")
    public Result getVideoByVid(int vid){
        return Result.success(this.videoService.getVideoByVid(vid));
    }

    @GetMapping("/getVideoTagByVid")
    public Result getVideoTagByVid(int vid){
        return Result.success(this.videoService.getVideoTagByVid(vid));
    }

    @GetMapping("/getEpisodeListByEpisodeId")
    public Result getEpisodeListByEpisodeId(int episodeId){
        return Result.success(this.videoService.getEpisodeListByEpisodeId(episodeId));
    }

    @PostMapping("/updateVideoFavoritesByFavoritesOperate")
    public Result updateVideoFavoritesByFavoritesOperate(@RequestBody VideoFavoritesOperate videoFavoritesOperate){
        return Result.success(this.videoService.updateVideoFavoritesByFavoritesOperate(videoFavoritesOperate));
    }
    @GetMapping("/getVideoFavoritesStateByVid")
    public Result getVideoFavoritesStateByVid(int vid){
        return Result.success(this.videoService.getVideoFavoritesStateByVid(vid));
    }

    @GetMapping("/getVideoLikeStateByVid")
    public Result getVideoLikeStateByVid(int vid){
        return Result.success(this.videoService.getVideoLikeStateByVid(vid));
    }

    @PostMapping("/updateVideoLikeByVideoLikeOperate")
    public Result updateVideoLikeByVideoLikeOperate(@RequestBody VideoLikeOperate videoLikeOperate){
        return Result.success(this.videoService.updateVideoLikeByVideoLikeOperate(videoLikeOperate));
    }

    @GetMapping("/getVideoByMd5AndCount")
    public Result getVideoByMd5(FileChunks fileChunks){
        return Result.success(this.videoService.getVideoByMd5AndCount(fileChunks.getMd5(),fileChunks.getCount()));
    }

    @PostMapping("/uploadVideoChunkFile")
    public Result uploadVideoChunkFile(FileChunksInfo fileChunksInfo){
        return Result.success(this.videoService.uploadVideoChunkFile(fileChunksInfo));
    }

    @GetMapping("/uploadFileChunkMergeByMd5")
    public Result uploadFileChunkMergeByMd5(String md5){
        return Result.success(this.videoService.uploadFileChunkMergeByMd5(md5));
    }

    @PostMapping("/uploadVideo")
    public Result uploadVideo(VideoUpload videoUpload){
        return Result.success(this.videoService.uploadVideo(videoUpload));
    }

    @GetMapping("/getUserUploadVideoInfoPageList")
    public Result getUserUploadVideoInfoPageList(UserUploadVideoInfoPage userUploadVideoInfoPage){
        return Result.success(this.videoService.getUserUploadVideoInfoPageList(userUploadVideoInfoPage));
    }

    @GetMapping("/getUserAllVideoStateCount")
    public Result getUserAllVideoStateCount(){
        return Result.success(this.videoService.getUserAllVideoStateCount());
    }

    @DeleteMapping("/deleteVideoByVid/{vid}")
    public Result deleteVideoByVid(@PathVariable Integer vid){
        return Result.success(this.videoService.deleteVideoByVid(vid));
    }

    @GetMapping("/getVideoDetailByVid")
    public Result getVideoDetailByVid(int vid){
        return Result.success(this.videoService.getVideoDetailByVid(vid));
    }

    @PostMapping("/updateVideo")
    public Result updateVideo(VideoUpdate videoUpdate){
        return Result.success(this.videoService.updateVideo(videoUpdate));
    }

    @PostMapping("/videoComplaint")
    public Result videoComplaint(@RequestBody VideoComplaint videoComplaint){
        return Result.success(this.videoService.videoComplaint(videoComplaint));
    }

    @GetMapping("/getVideoInfoListPageByUid")
    public Result getVideoInfoListPageByUid(VideoInfoListPage videoInfoListPage){
        return Result.success(this.videoService.getVideoInfoListPageByUid(videoInfoListPage));
    }

    @GetMapping("/getEpisodeFirstVideoIdByEpisodeId")
    public Result getEpisodeFirstVideoIdByEpisodeId(int id){
        return Result.success(this.videoService.getEpisodeFirstVideoIdByEpisodeId(id));
    }

    @GetMapping("/getVideoInfoListPageByCollection")
    public Result getVideoInfoListPageByCollection(CollectionListPage collectionListPage){
        return Result.success(this.videoService.getVideoInfoListPageByCollection(collectionListPage));
    }

    @GetMapping("/getConcernUserVideoInfoListPageByUid")
    public Result getConcernUserVideoInfoListPageByUid(ConcernUserListPage concernUserListPage){
        return Result.success(this.videoService.getConcernUserVideoInfoListPageByUid(concernUserListPage));
    }

    @GetMapping("/getVideoInfoListPage")
    public Result getVideoInfoListPage(VideoListPageBySearch videoListPageBySearch){
        return Result.success(this.videoService.getVideoInfoListPage(videoListPageBySearch));
    }

    @GetMapping("/getVideoInfoListPageByPartition")
    public Result getVideoInfoListPageByPartition(VideoListPageBySearch videoListPageBySearch){
        return Result.success(this.videoService.getVideoInfoListPageByPartition(videoListPageBySearch));
    }

    @GetMapping("/getVideoInfoListPageByVideoTag")
    public Result getVideoInfoListPageByVideoTag(VideoListPageBySearch videoListPageBySearch){
        return Result.success(this.videoService.getVideoInfoListPageByVideoTag(videoListPageBySearch));
    }

    @GetMapping("/getUserVideoAllLikeCount")
    public  Result getUserVideoAllLikeCount(){
        return Result.success(this.videoService.getUserVideoAllLikeCount());
    }

    @GetMapping("/getVideoMessageListPageByUid")
    public Result getVideoMessageListPageByUid(VideoMessageListPage videoMessageListPage){
        return Result.success(this.videoService.getVideoMessageListPageByUid(videoMessageListPage));
    }

    @GetMapping("/getUserVideoStatisticsByTimeRange")
    public Result getUserVideoStatisticsByTimeRange(String timeRange){
        return Result.success(this.videoService.getUserVideoStatisticsByTimeRange(timeRange));
    }
}
