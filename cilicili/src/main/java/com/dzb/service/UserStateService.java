package com.dzb.service;

import com.dzb.entity.UserState;

import java.util.List;

public interface UserStateService {

    String getUserStateById(int uid);

    List<UserState> getAllUserState();
}
