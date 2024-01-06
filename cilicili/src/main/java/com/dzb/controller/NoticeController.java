package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.controller.request.NoticeListPage;
import com.dzb.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Notice")
public class NoticeController {
    @Autowired private NoticeService noticeService;

    @GetMapping("/getAllNoticeListPage")
    public Result getAllNoticeListPage(NoticeListPage noticeListPage){
        return Result.success(this.noticeService.getAllNoticeListPage(noticeListPage));
    }

}
