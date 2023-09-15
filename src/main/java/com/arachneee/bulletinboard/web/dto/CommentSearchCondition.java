package com.arachneee.bulletinboard.web.dto;

import lombok.Data;

@Data
public class CommentSearchCondition {

    private String commentSortCode;
    private Integer commentPage;

    public CommentSearchCondition() {
        commentSortCode = "NEW";
        commentPage = 1;
    }

    public String toQueryString() {
        return "commentSortCode=" + commentSortCode +
                "&commentPage=" + commentPage;
    }
}
