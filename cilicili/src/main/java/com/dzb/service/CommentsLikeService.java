package com.dzb.service;

import com.dzb.controller.dto.CommentsLikeStateDto;
import com.dzb.controller.request.CommentsLikeOperate;
import com.dzb.controller.request.CommentsIdList;

import java.util.HashMap;
import java.util.List;

public interface CommentsLikeService {

    List<CommentsLikeStateDto> getCommentsListLikeStateByCommentsList(List<CommentsIdList> commentsIdList);

    int deleteCommentsLikeByCommentsIdList(List<Integer> commentsIdList);

    void deleteCommentsLikeByCommentsId(int commentsId);

    int insertCommentsLikeByUidAndCid(int uid, int cid);

    int deleteCommentsLikeByUidAndCid(int uid, int cid);

    int getCommentsLikeStateByUidAndCid(int uid, int cid);
}
