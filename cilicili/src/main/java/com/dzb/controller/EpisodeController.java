package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.controller.request.EpisodeListPage;
import com.dzb.entity.Episode;
import com.dzb.service.EpisodeService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Episode")
public class EpisodeController {
    @Autowired private EpisodeService episodeService;

    @GetMapping("/getEpisodeInfoByEpisodeId")
    public Result getEpisodeInfoByEpisodeId(int episodeId){
        return Result.success(this.episodeService.getEpisodeInfoByEpisodeId(episodeId));
    }
    @GetMapping("/getUserAllEpisode")
    public Result getUserAllEpisode(){
        return Result.success(this.episodeService.getUserAllEpisode());
    }

    @PostMapping("/insertEpisodeByEpisode")
    public Result insertEpisodeByEpisode(@RequestBody Episode episode){
        return Result.success(this.episodeService.insertEpisodeByEpisode(episode));
    }

    @GetMapping("/getEpisodeListPage")
    public Result getEpisodeListPage(EpisodeListPage episodeListPage){
        return Result.success(this.episodeService.getEpisodeListPage(episodeListPage));
    }

    @DeleteMapping("/deleteEpisodeById/{id}")
    public Result deleteEpisodeById(@PathVariable Integer id){
        return Result.success(this.episodeService.deleteEpisodeById(id));
    }
}
