package com.dzb.dao;

import com.dzb.controller.request.CommentsLikeOperate;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CommentsLikeDao {

    List<Integer> getCommentsLikeStateByCidListAndUid(Map<String, Object> map);

    int deleteCommentsLikeByCommentsIdList(@Param("commentsIdList")List<Integer> commentsIdList);

    void deleteCommentsLikeByCommentsId(int commentsId);

    int insertCommentsLikeByUidAndCid(HashMap<String, Object> map);

    int deleteCommentsLikeByUidAndCid(HashMap<String, Object> map);

    int getCommentsLikeStateByUidAndCid(HashMap<String, Object> map);
}
