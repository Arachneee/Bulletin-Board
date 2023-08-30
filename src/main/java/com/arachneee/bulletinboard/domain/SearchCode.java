package com.arachneee.bulletinboard.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCode {

    private String code;
    private String displayName;
}
