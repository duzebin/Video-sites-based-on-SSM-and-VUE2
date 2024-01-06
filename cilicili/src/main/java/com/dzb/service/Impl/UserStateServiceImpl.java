package com.dzb.service.Impl;

import com.dzb.dao.UserStateDao;
import com.dzb.entity.UserState;
import com.dzb.service.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserStateServiceImpl implements UserStateService {
    @Autowired private UserStateDao userStateDao;


    @Override
    public String getUserStateById(int uid) {
        return this.userStateDao.getUserStateById(uid);
    }

    @Override
    public List<UserState> getAllUserState() {
        return this.userStateDao.getAllUserState();
    }
}
