package com.dzb.dao;

import com.dzb.entity.UserState;

import java.util.List;

public interface UserStateDao {
    String getUserStateById(int uid);

    List<UserState> getAllUserState();
}
