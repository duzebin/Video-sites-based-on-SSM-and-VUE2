package com.dzb.service.Impl;

import com.dzb.dao.AdminPrivilegesDao;
import com.dzb.entity.AdminPrivileges;
import com.dzb.service.AdminPrivilegesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminPrivilegesServiceImpl implements AdminPrivilegesService {
    @Autowired AdminPrivilegesDao adminPrivilegesDao;

    @Override
    public String getAdminPrivilegesByPrivilegesId(int privileges) {
        return this.adminPrivilegesDao.getAdminPrivilegesByPrivilegesId(privileges);
    }

    @Override
    public List<AdminPrivileges> getAllAdminPrivilegesList() {
        return this.adminPrivilegesDao.getAllAdminPrivilegesList();
    }
}
