package com.dzb.service;

import com.dzb.controller.dto.CommentsLikeStateDto;
import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.controller.request.*;
import com.dzb.entity.Comments;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CommentsService {

    Map<String,Object> getChildCommentsPageList(ChildCommentsPage childCommentsPage);

    Map<String,Object> getMainCommentsPageList(MainCommentsPage mainCommentsPage);

    int insertCommentsByComments(Comments comments);

    int deleteCommentsById(int commentsId);

    List<Comments> getAllCommentsListPage(AdminCommentsSearchPage adminCommentsSearchPage);

    int getAllCommentsCount(AdminCommentsSearchPage adminCommentsSearchPage);

    Comments getCommentsById(int id);

    int updateCommentsStateByIdAndState(int id,int state);

    List<CommentsLikeStateDto> getCommentsListLikeStateByCommentsList(List<CommentsIdList> commentsIdList);

    int updateCommentsLikeByCommentsLikeOperate(CommentsLikeOperate commentsLikeOperate);

    int deleteCommentsByVid(Integer vid);

    Map<String,Object> getUserAllVideoAllCommentsListPage(UserAllVideoAllCommentsListPage userAllVideoAllCommentsListPage);

    int deleteCommentsByUid(Integer uid);


    int getTheCommentsForReviewByTime(Date time);

    int getTheCommentsForReview();

    List<DateAndCountDto> getCommentsUpDateByTimeRange(String timeRange);
}
