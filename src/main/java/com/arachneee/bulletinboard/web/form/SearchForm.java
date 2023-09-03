package com.arachneee.bulletinboard.web.form;

import lombok.Data;

@Data
public class SearchForm {
    private String searchCode = "TITLE";
    private String searchString = "";
    private String sortCode = "NEW";

    @Override
    public String toString() {
        return "SearchForm{" +
            "searchCode='" + searchCode + '\'' +
            ", searchString='" + searchString + '\'' +
            ", sortCode='" + sortCode + '\'' +
            '}';
    }
}
