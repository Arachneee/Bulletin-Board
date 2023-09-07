package com.arachneee.bulletinboard.web.form;

import lombok.Data;

@Data
public class PostSearchForm {
    private String searchCode = "TITLE";
    private String searchString = "";
    private String sortCode = "NEW";

}
