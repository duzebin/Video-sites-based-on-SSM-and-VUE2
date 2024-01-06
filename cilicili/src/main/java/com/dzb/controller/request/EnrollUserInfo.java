package com.dzb.controller.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EnrollUserInfo {
    private String uname;
    private String account;
    private String password;
    private MultipartFile file;
    private String describes;
}
