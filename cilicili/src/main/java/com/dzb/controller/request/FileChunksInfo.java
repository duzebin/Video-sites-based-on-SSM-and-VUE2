package com.dzb.controller.request;

import com.dzb.entity.FileChunks;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileChunksInfo extends FileChunks {
    private MultipartFile file;
}
