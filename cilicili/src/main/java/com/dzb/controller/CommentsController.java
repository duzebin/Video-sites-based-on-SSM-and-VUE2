package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.controller.request.*;
import com.dzb.entity.Comments;
import com.dzb.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Comments")
public class CommentsController {
    @Autowired private CommentsService commentsService;

    @GetMapping("/getChildCommentsPageList")
    public Result getChildCommentsPageList(ChildCommentsPage childCommentsPage){
        return Result.success(this.commentsService.getChildCommentsPageList(childCommentsPage));
    }

    @GetMapping("/getMainCommentsPageList")
    public Result getMainCommentsPageList(MainCommentsPage mainCommentsPage){
        return Result.success(this.commentsService.getMainCommentsPageList(mainCommentsPage));
    }

    @PostMapping("/insertCommentsByComments")
    public Result insertCommentsByComments(@RequestBody Comments comments){
        return Result.success(this.commentsService.insertCommentsByComments(comments));
    }

    @DeleteMapping("/deleteCommentsById/{commentsId}")
    public Result deleteCommentsById(@PathVariable Integer commentsId){
        return Result.success(this.commentsService.deleteCommentsById(commentsId));
    }

    @PostMapping("/getCommentsListLikeStateByCommentsList")
    public Result getCommentsListLikeStateByCommentsList(@RequestBody List<CommentsIdList> commentsIdList){
        return Result.success(this.commentsService.getCommentsListLikeStateByCommentsList(commentsIdList));
    }

    @PostMapping("/updateCommentsLikeByCommentsLikeOperate")
    public Result updateCommentsLikeByCommentsLikeOperate(@RequestBody CommentsLikeOperate commentsLikeOperate){
        return Result.success(this.commentsService.updateCommentsLikeByCommentsLikeOperate(commentsLikeOperate));
    }

    @GetMapping("/getUserAllVideoAllCommentsListPage")
    public Result getUserAllVideoAllCommentsListPage(UserAllVideoAllCommentsListPage userAllVideoAllCommentsListPage){
        return Result.success(this.commentsService.getUserAllVideoAllCommentsListPage(userAllVideoAllCommentsListPage));
    }
}
