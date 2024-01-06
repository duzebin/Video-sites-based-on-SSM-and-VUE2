package com.dzb.service.Impl;

import com.dzb.common.RequestInfo;
import com.dzb.controller.dto.CommentsLikeStateDto;
import com.dzb.controller.request.CommentsLikeOperate;
import com.dzb.controller.request.CommentsIdList;
import com.dzb.dao.CommentsLikeDao;
import com.dzb.exception.ServiceException;
import com.dzb.service.CommentsLikeService;
import com.dzb.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommentsLikeServiceImpl implements CommentsLikeService {
    @Autowired private CommentsLikeDao commentsLikeDao;

    @Override
    public List<CommentsLikeStateDto> getCommentsListLikeStateByCommentsList(List<CommentsIdList> commentsIdList) {
        List<CommentsLikeStateDto> commentsLikeStateDtoList = new ArrayList<CommentsLikeStateDto>();
        List<Integer> cidList = new ArrayList<Integer>();
        for (int i = 0; i < commentsIdList.size(); i++) {
            cidList.add(commentsIdList.get(i).getCid());
            for (int j = 0; j < commentsIdList.get(i).getChildCommentsList().size(); j++) {
                cidList.add(commentsIdList.get(i).getChildCommentsList().get(j));
            }
        }
        if (cidList.size()==0){
            return commentsLikeStateDtoList;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", RequestInfo.getUser().getUid());
        map.put("cidList", cidList);
        List<Integer> cList = this.commentsLikeDao.getCommentsLikeStateByCidListAndUid(map);
        for (int i = 0; i < commentsIdList.size(); i++) {
            CommentsLikeStateDto commentsLikeStateDto = new CommentsLikeStateDto();
            commentsLikeStateDto.setMainCommentsLikeState(cList.contains(commentsIdList.get(i).getCid()));
            List<Boolean> childCommentsLikeStateList = new ArrayList<Boolean>();
            for (int j = 0; j < commentsIdList.get(i).getChildCommentsList().size(); j++) {
                childCommentsLikeStateList.add(cList.contains(commentsIdList.get(i).getChildCommentsList().get(j)));
            }
            commentsLikeStateDto.setChildCommentsLikeState(childCommentsLikeStateList);
            commentsLikeStateDtoList.add(commentsLikeStateDto);
        }
        return commentsLikeStateDtoList;
    }

    @Override
    public int deleteCommentsLikeByCommentsIdList(List<Integer> commentsIdList) {
        if (commentsIdList.size()==0){
            return 0;
        }
        return this.commentsLikeDao.deleteCommentsLikeByCommentsIdList(commentsIdList);
    }

    @Override
    public void deleteCommentsLikeByCommentsId(int commentsId) {
        this.commentsLikeDao.deleteCommentsLikeByCommentsId(commentsId);
    }


    @Override
    public int insertCommentsLikeByUidAndCid(int uid, int cid) {
        HashMap<String,Object> map=new HashMap<>();
        map.put("uid",uid);
        map.put("cid",cid);
        return this.commentsLikeDao.insertCommentsLikeByUidAndCid(map);
    }

    @Override
    public int deleteCommentsLikeByUidAndCid(int uid, int cid) {
        HashMap<String,Object> map=new HashMap<>();
        map.put("uid",uid);
        map.put("cid",cid);
        return this.commentsLikeDao.deleteCommentsLikeByUidAndCid(map);
    }

    @Override
    public int getCommentsLikeStateByUidAndCid(int uid, int cid) {
        HashMap<String,Object> map=new HashMap<>();
        map.put("uid",uid);
        map.put("cid",cid);
        return this.commentsLikeDao.getCommentsLikeStateByUidAndCid(map);
    }

}
