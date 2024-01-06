package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.controller.request.PartitionPage;
import com.dzb.service.PartitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Partition")
public class PartitionController {
    @Autowired PartitionService partitionService;

    @GetMapping("/getPartitionList")
    public Result getPartitionList(PartitionPage partitionPage){
        return Result.success(this.partitionService.getPartitionList(partitionPage));
    }
    @GetMapping("/getAllPartitionList")
    public Result getAllPartitionList(){
        return Result.success(this.partitionService.getAllPartitionList());
    }
}
