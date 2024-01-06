package com.dzb.dao;

import com.dzb.controller.dto.AdminDto;
import com.dzb.entity.Admin;

import java.util.List;
import java.util.Map;

public interface AdminDao {
    Admin getAdminByAccount(String adminAccount);

    Admin getAdminByAccountAndPassword(Admin loginadmin);

    boolean insertAdminByAdmin(Admin admin);

    List<AdminDto> getAllAdminListPage(Map<String, Object> map);

    int getAllAdminListCount(Map<String, Object> map);

    int updateAdminPrivilegesByIdAndPrivileges(Map<String, Object> map);

    boolean deleteAdminById(Integer id);
}
