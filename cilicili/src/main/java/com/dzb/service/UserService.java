package com.dzb.service;

import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.controller.dto.UserInfoDto;
import com.dzb.controller.request.*;
import com.dzb.entity.User;
import com.dzb.entity.UserInfo;

import java.util.List;

public interface UserService {

    UserInfoDto login(User loginUser);

    User getUserByAccount(String account);

    UserInfoDto insertUserByEnrollUserInfo(EnrollUserInfo enrollUserInfo);


    User getUserByUid(int uid);

    UserInfo getUserInfoByUid(int uid);

    boolean getConcernsStateByUid(int uid);

    int updateConcernByConcernsOperate(ConcernsOperate concernsOperate);

    List<User> getUserByUidList(List<Integer> userIdList);

    boolean updateUserStateByUidAndState(UpdateUserState updateUserState);

    int deleteUserByUid(Integer uid);

    List<Integer> getConcernUidListByUid(int uid);

    UserInfoDto updateUserInfoByUserInfo(UserInfoEdit userInfoEdit);
}
