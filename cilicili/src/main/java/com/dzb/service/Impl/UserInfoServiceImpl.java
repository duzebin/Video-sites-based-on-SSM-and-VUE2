package com.dzb.service.Impl;

import com.dzb.controller.dto.UserInfoDto;
import com.dzb.controller.request.AdminUserSearchPage;
import com.dzb.controller.request.ConcernsOperate;
import com.dzb.controller.request.UserInfoEdit;
import com.dzb.dao.UserInfoDao;
import com.dzb.entity.UserInfo;
import com.dzb.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired private UserInfoDao userInfoDao;

    public UserInfo getUserInfoByUid(int uid) {
        return this.userInfoDao.getUserInfoByUid(uid);
    }


    @Override
    public void updateLevelByUid(int uid) {
        this.userInfoDao.updateLevelByUid(uid);
    }

    @Override
    public int insertUserInfoByUserInfo(UserInfo userInfo) {
        return this.userInfoDao.insertUserInfoByUserInfo(userInfo);
    }

    @Override
    public void updateConcernsCountByUidAndType(int uid,String type) {
        Map<String,Object> map=new HashMap<>();
        map.put("uid",uid);
        map.put("type",type);
        this.userInfoDao.updateConcernsCountByUidAndType(map);
    }

    @Override
    public void updateFansCountByUidAndType(int uid,String type) {
        Map<String,Object> map=new HashMap<>();
        map.put("uid",uid);
        map.put("type",type);
        this.userInfoDao.updateFansCountByUidAndType(map);
    }

    @Override
    public List<UserInfoDto> getAllUserInfoListPage(AdminUserSearchPage adminUserSearchPage) {
        Map<String,Object> map=new HashMap<>();
        map.put("uid",adminUserSearchPage.getUid());
        map.put("uname",adminUserSearchPage.getUname());
        map.put("pageStart", (adminUserSearchPage.getPageNum() - 1) * adminUserSearchPage.getPageSize());
        map.put("pageSize", adminUserSearchPage.getPageSize());
        return this.userInfoDao.getAllUserInfoListPage(map);
    }

    @Override
    public int getAllUserInfoListCount(AdminUserSearchPage adminUserSearchPage) {
        Map<String,Object> map=new HashMap<>();
        map.put("uid",adminUserSearchPage.getUid());
        map.put("uname",adminUserSearchPage.getUname());
        map.put("pageStart", (adminUserSearchPage.getPageNum() - 1) * adminUserSearchPage.getPageSize());
        map.put("pageSize", adminUserSearchPage.getPageSize());
        return this.userInfoDao.getAllUserInfoListCount(map);
    }

    @Override
    public int updateUserConcernCountByUidList(List<Integer> fansIdList) {
        if (fansIdList.size()==0){
            return 0;
        }
        return this.userInfoDao.updateUserConcernCountByUidList(fansIdList);
    }

    @Override
    public int updateUserFansCountByUidList(List<Integer> concernsIdList) {
        if (concernsIdList.size()==0){
            return 0;
        }
        return this.userInfoDao.updateUserFansCountByUidList(concernsIdList);
    }

    @Override
    public int deleteUserInfoByUid(Integer uid) {
        return this.userInfoDao.deleteUserInfoByUid(uid);
    }

    @Override
    public int updateUserInfoByUserInfo(UserInfoEdit userInfoEdit) {
        return this.userInfoDao.updateUserInfoByUserInfo(userInfoEdit);
    }


    @Override
    public void updateExperienceByUidAndNumber(int uid, int loginExperience) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("uid",uid);
        map.put("loginExperience",loginExperience);
        this.userInfoDao.updateExperienceByUidAndNumber(map);
    }


}
