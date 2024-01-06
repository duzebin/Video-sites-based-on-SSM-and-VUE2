package com.dzb.dao;

import com.dzb.entity.AdminPrivileges;

import java.util.List;

public interface AdminPrivilegesDao {
    String getAdminPrivilegesByPrivilegesId(int privileges);

    List<AdminPrivileges> getAllAdminPrivilegesList();
}
