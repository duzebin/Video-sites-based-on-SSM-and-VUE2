package com.dzb.service;

import com.dzb.entity.Tag;

import java.util.List;

public interface TagService {
    int insertTagByTagList(List<String> tagList);

    List<Tag> getTagListByTagList(List<String> tagList);


    List<Integer> getTagIdListByTagList(List<String> tagList);

    List<String> getTagNameListByTagList(List<String> tagList);

    List<Integer> insertAndGetTagIdByTagList(List<String> tagNameList);
}
