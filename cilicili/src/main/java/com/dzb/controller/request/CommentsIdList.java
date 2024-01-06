package com.dzb.controller.request;

import lombok.Data;

import java.util.List;


@Data
public class CommentsIdList {
    private int cid;
    private List<Integer> childCommentsList;
}
