package com.dzb.service.Impl;

import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.controller.dto.VideoMessageDto;
import com.dzb.dao.VideoMessageDao;
import com.dzb.entity.VideoMessage;
import com.dzb.service.VideoMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VideoMessageServiceImpl implements VideoMessageService{
    @Autowired private VideoMessageDao videoMessageDao;

    @Override
    public int insertVideoMessage(Map<String, Object> map) {
        return this.videoMessageDao.insertVideoMessage(map);
    }

    @Override
    public List<VideoMessageDto> getVideoMessageListPageByUid(Map<String, Object> map) {
        return this.videoMessageDao.getVideoMessageListPageByUid(map);
    }

    @Override
    public int getVideoMessageListCountByUid(Map<String, Object> map) {
        return this.videoMessageDao.getVideoMessageListCountByUid(map);
    }

    @Override
    public int deleteVideoMessageByVid(Integer vid) {
        return this.videoMessageDao.deleteVideoMessageByVid(vid);
    }

    @Override
    public int deleteVideoMessageByAId(Integer id) {
        return this.videoMessageDao.deleteVideoMessageByAId(id);
    }

    @Override
    public List<DateAndCountDto> getVideoAuditDateByTimeRange(String timeRange) {
        return this.videoMessageDao.getVideoAuditDateByTimeRange(timeRange);
    }

    @Override
    public List<DateAndCountDto> getUserVideoAuditDataByTimeRangeAndAuditId(Map<String, Object> map) {
        return this.videoMessageDao.getUserVideoAuditDataByTimeRangeAndAuditId(map);
    }
}
