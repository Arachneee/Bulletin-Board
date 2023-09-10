package com.arachneee.bulletinboard.web.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
