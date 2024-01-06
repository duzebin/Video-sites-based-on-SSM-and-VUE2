package com.dzb.controller.dto;

import com.dzb.entity.Comments;
import com.dzb.entity.UserInfo;
import lombok.Data;

import java.util.List;

@Data
public class CommentsInfoDto extends Comments {
    private UserInfo userInfo;
    private List<Comments> childCommentsList;
    private int childCommentsCount;
}
