package com.dzb.service;

import com.dzb.controller.request.ConcernsOperate;

import java.util.List;

public interface ConcernsService {

    int getConcernsStateByUidAndFansUid(int uid, int fansUid);

    int insertConcernsByUidAndFansUid(int uid, int fansUid);

    int deleteConcernsByUidAndFansUid(int uid, int fansUid);

    List<Integer> getFansIdListByUid(Integer uid);

    List<Integer> getConcernsIdListByUid(Integer uid);

    int deleteConcernsByUid(Integer uid);

    List<Integer> getConcernUidListByUid(int uid);
}
