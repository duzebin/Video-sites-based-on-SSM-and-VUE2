package com.dzb.controller.dto;

import com.dzb.entity.Comments;
import lombok.Data;

@Data
public class CommentsDto extends Comments {
    private String stateName;
}
