package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.entity.Publicize;
import com.dzb.service.PublicizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Publicize")
public class PublicizeController {
    @Autowired private PublicizeService publicizeService;

    @GetMapping("/getPublicizeByType")
    public Result getPublicizeByType( String type){
        return Result.success(this.publicizeService.getPublicizeByType(type));
    }

}
