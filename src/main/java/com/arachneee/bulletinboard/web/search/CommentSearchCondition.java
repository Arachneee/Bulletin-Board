package com.arachneee.bulletinboard.web.search;

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
