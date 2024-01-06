package com.dzb.dao;

import com.dzb.entity.Notice;

import java.util.List;
import java.util.Map;

public interface NoticeDao {
    List<Notice> getAllNoticeListPage(Map<String, Object> map);

    int getAllNoticeListCount(Map<String, Object> map);

    int deleteNoticeByAid(Integer id);

    int insertNoticeByNotice(Notice notice);

    int deleteNoticeById(int id);
}
