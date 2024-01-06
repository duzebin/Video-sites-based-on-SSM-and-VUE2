package com.dzb.service.Impl;

import com.dzb.dao.LevelDao;
import com.dzb.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LevelServiceImpl implements LevelService {
    @Autowired private LevelDao levelDao;

    public int getLevelUpExperienceByLevel(int level) {
        return this.levelDao.getLevelUpExperienceByLevel(level);
    }
}
