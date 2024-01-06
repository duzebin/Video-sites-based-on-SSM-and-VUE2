package com.dzb.controller;

import com.dzb.common.Result;
import com.dzb.controller.request.FileChunksInfo;
import com.dzb.service.FileChunksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/File")
public class FileController {

    @Autowired private FileChunksService fileChunksService;

}
