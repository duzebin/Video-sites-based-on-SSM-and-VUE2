package com.dzb.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.dzb.common.RequestInfo;
import com.dzb.controller.dto.*;
import com.dzb.controller.request.*;
import com.dzb.dao.AdminDao;
import com.dzb.entity.*;
import com.dzb.exception.ServiceException;
import com.dzb.service.*;
import com.dzb.utils.TokenUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final String SALT = "ADMIN_DUZEBIN";
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private AdminPrivilegesService adminPrivilegesService;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStateService userStateService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private PartitionService partitionService;
    @Autowired
    private VideoTagService videoTagService;
    @Autowired
    private VideoFilesService videoFilesService;
    @Autowired
    private FileChunksService fileChunksService;
    @Autowired
    private ConcernsService concernsService;
    @Autowired
    private VideoFavoritesService videoFavoritesService;
    @Autowired
    private VideoLikeService videoLikeService;
    @Autowired
    private EpisodeService episodeService;
    @Autowired
    private NoticeService noticeService;
    @Autowired private VideoMessageService videoMessageService;

    private String passwordMd5Encrypt(String password) {
        String saltPassword = password + SALT;
        return DigestUtils.md5DigestAsHex(saltPassword.getBytes());
    }

    private List<String> dateTimeToDateString(List<DateTime> dateTimeList) {
        List<String> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(dateTimeList)) {
            return list;
        }
        for (DateTime dateTime : dateTimeList) {
            String date = DateUtil.formatDate(dateTime);
            list.add(date);
        }
        return list;
    }

    private List<Integer> countList(List<DateAndCountDto> dateAndCountDtoList, List<String> dateStringRange) {
        List<Integer> retList = new ArrayList<>();
        if (dateAndCountDtoList.size() == 0) {
            return retList;
        }
        for (String s : dateStringRange) {
            boolean flag=true;
            for (DateAndCountDto dateAndCountDto : dateAndCountDtoList) {
                if (s.equals(dateAndCountDto.getDate())) {
                    retList.add(dateAndCountDto.getCount());
                    flag=false;
                    break;
                }
            }
            if (flag){
                retList.add(0);
            }
        }
        return retList;
    }

    @Override
    public Admin getAdminByAccount(String adminAccount) {
        return this.adminDao.getAdminByAccount(adminAccount);
    }

    @Override
    public AdminDto login(Admin loginadmin) {
        AdminDto adminDto = new AdminDto();
        loginadmin.setPassword(passwordMd5Encrypt(loginadmin.getPassword()));
        Admin admin = this.adminDao.getAdminByAccountAndPassword(loginadmin);
        if (admin == null) {
            throw new ServiceException("管理员账号或密码错误");
        }
        adminDto.setId(admin.getId());
        adminDto.setName(admin.getName());
        adminDto.setPrivileges(admin.getPrivileges());
        adminDto.setPrivilegesName(this.adminPrivilegesService.getAdminPrivilegesByPrivilegesId(admin.getPrivileges()));
        adminDto.setToken(TokenUtils.getToken(admin.getAccount(), admin.getPassword()));
        return adminDto;
    }

    @Override
    public Map<String, Object> getAllCommentsListPage(AdminCommentsSearchPage adminCommentsSearchPage) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 3);
        RequestInfo.judgePrivileges(ActionablePermissions);

        List<Comments> commentsList = this.commentsService.getAllCommentsListPage(adminCommentsSearchPage);
        List<Audit> auditList = this.auditService.getAllAudit();
        Map<Integer, String> auditMap = new HashMap<>();
        for (int i = 0; i < auditList.size(); i++) {
            auditMap.put(auditList.get(i).getId(), auditList.get(i).getName());
        }
        List<CommentsDto> commentsDtoList = new ArrayList<>();
        for (int i = 0; i < commentsList.size(); i++) {
            CommentsDto commentsDto = new CommentsDto();
            try {
                BeanUtils.copyProperties(commentsDto, commentsList.get(i));
                commentsDto.setStateName(auditMap.get(commentsDto.getState()));
                commentsDtoList.add(commentsDto);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("commentsList", commentsDtoList);
        map.put("commentsCount", this.commentsService.getAllCommentsCount(adminCommentsSearchPage));
        return map;
    }

    @Override
    public UserInfoDto getUserInfoByUid(int uid) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 2, 3, 4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        UserInfo userInfo = this.userInfoService.getUserInfoByUid(uid);
        UserInfoDto userInfoDto = new UserInfoDto();
        try {
            BeanUtils.copyProperties(userInfoDto, userInfo);
            User user = this.userService.getUserByUid(userInfoDto.getUid());
            userInfoDto.setState(this.userStateService.getUserStateById(user.getState()));
            userInfoDto.setLastLogin(user.getLastLogin());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return userInfoDto;
    }

    @Override
    public Video getVideoByVid(int vid) {

        return this.videoService.getVideoByVid(vid);
    }

    @Override
    public int deleteCommentsById(Integer commentsId) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 3);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return this.commentsService.deleteCommentsById(commentsId);
    }

    @Override
    public List<Audit> getAllAudit() {
        List<Integer> ActionablePermissions = Arrays.asList(1, 2, 3);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return this.auditService.getAllAudit();
    }

    @Override
    public boolean passComments(int id) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 3);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return 0 != this.commentsService.updateCommentsStateByIdAndState(id, 1);
    }

    @Override
    public Map<String, Object> getAllVideoListPage(AdminVideoSearchPage adminVideoSearchPage) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 2);
        RequestInfo.judgePrivileges(ActionablePermissions);

        List<VideoInfoDto> videoInfoDtoList = this.videoService.getVideoPageList(adminVideoSearchPage);
        List<Audit> auditList = this.auditService.getAllAudit();
        Map<Integer, String> auditMap = new HashMap<>();
        for (Audit audit : auditList) {
            auditMap.put(audit.getId(), audit.getName());
        }
        for (VideoInfoDto videoInfoDto : videoInfoDtoList) {
            videoInfoDto.setStateName(auditMap.get(videoInfoDto.getState()));
        }
        Map<String, Object> videoListPage = new HashMap<>();
        videoListPage.put("videoList", videoInfoDtoList);
        videoListPage.put("videoCount", this.videoService.getAllVideoCount(adminVideoSearchPage));
        return videoListPage;
    }

    @Override
    public List<Partition> getAllPartition() {

        return this.partitionService.getAllPartitionList();
    }

    @Override
    public boolean deleteVideoByVid(Integer vid) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 2);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return this.videoService.deleteVideoByVid(vid);
    }

    @Override
    public List<VideoTagInfoDto> getVideoTagByVid(int vid) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 2);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return this.videoTagService.getVideoTagByVid(vid);
    }

    @Override
    public boolean videoNotPass(VideoAudit videoAudit) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 2);
        RequestInfo.judgePrivileges(ActionablePermissions);
        Map<String, Object> map = new HashMap<>();
        map.put("vid", videoAudit.getVid());
        map.put("state", 3);
        this.videoService.updateVideoStateByVid(map);
        map.put("aid",RequestInfo.getAdmin().getId());
        map.put("msg",videoAudit.getCause());
        map.put("uid",this.videoService.getVideoUidByVid(videoAudit.getVid()));
        map.put("time",new Date());
        this.videoMessageService.insertVideoMessage(map);
        return true;
    }

    @Override
    public boolean videoPass(VideoAudit videoAudit) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 2);
        RequestInfo.judgePrivileges(ActionablePermissions);

        Map<String, Object> map = new HashMap<>();
        map.put("vid", videoAudit.getVid());
        map.put("state", 1);
        this.videoService.updateVideoStateByVid(map);
        map.put("aid",RequestInfo.getAdmin().getId());
        map.put("msg","通过审核");
        map.put("uid",this.videoService.getVideoUidByVid(videoAudit.getVid()));
        map.put("time",new Date());
        this.videoMessageService.insertVideoMessage(map);
        return true;
    }

    @Override
    public VideoInfoDto getVideoInfoByVid(int vid) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 2);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return this.videoService.adminGetVideoInfoByVid(vid);
    }

    @Override
    public Map<String, Object> getAllUserInfoListPage(AdminUserSearchPage adminUserSearchPage) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        List<UserInfoDto> userInfoDtoList = this.userInfoService.getAllUserInfoListPage(adminUserSearchPage);
        if (userInfoDtoList.size() > 0) {
            List<Integer> userIdList = new ArrayList<>();
            for (UserInfoDto userInfoDto : userInfoDtoList) {
                userIdList.add(userInfoDto.getUid());
            }
            List<User> userList = this.userService.getUserByUidList(userIdList);
            Map<Integer, Object> map = new HashMap<>();
            for (User user : userList) {
                map.put(user.getUid(), user);
            }
            for (int i = 0; i < userInfoDtoList.size(); i++) {
                User user = (User) map.get(userInfoDtoList.get(i).getUid());
                userInfoDtoList.get(i).setLastLogin(user.getLastLogin());
                userInfoDtoList.get(i).setStateId(user.getState());
            }

        }

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("userList", userInfoDtoList);
        retMap.put("userCount", this.userInfoService.getAllUserInfoListCount(adminUserSearchPage));
        return retMap;
    }

    @Override
    public List<UserState> getAllUserState() {


        return this.userStateService.getAllUserState();
    }

    @Override
    public boolean updateUserStateByUidAndState(UpdateUserState updateUserState) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return this.userService.updateUserStateByUidAndState(updateUserState);
    }

    @Override
    public boolean deleteUserByUid(Integer uid) {
        List<Integer> ActionablePermissions = Arrays.asList(1, 4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        List<Integer> videoIdList = this.videoService.getVideoIdListByUid(uid);
        for (int i = 0; i < videoIdList.size(); i++) {
            this.videoService.deleteVideoByVid(videoIdList.get(i));
        }
        this.fileChunksService.deleteVideoFileByUid(uid);

        this.commentsService.deleteCommentsByUid(uid);

        List<Integer> fansIdList = this.concernsService.getFansIdListByUid(uid);
        this.userInfoService.updateUserConcernCountByUidList(fansIdList);

        List<Integer> concernsIdList = this.concernsService.getConcernsIdListByUid(uid);
        this.userInfoService.updateUserFansCountByUidList(concernsIdList);
        this.concernsService.deleteConcernsByUid(uid);

        videoIdList = this.videoFavoritesService.getVideoIdByUid(uid);
        this.videoService.updateVideoFavoritesByVideoIdList(videoIdList);
        this.videoFavoritesService.deleteFavoritesByUid(uid);

        videoIdList = this.videoLikeService.getVideoIdByUid(uid);
        this.videoService.updateVideoLikeByVideoIdList(videoIdList);
        this.videoLikeService.deleteVideoLikeByUid(uid);

        this.episodeService.deleteEpisodeByUid(uid);

        this.fileChunksService.deleteUserAvatar(uid);

        this.userInfoService.deleteUserInfoByUid(uid);
        this.userService.deleteUserByUid(uid);
        return true;
    }

    @Override
    public List<AdminPrivileges> getAllAdminPrivilegesList() {
        return this.adminPrivilegesService.getAllAdminPrivilegesList();
    }

    @Override
    public boolean insertAdminByAdmin(Admin admin) {
        List<Integer> ActionablePermissions = Arrays.asList(1);
        RequestInfo.judgePrivileges(ActionablePermissions);

        if (this.adminDao.getAdminByAccount(admin.getAccount()) != null) {
            throw new ServiceException("当前账号已被注册");
        }
        admin.setPassword(passwordMd5Encrypt(admin.getPassword()));
        return this.adminDao.insertAdminByAdmin(admin);
    }

    @Override
    public Map<String, Object> getAllAdminListPage(AdminListSearchPage adminListSearchPage) {
        List<Integer> ActionablePermissions = Arrays.asList(1);
        RequestInfo.judgePrivileges(ActionablePermissions);

        Map<String, Object> map = new HashMap<>();
        map.put("pageStart", (adminListSearchPage.getPageNum() - 1) * adminListSearchPage.getPageSize());
        map.put("pageSize", adminListSearchPage.getPageSize());
        map.put("id", adminListSearchPage.getId());
        map.put("name", adminListSearchPage.getName());
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("adminList", this.adminDao.getAllAdminListPage(map));
        retMap.put("adminCount", this.adminDao.getAllAdminListCount(map));
        return retMap;
    }

    @Override
    public boolean updateAdminPrivilegesByIdAndPrivileges(AdminUpdatePrivileges adminUpdatePrivileges) {
        List<Integer> ActionablePermissions = Arrays.asList(1);
        RequestInfo.judgePrivileges(ActionablePermissions);

        Map<String, Object> map = new HashMap<>();
        map.put("privileges", adminUpdatePrivileges.getPrivileges());
        map.put("id", adminUpdatePrivileges.getId());
        this.adminDao.updateAdminPrivilegesByIdAndPrivileges(map);
        return true;
    }

    @Override
    public boolean deleteAdminById(Integer id) {
        List<Integer> ActionablePermissions = Arrays.asList(1);
        RequestInfo.judgePrivileges(ActionablePermissions);

        this.videoMessageService.deleteVideoMessageByAId(id);
        this.noticeService.deleteNoticeByAid(id);
        return this.adminDao.deleteAdminById(id);
    }

    @Override
    public int getTheVideoForReviewByTime(String time) {
        List<Integer> ActionablePermissions = Arrays.asList(1,2,3,4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return this.videoService.getTheVideoForReviewByTime(sdf.parse(time));
        } catch (ParseException e) {
            throw new ServiceException("输入的日期存在问题");
        }
    }

    @Override
    public int getTheCommentsForReviewByTime(String time) {
        List<Integer> ActionablePermissions = Arrays.asList(1,2,3,4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return this.commentsService.getTheCommentsForReviewByTime(sdf.parse(time));
        } catch (ParseException e) {
            throw new ServiceException("输入的日期存在问题");
        }
    }

    @Override
    public int getTheVideoForReview() {
        List<Integer> ActionablePermissions = Arrays.asList(1,2,3,4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return this.videoService.getTheVideoForReview();
    }

    @Override
    public int getTheCommentsForReview() {
        List<Integer> ActionablePermissions = Arrays.asList(1,2,3,4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return this.commentsService.getTheCommentsForReview();
    }

    @Override
    public Map<String, Object> getAllNoticeListPage(NoticeListPage noticeListPage) {
        List<Integer> ActionablePermissions = Arrays.asList(1,2,3,4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        return this.noticeService.getAllNoticeListPage(noticeListPage);
    }

    @Override
    public Map<String, Object> getWebStatisticsByTimeRange(String timeRange) {
        List<Integer> ActionablePermissions = Arrays.asList(1,2,3,4);
        RequestInfo.judgePrivileges(ActionablePermissions);

        Map<String, Object> map = new HashMap<>();
        Date today = new Date();
        List<DateTime> dateTimeList = new ArrayList<>();
        if ("Week".equals(timeRange)) {
            dateTimeList = DateUtil.rangeToList(DateUtil.offsetDay(today, -6), today, DateField.DAY_OF_WEEK);
        } else if ("Month1".equals(timeRange)) {
            dateTimeList = DateUtil.rangeToList(DateUtil.offsetDay(today, -29), today, DateField.DAY_OF_MONTH);
        } else if ("Month2".equals(timeRange)) {
            dateTimeList = DateUtil.rangeToList(DateUtil.offsetDay(today, -59), today, DateField.DAY_OF_MONTH);
        } else if ("Month3".equals(timeRange)) {
            dateTimeList = DateUtil.rangeToList(DateUtil.offsetDay(today, -89), today, DateField.DAY_OF_MONTH);
        }
        List<String> dateStringRange = dateTimeToDateString(dateTimeList);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("date", dateStringRange);
        List<DateAndCountDto> videoUploadData = this.videoService.getVideoUploadDataByTimeRange(timeRange);
        List<DateAndCountDto> commentsUpDate = this.commentsService.getCommentsUpDateByTimeRange(timeRange);
        List<DateAndCountDto> videoAuditDate = this.videoMessageService.getVideoAuditDateByTimeRange(timeRange);
        retMap.put("videoUploadData", countList(videoUploadData, dateStringRange));
        retMap.put("commentsUpDate", countList(commentsUpDate, dateStringRange));
        retMap.put("videoAuditDate", countList(videoAuditDate, dateStringRange));
        return retMap;
    }

    @Override
    public boolean upNotice(Notice notice) {
        List<Integer> ActionablePermissions = Arrays.asList(1);
        RequestInfo.judgePrivileges(ActionablePermissions);

        notice.setUptime(new Date());
        notice.setAid(RequestInfo.getAdmin().getId());
        return this.noticeService.insertNoticeByNotice(notice);
    }

    @Override
    public boolean deleteNoticeById(int id) {
        return 0!=this.noticeService.deleteNoticeById(id);
    }


}
