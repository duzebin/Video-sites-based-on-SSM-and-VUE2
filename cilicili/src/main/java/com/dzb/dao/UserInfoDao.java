package com.dzb.dao;

import com.dzb.controller.dto.UserInfoDto;
import com.dzb.controller.request.ConcernsOperate;
import com.dzb.controller.request.UserInfoEdit;
import com.dzb.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserInfoDao {
    UserInfo getUserInfoByUid(int uid);

    void updateLevelByUid(int uid);

    void updateExperienceByUidAndNumber(Map<String, Object> map);

    int insertUserInfoByUserInfo(UserInfo userInfo);

    void updateConcernsCountByUidAndType(Map<String, Object> map);

    void updateFansCountByUidAndType(Map<String, Object> map);

    List<UserInfoDto> getAllUserInfoListPage(Map<String, Object> map);

    int getAllUserInfoListCount(Map<String, Object> map);

    int updateUserConcernCountByUidList(@Param("uidList") List<Integer> uidList);

    int updateUserFansCountByUidList(@Param("uidList") List<Integer> uidList);

    int deleteUserInfoByUid(Integer uid);

    int updateUserInfoByUserInfo(UserInfoEdit userInfoEdit);
}
