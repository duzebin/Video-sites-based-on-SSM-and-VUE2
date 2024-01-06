package com.dzb.dao;

import com.dzb.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagDao {
    int insertTagByTagList(@Param("tagList")List<String> tagList);

    List<Tag> getTagListByTagList(@Param("tagList")List<String> tagList);


    List<Integer> getTagIdListByTagList(@Param("tagList")List<String> tagList);

    List<String> getTagNameListByTagList(@Param("tagList")List<String> tagList);
}
