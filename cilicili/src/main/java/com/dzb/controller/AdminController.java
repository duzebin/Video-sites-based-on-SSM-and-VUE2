package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.controller.request.*;
import com.dzb.entity.Admin;
import com.dzb.entity.Notice;
import com.dzb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/Admin")
public class AdminController {
    @Autowired private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody Admin loginadmin){
        return Result.success(this.adminService.login(loginadmin));
    }

    @GetMapping("/getAllCommentsListPage")
    public Result getAllCommentsListPage(AdminCommentsSearchPage adminCommentsSearchPage){
        return Result.success(this.adminService.getAllCommentsListPage(adminCommentsSearchPage));
    }
    @GetMapping("/getUserInfoByUid")
    public Result getUserInfoByUid(int uid){
        return Result.success(this.adminService.getUserInfoByUid(uid));
    }
    @GetMapping("/getVideoByVid")
    public Result getVideoByVid(int vid){
        return Result.success(this.adminService.getVideoByVid(vid));
    }

    @DeleteMapping("/deleteCommentsById/{commentsId}")
    public Result deleteCommentsById(@PathVariable Integer commentsId){
        return Result.success(this.adminService.deleteCommentsById(commentsId));
    }
    @GetMapping("/getAllAudit")
    public Result getAllAudit(){
        return Result.success(this.adminService.getAllAudit());
    }

    @PostMapping("/passComments/{Id}")
    public Result passComments(@PathVariable Integer Id){
        return Result.success(this.adminService.passComments(Id));
    }

    @GetMapping("/getAllVideoListPage")
    public Result getAllVideoListPage(AdminVideoSearchPage adminVideoSearchPage){
        return Result.success(this.adminService.getAllVideoListPage(adminVideoSearchPage));
    }

    @GetMapping("/getAllPartition")
    public Result getAllPartition(){
        return Result.success(this.adminService.getAllPartition());
    }

    @DeleteMapping("/deleteVideoByVid/{vid}")
    public Result deleteVideoByVid(@PathVariable Integer vid){
        return Result.success(this.adminService.deleteVideoByVid(vid));
    }

    @GetMapping("/getVideoTagByVid")
    public Result getVideoTagByVid(int vid){
        return Result.success(this.adminService.getVideoTagByVid(vid));
    }

    @PostMapping("/videoNotPass")
    public Result videoNotPass(@RequestBody VideoAudit videoAudit){
        return Result.success(this.adminService.videoNotPass(videoAudit));
    }

    @PostMapping("/videoPass")
    public Result videoPass(@RequestBody VideoAudit videoAudit){
        return Result.success(this.adminService.videoPass(videoAudit));
    }

    @GetMapping("/getVideoInfoByVid")
    public Result getVideoInfoByVid(int vid){
        return Result.success(this.adminService.getVideoInfoByVid(vid));
    }

    @GetMapping("getAllUserInfoListPage")
    public Result getAllUserInfoListPage(AdminUserSearchPage adminUserSearchPage){
        return Result.success(this.adminService.getAllUserInfoListPage(adminUserSearchPage));
    }

    @GetMapping("/getAllUserState")
    public Result getAllUserState(){
        return Result.success(this.adminService.getAllUserState());
    }

    @PostMapping("updateUserStateByUidAndState")
    public Result updateUserStateByUidAndState(@RequestBody UpdateUserState updateUserState){
        return Result.success(this.adminService.updateUserStateByUidAndState(updateUserState));
    }

    @DeleteMapping("/deleteUserByUid/{uid}")
    public Result deleteUserByUid(@PathVariable Integer uid){
        return Result.success(this.adminService.deleteUserByUid(uid));
    }

    @GetMapping("/getAllAdminPrivilegesList")
    public Result getAllAdminPrivilegesList(){
        return Result.success(this.adminService.getAllAdminPrivilegesList());
    }

    @PostMapping("/Enroll")
    public Result Enroll(@RequestBody Admin admin){
        return Result.success(this.adminService.insertAdminByAdmin(admin));
    }

    @GetMapping("/getAllAdminListPage")
    public Result getAllAdminListPage(AdminListSearchPage adminListSearchPage){
        return Result.success(this.adminService.getAllAdminListPage(adminListSearchPage));
    }

    @PostMapping("/updateAdminPrivilegesByIdAndPrivileges")
    public Result updateAdminPrivilegesByIdAndPrivileges(@RequestBody AdminUpdatePrivileges adminUpdatePrivileges){
        return Result.success(this.adminService.updateAdminPrivilegesByIdAndPrivileges(adminUpdatePrivileges));
    }

    @DeleteMapping("/deleteAdminById/{id}")
    public Result deleteAdminById(@PathVariable Integer id){
        return Result.success(this.adminService.deleteAdminById(id));
    }

    @GetMapping("/getTheVideoForReviewByTime")
    public Result getTheVideoForReviewByTime(String time){
        return Result.success(this.adminService.getTheVideoForReviewByTime(time));
    }

    @GetMapping("/getTheCommentsForReviewByTime")
    public Result getTheCommentsForReviewByTime(String time){
        return Result.success(this.adminService.getTheCommentsForReviewByTime(time));
    }

    @GetMapping("/getTheVideoForReview")
    public Result getTheVideoForReview(){
        return Result.success(this.adminService.getTheVideoForReview());
    }

    @GetMapping("/getTheCommentsForReview")
    public Result getTheCommentsForReview(){
        return Result.success(this.adminService.getTheCommentsForReview());
    }

    @GetMapping("/getAllNoticeListPage")
    public Result getAllNoticeListPage(NoticeListPage noticeListPage){
        return Result.success(this.adminService.getAllNoticeListPage(noticeListPage));
    }

    @GetMapping("/getWebStatisticsByTimeRange")
    public Result getWebStatisticsByTimeRange(String timeRange){
        return Result.success(this.adminService.getWebStatisticsByTimeRange(timeRange));
    }

    @PostMapping("/upNotice")
    public Result upNotice(@RequestBody Notice notice){
        return Result.success(this.adminService.upNotice(notice));
    }

    @DeleteMapping("/deleteNoticeById/{id}")
    public Result deleteNoticeById(@PathVariable int id){
        return Result.success(this.adminService.deleteNoticeById(id));
    }
}
