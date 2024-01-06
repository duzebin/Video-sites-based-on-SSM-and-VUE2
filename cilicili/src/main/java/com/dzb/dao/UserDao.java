package com.dzb.dao;

import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.entity.User;
import com.dzb.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDao {


    User getUserByAccountWithPassword(User loginUser);

    User getUserByAccount(String account);

    void updateUserLastLoginByUidAndTime(Map<String, Object> map);


    int insertUser(Map<String, Object> map);

    int getUserCountByAccount(String account);

    User getUserByUid(int uid);

    List<User> getUserByUidList(@Param("userIdList") List<Integer> userIdList);

    int updateUserStateByUidAndState(Map<String, Object> map);

    int deleteUserByUid(Integer uid);


}
