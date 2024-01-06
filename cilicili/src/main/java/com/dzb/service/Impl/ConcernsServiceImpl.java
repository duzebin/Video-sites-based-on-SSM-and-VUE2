package com.dzb.service.Impl;

import com.dzb.common.RequestInfo;
import com.dzb.controller.request.ConcernsOperate;
import com.dzb.dao.ConcernsDao;
import com.dzb.entity.User;
import com.dzb.exception.ServiceException;
import com.dzb.service.ConcernsService;
import com.dzb.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ConcernsServiceImpl implements ConcernsService {
    @Autowired private ConcernsDao concernsDao;

    @Override
    public int getConcernsStateByUidAndFansUid(int uid, int fansUid) {
        Map<String,Object> map=new HashMap<>();
        map.put("uid",uid);
        map.put("fansUid",fansUid);
        return this.concernsDao.getConcernsStateByUidAndFansUid(map);
    }

    @Override
    public int insertConcernsByUidAndFansUid(int uid, int fansUid) {
        Map<String,Object> map=new HashMap<>();
        map.put("uid",uid);
        map.put("fansUid",fansUid);
        return this.concernsDao.insertConcernsByUidAndFansUid(map);
    }

    @Override
    public int deleteConcernsByUidAndFansUid(int uid, int fansUid) {
        Map<String,Object> map=new HashMap<>();
        map.put("uid",uid);
        map.put("fansUid",fansUid);
        return this.concernsDao.deleteConcernsByUidAndFansUid(map);
    }

    @Override
    public List<Integer> getFansIdListByUid(Integer uid) {
        return this.concernsDao.getFansIdListByUid(uid);
    }

    @Override
    public List<Integer> getConcernsIdListByUid(Integer uid) {
        return this.concernsDao.getConcernsIdListByUid(uid);
    }

    @Override
    public int deleteConcernsByUid(Integer uid) {
        return this.concernsDao.deleteConcernsByUid(uid);
    }

    @Override
    public List<Integer> getConcernUidListByUid(int uid) {
        return this.concernsDao.getConcernsIdListByUid(uid);
    }


}
