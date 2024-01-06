package com.dzb.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.dzb.common.RequestInfo;
import com.dzb.controller.dto.CommentsInfoDto;
import com.dzb.controller.dto.CommentsLikeStateDto;
import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.controller.request.*;
import com.dzb.dao.CommentsDao;
import com.dzb.entity.Comments;
import com.dzb.entity.User;
import com.dzb.exception.ServiceException;
import com.dzb.service.CommentsLikeService;
import com.dzb.service.CommentsService;
import com.dzb.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsDao commentsDao;
    @Autowired
    private CommentsLikeService commentsLikeService;
    @Autowired
    private VideoService videoService;

    public Map<String, Object> getChildCommentsPageList(ChildCommentsPage childCommentsPage) {
        Map<String, Object> childMap = new HashMap<String, Object>();
        childMap.put("pageStart", (childCommentsPage.getPageNum() - 1) * childCommentsPage.getPageSize());
        childMap.put("pageSize", childCommentsPage.getPageSize());
        childMap.put("adhereId", childCommentsPage.getAdhereId());
        Map<String, Object> childCommentsMap = new HashMap<String, Object>();
        childCommentsMap.put("childCommentsList", this.commentsDao.getChildCommentsPageList(childMap));
        childCommentsMap.put("childCommentsCount", this.commentsDao.getChildCommentsCountByAdhere(childCommentsPage.getAdhereId()));
        return childCommentsMap;
    }

    public Map<String, Object> getMainCommentsPageList(MainCommentsPage mainCommentsPage) {
        Map<String, Object> mainMap = new HashMap<String, Object>();
        mainMap.put("pageStart", (mainCommentsPage.getPageNum() - 1) * mainCommentsPage.getPageSize());
        mainMap.put("pageSize", mainCommentsPage.getPageSize());
        mainMap.put("vid", mainCommentsPage.getVid());
        mainMap.put("sort", mainCommentsPage.getSort());
        List<CommentsInfoDto> mainCommentsList = this.commentsDao.getMainCommentsPageList(mainMap);
        ChildCommentsPage childCommentsPage = new ChildCommentsPage();
        childCommentsPage.setPageSize(mainCommentsPage.getChildPageSize());
        childCommentsPage.setPageNum(1);
        for (int i = 0; i < mainCommentsList.size(); i++) {
            childCommentsPage.setAdhereId(mainCommentsList.get(i).getId());
            Map<String, Object> childCommentsList = getChildCommentsPageList(childCommentsPage);
            mainCommentsList.get(i).setChildCommentsCount((Integer) childCommentsList.get("childCommentsCount"));
            mainCommentsList.get(i).setChildCommentsList((List<Comments>) childCommentsList.get("childCommentsList"));
        }
        Map<String, Object> commentsMap = new HashMap<String, Object>();
        commentsMap.put("mainCommentsList", mainCommentsList);
        commentsMap.put("mainCommentsCount", this.commentsDao.getMainCommentsCountByVid(mainCommentsPage.getVid()));
        commentsMap.put("commentsCount", this.commentsDao.getCommentsCountByVid(mainCommentsPage.getVid()));
        return commentsMap;
    }

    @Override
    public int insertCommentsByComments(Comments comments) {
        User user = RequestInfo.getUser();
        comments.setUid(user.getUid());
        comments.setState(2);
        if (user.getState() == 3) {
            throw new ServiceException("您已被禁止评论,请联系管理员");
        }
        if (StrUtil.isBlank(comments.getText())) {
            throw new ServiceException("不能发送空信息");
        }
        return this.commentsDao.insertCommentsByComments(comments);
    }

    @Override
    public int deleteCommentsById(int commentsId) {
        Map<String, Object> comments = this.commentsDao.getCommentsMapByCommentsId(commentsId);
        RequestInfo.judgeUid((Integer) comments.get("uid"), this.videoService.getVideoUidByVid((Integer) comments.get("vid")));
        int delChildCommentsCount = 0;
        if (!comments.containsKey("adhere")) {
            List<Integer> commentsIdList = this.commentsDao.getChildCommentsIdByAdhere((Integer) comments.get("id"));
            if (commentsIdList.size() != 0) {
                this.commentsLikeService.deleteCommentsLikeByCommentsIdList(commentsIdList);
                delChildCommentsCount = this.commentsDao.deleteCommentsByAdhere((Integer) comments.get("id"));
            }
        }
        this.commentsLikeService.deleteCommentsLikeByCommentsId(commentsId);
        return delChildCommentsCount + this.commentsDao.deleteCommentsById(commentsId);
    }

    @Override
    public List<Comments> getAllCommentsListPage(AdminCommentsSearchPage adminCommentsSearchPage) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (adminCommentsSearchPage.getPageNum() - 1) * adminCommentsSearchPage.getPageSize());
        map.put("pageSize", adminCommentsSearchPage.getPageSize());
        map.put("uid", adminCommentsSearchPage.getUid());
        map.put("vid", adminCommentsSearchPage.getVid());
        map.put("state", adminCommentsSearchPage.getState());
        map.put("text",adminCommentsSearchPage.getText());
        return this.commentsDao.getAllCommentsListPage(map);
    }

    @Override
    public int getAllCommentsCount(AdminCommentsSearchPage adminCommentsSearchPage) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", adminCommentsSearchPage.getUid());
        map.put("vid", adminCommentsSearchPage.getVid());
        map.put("state", adminCommentsSearchPage.getState());
        return this.commentsDao.getAllCommentsCount(map);
    }

    @Override
    public Comments getCommentsById(int id) {
        return this.commentsDao.getCommentsById(id);
    }

    @Override
    public int updateCommentsStateByIdAndState(int id, int state) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("state", 1);
        return this.commentsDao.updateCommentsStateByIdAndState(map);
    }

    @Override
    public List<CommentsLikeStateDto> getCommentsListLikeStateByCommentsList(List<CommentsIdList> commentsIdList) {
        return this.commentsLikeService.getCommentsListLikeStateByCommentsList(commentsIdList);
    }

    @Override
    public int updateCommentsLikeByCommentsLikeOperate(CommentsLikeOperate commentsLikeOperate) {
        commentsLikeOperate.setUid(RequestInfo.getUser().getUid());
        int x = this.commentsLikeService.getCommentsLikeStateByUidAndCid(commentsLikeOperate.getUid(), commentsLikeOperate.getCid());
        int n = 0;
        if ("add".equals(commentsLikeOperate.getType()) && x == 0) {
            n = this.commentsLikeService.insertCommentsLikeByUidAndCid(commentsLikeOperate.getUid(), commentsLikeOperate.getCid());
        } else if ("cancel".equals(commentsLikeOperate.getType()) && x != 0) {
            n = this.commentsLikeService.deleteCommentsLikeByUidAndCid(commentsLikeOperate.getUid(), commentsLikeOperate.getCid());
        } else {
            throw new ServiceException("数据错误");
        }
        if (n != 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("cid", commentsLikeOperate.getCid());
            map.put("type", commentsLikeOperate.getType());
            this.commentsDao.updateCommentsLikeByCidAndType(map);
        }
        return n;
    }

    @Override
    public int deleteCommentsByVid(Integer vid) {
        int n = 0;
        List<Integer> cidList = this.commentsDao.getCommentsIdListByVid(vid);
        if (cidList.size() > 0) {
            n += this.commentsLikeService.deleteCommentsLikeByCommentsIdList(cidList);
        }
        List<Integer> childCidList = this.commentsDao.getChildCommentsIdListByVid(vid);
        if (childCidList.size() > 0) {
            n += this.commentsDao.deleteCommentsByIdList(childCidList);
        }
        List<Integer> mainCidList = this.commentsDao.getMainCommentsIdListByVid(vid);
        if (mainCidList.size() > 0) {
            n += this.commentsDao.deleteCommentsByIdList(mainCidList);
        }
        return n;
    }

    @Override
    public Map<String, Object> getUserAllVideoAllCommentsListPage(UserAllVideoAllCommentsListPage userAllVideoAllCommentsListPage) {
        User user = RequestInfo.getUser();
        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (userAllVideoAllCommentsListPage.getPageNum() - 1) * userAllVideoAllCommentsListPage.getPageSize());
        map.put("pageSize", userAllVideoAllCommentsListPage.getPageSize());
        List<Integer> videoIdList = new ArrayList<>();
        videoIdList.add(userAllVideoAllCommentsListPage.getVid());
        if (userAllVideoAllCommentsListPage.getVid() == 0) {
            videoIdList = this.videoService.getVideoIdListByUid(user.getUid());
        }
        map.put("videoIdList", videoIdList);
        map.put("uid", userAllVideoAllCommentsListPage.getUid());
        map.put("text",userAllVideoAllCommentsListPage.getText());

        Map<String,Object> retMap=new HashMap<>();
        retMap.put("commentsList",this.commentsDao.getUserAllVideoAllCommentsListPage(map));
        retMap.put("commentsCount",this.commentsDao.getUserAllVideoAllCommentsListCount(map));
        return retMap;
    }

    @Override
    public int deleteCommentsByUid(Integer uid) {
        int n=0;
        this.commentsLikeService.deleteCommentsLikeByCommentsIdList(this.commentsDao.getChildCommentsIdListByUid(uid));
        n+=this.commentsDao.deleteChildCommentsByUid(uid);
        this.commentsLikeService.deleteCommentsLikeByCommentsIdList(this.commentsDao.getMainCommentsIdListByUid(uid));
        n+=this.commentsDao.deleteMainCommentsByUid(uid);
        return n;
    }

    @Override
    public int getTheCommentsForReviewByTime(Date time) {
        return this.commentsDao.getTheCommentsForReviewByTime(time);
    }

    @Override
    public int getTheCommentsForReview() {
        return this.commentsDao.getTheCommentsForReview();
    }

    @Override
    public List<DateAndCountDto> getCommentsUpDateByTimeRange(String timeRange) {
        return this.commentsDao.getCommentsUpDateByTimeRange(timeRange);
    }


}
