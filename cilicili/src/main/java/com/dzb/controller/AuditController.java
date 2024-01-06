package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Audit")
public class AuditController {
    @Autowired private AuditService auditService;

    @GetMapping("/getAllAudit")
    public Result getAllAudit(){
        return Result.success(this.auditService.getAllAudit());
    }
}
