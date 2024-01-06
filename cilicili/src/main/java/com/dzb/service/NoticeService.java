package com.dzb.service;

import com.dzb.controller.request.NoticeListPage;
import com.dzb.entity.Notice;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    Map<String,Object> getAllNoticeListPage(NoticeListPage noticeListPage);

    int deleteNoticeByAid(Integer id);

    boolean insertNoticeByNotice(Notice notice);

    int deleteNoticeById(int id);
}
