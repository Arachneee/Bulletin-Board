package com.arachneee.bulletinboard.web.form;

import lombok.Data;

@Data
public class PostSearchForm {

    private String searchCode;
    private String searchString;
    private String sortCode;
    private Long page;

    public PostSearchForm() {
        searchCode = "TITLE";
        searchString = "";
        sortCode = "NEW";
        page = 1L;
    }

    public String toQueryString() {
        return "?searchCode=" + searchCode +
                "&searchString=" + searchString +
                "&sortCode=" + sortCode +
                "&page=" + page;
    }
}
