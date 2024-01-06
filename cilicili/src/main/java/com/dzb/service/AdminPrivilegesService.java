package com.dzb.service;

import com.dzb.entity.AdminPrivileges;

import java.util.List;

public interface AdminPrivilegesService {
    String getAdminPrivilegesByPrivilegesId(int privileges);

    List<AdminPrivileges> getAllAdminPrivilegesList();
}
