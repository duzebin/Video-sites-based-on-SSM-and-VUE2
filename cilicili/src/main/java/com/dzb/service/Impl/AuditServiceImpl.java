package com.dzb.service.Impl;

import com.dzb.dao.AuditDao;
import com.dzb.entity.Audit;
import com.dzb.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {
    @Autowired private AuditDao auditDao;

    @Override
    public List<Audit> getAllAudit() {
        return this.auditDao.getAllAudit();
    }
}
