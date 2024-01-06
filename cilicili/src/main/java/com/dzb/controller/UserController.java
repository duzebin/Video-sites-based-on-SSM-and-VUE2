package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.controller.request.ConcernsOperate;
import com.dzb.controller.request.EnrollUserInfo;
import com.dzb.controller.request.UserInfoEdit;
import com.dzb.entity.User;
import com.dzb.entity.UserInfo;
import com.dzb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        return Result.success(this.userService.login(loginUser));
    }

    @PostMapping("/enroll")
    public Result enroll(EnrollUserInfo enrollUserInfo){
        return Result.success(this.userService.insertUserByEnrollUserInfo(enrollUserInfo));
    }

    @GetMapping("/getUserInfoByUid")
    public Result getUserInfoByUid(int uid) {
        return Result.success(this.userService.getUserInfoByUid(uid));
    }

    @GetMapping("/getConcernsStateByUid")
    public Result getConcernsStateByUid(int uid){
        return Result.success(this.userService.getConcernsStateByUid(uid));
    }

    @PostMapping("/updateConcernByConcernsOperate")
    public Result updateConcernByConcernsOperate(@RequestBody ConcernsOperate concernsOperate){
        return Result.success(this.userService.updateConcernByConcernsOperate(concernsOperate));
    }

    @PostMapping("/updateUserInfoByUserInfo")
    public Result updateUserInfoByUserInfo(UserInfoEdit userInfoEdit){
        return Result.success(this.userService.updateUserInfoByUserInfo(userInfoEdit));
    }
}
