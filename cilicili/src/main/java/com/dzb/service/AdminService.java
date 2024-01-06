package com.dzb.service;

import com.dzb.common.Result;
import com.dzb.controller.dto.UserInfoDto;
import com.dzb.controller.dto.AdminDto;
import com.dzb.controller.dto.VideoInfoDto;
import com.dzb.controller.dto.VideoTagInfoDto;
import com.dzb.controller.request.*;
import com.dzb.entity.*;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Admin getAdminByAccount(String adminAccount);

    AdminDto login(Admin loginadmin);


    Map<String,Object> getAllCommentsListPage(AdminCommentsSearchPage adminCommentsSearchPage);

    UserInfoDto getUserInfoByUid(int uid);

    Video getVideoByVid(int vid);

    int deleteCommentsById(Integer commentsId);

    List<Audit> getAllAudit();

    boolean passComments(int id);

    Map<String,Object> getAllVideoListPage(AdminVideoSearchPage adminVideoSearchPage);

    List<Partition> getAllPartition();

    boolean deleteVideoByVid(Integer vid);

    List<VideoTagInfoDto> getVideoTagByVid(int vid);

    boolean videoNotPass(VideoAudit videoAudit);

    boolean videoPass(VideoAudit videoAudit);

    VideoInfoDto getVideoInfoByVid(int vid);

    Map<String,Object> getAllUserInfoListPage(AdminUserSearchPage adminUserSearchPage);

    List<UserState> getAllUserState();

    boolean updateUserStateByUidAndState(UpdateUserState updateUserState);

    boolean deleteUserByUid(Integer uid);

    List<AdminPrivileges> getAllAdminPrivilegesList();

    boolean insertAdminByAdmin(Admin admin);

    Map<String,Object> getAllAdminListPage(AdminListSearchPage adminListSearchPage);

    boolean updateAdminPrivilegesByIdAndPrivileges(AdminUpdatePrivileges adminUpdatePrivileges);

    boolean deleteAdminById(Integer id);

    int getTheVideoForReviewByTime(String time);

    int getTheCommentsForReviewByTime(String time);

    int getTheVideoForReview();

    int getTheCommentsForReview();

    Map<String,Object> getAllNoticeListPage(NoticeListPage noticeListPage);

    Map<String,Object> getWebStatisticsByTimeRange(String timeRange);

    boolean upNotice(Notice notice);

    boolean deleteNoticeById(int id);
}
