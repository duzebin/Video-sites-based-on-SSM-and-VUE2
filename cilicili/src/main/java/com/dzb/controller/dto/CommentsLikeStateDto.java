package com.dzb.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentsLikeStateDto {
    private boolean mainCommentsLikeState;
    private List<Boolean> childCommentsLikeState;
}
