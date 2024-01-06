package com.dzb.service.Impl;

import com.dzb.common.RequestInfo;
import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.controller.dto.UserInfoDto;
import com.dzb.controller.request.*;
import com.dzb.utils.TokenUtils;
import com.dzb.dao.UserDao;
import com.dzb.entity.User;
import com.dzb.entity.UserInfo;
import com.dzb.exception.ServiceException;
import com.dzb.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final int LOGIN_EXPERIENCE = 10;
    private  static final String SALT="DuZeBin";
    @Autowired private UserDao userDao;
    @Autowired private UserInfoService userInfoService;
    @Autowired private UserStateService userStateService;
    @Autowired private LevelService levelService;
    @Autowired private FileChunksService fileChunksService;
    @Autowired private ConcernsService concernsService;

    private String passwordMd5Encrypt(String password){
        String saltPassword=password+SALT;
        return DigestUtils.md5DigestAsHex(saltPassword.getBytes());
    }

    public UserInfoDto login(User loginUser) {
        loginUser.setPassword(passwordMd5Encrypt(loginUser.getPassword()));
        User user = this.userDao.getUserByAccountWithPassword(loginUser);
        if (user == null) {
            throw new ServiceException("用户账号或密码错误");
        }
        if (user.getState() == 2) {
            throw new ServiceException("该账户已被禁止登录");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", user.getUid());
        map.put("time", sdf1.format(new Date()));
        this.userDao.updateUserLastLoginByUidAndTime(map);
        UserInfoDto userInfoDto = new UserInfoDto();
        try {
            BeanUtils.copyProperties(userInfoDto, this.userInfoService.getUserInfoByUid(user.getUid()));
            Date currentTime = new Date();
            String currentTimeString = sdf.format(currentTime);
            currentTime = sdf.parse(currentTimeString);
            if (user.getLastLogin().getTime() < currentTime.getTime()) {
                this.userInfoService.updateExperienceByUidAndNumber(user.getUid(), LOGIN_EXPERIENCE);
                userInfoDto.setExperience(userInfoDto.getExperience() + LOGIN_EXPERIENCE);
            }
        } catch (IllegalAccessException | InvocationTargetException | ParseException e) {
            throw new RuntimeException(e);
        }
        userInfoDto.setToken(TokenUtils.getToken(user.getAccount(), user.getPassword()));
        userInfoDto.setState(this.userStateService.getUserStateById(user.getState()));
        if (userInfoDto.getLevel() < 6) {
            userInfoDto.setLevelUpExperience(this.levelService.getLevelUpExperienceByLevel(userInfoDto.getLevel() + 1));
            if (userInfoDto.getExperience() >= userInfoDto.getLevelUpExperience()) {
                userInfoDto.setLevel(userInfoDto.getLevel() + 1);
                this.userInfoService.updateLevelByUid(user.getUid());
                if (userInfoDto.getLevel() < 6) {
                    this.levelService.getLevelUpExperienceByLevel(userInfoDto.getLevel() + 1);
                } else {
                    userInfoDto.setLevelUpExperience(0);
                }
            }
        } else {
            userInfoDto.setLevelUpExperience(0);
        }
        return userInfoDto;
    }

    public User getUserByAccount(String account) {
        return this.userDao.getUserByAccount(account);
    }

    @Override
    public UserInfoDto insertUserByEnrollUserInfo(EnrollUserInfo enrollUserInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        if (0!=this.userDao.getUserCountByAccount(enrollUserInfo.getAccount())){
            throw new ServiceException("当前用户已注册，请更换用户名");
        }
        map.put("account", enrollUserInfo.getAccount());
        map.put("password", passwordMd5Encrypt(enrollUserInfo.getPassword()));
        map.put("lastLogin", sdf.format(new Date()));
        map.put("state", 1);
        if (0 == this.userDao.insertUser(map)) {
            throw new ServiceException("注册失败,请稍后再试，或联系管理员");
        }
        User user = this.userDao.getUserByAccount(enrollUserInfo.getAccount());
        UserInfo userInfo=new UserInfo();
        userInfo.setUid(user.getUid());
        userInfo.setUname(enrollUserInfo.getUname());
        userInfo.setAvatar(this.fileChunksService.upAvatar(enrollUserInfo.getFile(), String.valueOf(user.getUid())));
        userInfo.setExperience(10);
        userInfo.setLevel(1);
        userInfo.setDescribes(enrollUserInfo.getDescribes());
        userInfo.setVip(false);
        userInfo.setConcerns(0);
        userInfo.setFans(0);
        if (0==this.userInfoService.insertUserInfoByUserInfo(userInfo)){
            throw new ServiceException("注册失败,请稍后再试，或联系管理员");
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        try {
            BeanUtils.copyProperties(userInfoDto, this.userInfoService.getUserInfoByUid(user.getUid()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        userInfoDto.setToken(TokenUtils.getToken(user.getAccount(), user.getPassword()));
        userInfoDto.setState(this.userStateService.getUserStateById(user.getState()));
        userInfoDto.setLevelUpExperience(this.levelService.getLevelUpExperienceByLevel(userInfoDto.getLevel()+1));
        userInfoDto.setLastLogin(user.getLastLogin());
        return userInfoDto;
    }

    @Override
    public User getUserByUid(int uid) {
        return this.userDao.getUserByUid(uid);
    }

    @Override
    public UserInfo getUserInfoByUid(int uid) {
        return this.userInfoService.getUserInfoByUid(uid);
    }

    @Override
    public boolean getConcernsStateByUid(int uid) {
        User user=RequestInfo.getUser();
        if (user.getUid()==uid){
            return false;
        }
        return 0!=this.concernsService.getConcernsStateByUidAndFansUid(uid,user.getUid());
    }

    @Override
    public int updateConcernByConcernsOperate(ConcernsOperate concernsOperate) {
        User user= RequestInfo.getUser();
        if (user.getUid()==concernsOperate.getUid()){
            throw new ServiceException("不能关注自己");
        }
        int x=this.concernsService.getConcernsStateByUidAndFansUid(concernsOperate.getUid(),user.getUid());
        int n=0;
        if ("add".equals(concernsOperate.getType())&&x==0) {
            n=this.concernsService.insertConcernsByUidAndFansUid(concernsOperate.getUid(),user.getUid());
        }else if ("cancel".equals(concernsOperate.getType())&&x!=0){
            n=this.concernsService.deleteConcernsByUidAndFansUid(concernsOperate.getUid(),user.getUid());
        }else{
            throw new ServiceException("数据错误");
        }
        if (n!=0){
            this.userInfoService.updateConcernsCountByUidAndType(user.getUid(),concernsOperate.getType());
            this.userInfoService.updateFansCountByUidAndType(concernsOperate.getUid(),concernsOperate.getType());
        }
        return n;
    }

    @Override
    public List<User> getUserByUidList(List<Integer> userIdList) {
        return this.userDao.getUserByUidList(userIdList);
    }

    @Override
    public boolean updateUserStateByUidAndState(UpdateUserState updateUserState) {
        Map<String,Object> map=new HashMap<>();
        map.put("state",updateUserState.getState());
        map.put("uid",updateUserState.getUid());
        return 0!=this.userDao.updateUserStateByUidAndState(map);
    }

    @Override
    public int deleteUserByUid(Integer uid) {
        return this.userDao.deleteUserByUid(uid);
    }

    @Override
    public List<Integer> getConcernUidListByUid(int uid) {
        return this.concernsService.getConcernUidListByUid(uid);
    }

    @Override
    public UserInfoDto updateUserInfoByUserInfo(UserInfoEdit userInfoEdit) {
        if (!ObjectUtils.isEmpty(userInfoEdit.getFile()) && userInfoEdit.getFile().getSize() >= 0) {
            userInfoEdit.setAvatar(this.fileChunksService.reviseAvatar(userInfoEdit.getFile(), this.userInfoService.getUserInfoByUid(RequestInfo.getUser().getUid())));
        }
        userInfoEdit.setUid(RequestInfo.getUser().getUid());
        this.userInfoService.updateUserInfoByUserInfo(userInfoEdit);
        UserInfoDto userInfoDto=new UserInfoDto();
        try {
            BeanUtils.copyProperties(userInfoDto, this.userInfoService.getUserInfoByUid(RequestInfo.getUser().getUid()));
            User user=this.userDao.getUserByUid(RequestInfo.getUser().getUid());
            userInfoDto.setToken(TokenUtils.getToken(user.getAccount(), user.getPassword()));
            userInfoDto.setState(this.userStateService.getUserStateById(user.getState()));
            userInfoDto.setLevelUpExperience(this.levelService.getLevelUpExperienceByLevel(userInfoDto.getLevel()+1));
            userInfoDto.setLastLogin(user.getLastLogin());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ServiceException("修改信息错误");
        }
        return userInfoDto;
    }


}
