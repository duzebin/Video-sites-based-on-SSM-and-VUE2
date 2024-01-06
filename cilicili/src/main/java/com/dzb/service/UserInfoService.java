package com.dzb.service;

import com.dzb.controller.dto.UserInfoDto;
import com.dzb.controller.request.AdminUserSearchPage;
import com.dzb.controller.request.ConcernsOperate;
import com.dzb.controller.request.UserInfoEdit;
import com.dzb.entity.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoService {

    UserInfo getUserInfoByUid(int uid);

    void updateExperienceByUidAndNumber(int uid, int loginExperience);

    void updateLevelByUid(int uid);

    int insertUserInfoByUserInfo(UserInfo userInfo);

    void updateConcernsCountByUidAndType(int uid,String type);

    void updateFansCountByUidAndType(int uid,String type);


    List<UserInfoDto> getAllUserInfoListPage(AdminUserSearchPage adminUserSearchPage);

    int getAllUserInfoListCount(AdminUserSearchPage adminUserSearchPage);

    int updateUserConcernCountByUidList(List<Integer> fansIdList);

    int updateUserFansCountByUidList(List<Integer> concernsIdList);

    int deleteUserInfoByUid(Integer uid);

    int updateUserInfoByUserInfo(UserInfoEdit userInfoEdit);
}
