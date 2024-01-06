package com.dzb.service.Impl;

import com.dzb.dao.PublicizeDao;
import com.dzb.entity.Publicize;
import com.dzb.service.PublicizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PublicizeServiceImpl implements PublicizeService {
    @Autowired private PublicizeDao publicizeDao;

    public List<Publicize> getPublicizeByType(String type) {
        return this.publicizeDao.getPublicizeByType(type);
    }
}
