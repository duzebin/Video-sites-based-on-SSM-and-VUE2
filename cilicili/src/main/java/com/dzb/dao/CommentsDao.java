package com.dzb.dao;


import com.dzb.controller.dto.CommentsInfoDto;
import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.entity.Comments;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CommentsDao {

    int getMainCommentsCountByVid(int vid);

    int getChildCommentsCountByAdhere(int id);

    List<CommentsInfoDto> getChildCommentsPageList(Map<String, Object> childMap);

    List<CommentsInfoDto> getMainCommentsPageList(Map<String, Object> mainMap);

    int getCommentsCountByVid(int vid);

    int insertCommentsByComments(Comments comments);

    int deleteCommentsById(int commentsId);

    Map<String, Object> getCommentsMapByCommentsId(int commentsId);

    int deleteCommentsByAdhere(int adhere);

    List<Integer> getChildCommentsIdByAdhere(int adhere);

    List<Comments> getAllCommentsListPage(Map<String, Object> map);

    int getAllCommentsCount(Map<String, Object> map);

    Comments getCommentsById(int id);

    int updateCommentsStateByIdAndState(Map<String, Object> map);


    void updateCommentsLikeByCidAndType(Map<String, Object> map);

    List<Integer> getCommentsIdListByVid(Integer vid);

    int deleteCommentsByIdList(@Param("cidList")List<Integer> cidList);

    List<Integer> getChildCommentsIdListByVid(Integer vid);

    List<Integer> getMainCommentsIdListByVid(Integer vid);

    List<Comments> getUserAllVideoAllCommentsListPage(Map<String, Object> map);

    int getUserAllVideoAllCommentsListCount(Map<String, Object> map);

    int deleteChildCommentsByUid(Integer uid);

    int deleteMainCommentsByUid(Integer uid);

    List<Integer> getChildCommentsIdListByUid(Integer uid);

    List<Integer> getMainCommentsIdListByUid(Integer uid);


    int getTheCommentsForReviewByTime(Date time);

    int getTheCommentsForReview();

    List<DateAndCountDto> getCommentsUpDateByTimeRange(String timeRange);
}
