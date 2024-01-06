package com.dzb.service.Impl;

import com.dzb.controller.request.NoticeListPage;
import com.dzb.dao.NoticeDao;
import com.dzb.entity.Notice;
import com.dzb.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {
    @Autowired private NoticeDao noticeDao;

    @Override
    public Map<String, Object> getAllNoticeListPage(NoticeListPage noticeListPage) {
        Map<String,Object> map=new HashMap<>();
        map.put("pageStart", (noticeListPage.getPageNum() - 1) * noticeListPage.getPageSize());
        map.put("pageSize", noticeListPage.getPageSize());
        Map<String,Object> retMap=new HashMap<>();
        retMap.put("noticeList",this.noticeDao.getAllNoticeListPage(map));
        retMap.put("noticeCount",this.noticeDao.getAllNoticeListCount(map));
        return retMap;
    }

    @Override
    public int deleteNoticeByAid(Integer id) {
        return this.noticeDao.deleteNoticeByAid(id);
    }

    @Override
    public boolean insertNoticeByNotice(Notice notice) {
        return 0!=this.noticeDao.insertNoticeByNotice(notice);
    }

    @Override
    public int deleteNoticeById(int id) {
        return this.noticeDao.deleteNoticeById(id);
    }
}
