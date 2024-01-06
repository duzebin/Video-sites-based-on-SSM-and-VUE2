package com.dzb.service.Impl;

import com.dzb.dao.TagDao;
import com.dzb.entity.Tag;
import com.dzb.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Override
    public int insertTagByTagList(List<String> tagList) {
        return this.tagDao.insertTagByTagList(tagList);
    }

    @Override
    public List<Tag> getTagListByTagList(List<String> tagList) {
        return this.tagDao.getTagListByTagList(tagList);
    }

    @Override
    public List<Integer> getTagIdListByTagList(List<String> tagList) {
        return this.tagDao.getTagIdListByTagList(tagList);
    }

    @Override
    public List<String> getTagNameListByTagList(List<String> tagList) {
        return this.tagDao.getTagNameListByTagList(tagList);
    }

    @Override
    public List<Integer> insertAndGetTagIdByTagList(List<String> tagList) {
        if (tagList.size() == 0) {
            return new ArrayList<>();
        }
        Set<String> set = new HashSet<>();
        set.addAll(tagList);
        List<String> newTagList = new ArrayList<>();
        newTagList.addAll(set);
        tagList = newTagList;
        List<String> tagNameList = this.tagDao.getTagNameListByTagList(tagList);
        Iterator<String> it = tagList.iterator();
        while (it.hasNext()) {
            String str = it.next();
            tagNameList.forEach(item -> {
                if (str.equals(item)) {
                    it.remove();
                }
            });

        }
        if (tagList.size() > 0) {
            this.tagDao.insertTagByTagList(tagList);
            tagNameList.addAll(tagList);
        }
        return this.tagDao.getTagIdListByTagList(tagNameList);
    }


}
