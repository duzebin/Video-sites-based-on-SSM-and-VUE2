package com.dzb.dao;

import com.dzb.controller.request.ConcernsOperate;

import java.util.List;
import java.util.Map;

public interface ConcernsDao {

    int getConcernsStateByUidAndFansUid(Map<String,Object> map);


    int insertConcernsByUidAndFansUid(Map<String,Object> map);

    int deleteConcernsByUidAndFansUid(Map<String,Object> map);

    List<Integer> getFansIdListByUid(Integer uid);

    List<Integer> getConcernsIdListByUid(Integer uid);

    int deleteConcernsByUid(Integer uid);
}
