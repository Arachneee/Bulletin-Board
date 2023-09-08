package com.arachneee.bulletinboard.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSearchForm {
    private String searchCode = "TITLE";
    private String searchString = "";
    private String sortCode = "NEW";
}
