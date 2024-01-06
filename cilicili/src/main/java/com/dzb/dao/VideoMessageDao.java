package com.dzb.dao;

import com.dzb.controller.dto.DateAndCountDto;
import com.dzb.controller.dto.VideoMessageDto;
import com.dzb.entity.VideoMessage;

import java.util.List;
import java.util.Map;

public interface VideoMessageDao {
    int insertVideoMessage(Map<String, Object> map);

    List<VideoMessageDto> getVideoMessageListPageByUid(Map<String, Object> map);

    int getVideoMessageListCountByUid(Map<String, Object> map);

    int deleteVideoMessageByVid(Integer vid);

    int deleteVideoMessageByAId(Integer id);

    List<DateAndCountDto> getVideoAuditDateByTimeRange(String timeRange);

    List<DateAndCountDto> getUserVideoAuditDataByTimeRangeAndAuditId(Map<String, Object> map);
}
