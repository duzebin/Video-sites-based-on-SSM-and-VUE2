package com.dzb.controller.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserInfoEdit {
    private int uid;
    private String uname;
    private String describes;
    private MultipartFile file;
    private String avatar;
}
