package com.hongye.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserActionTypeEnum {

    VISIT(0),
    LIKE(1);

    private final int code;

}
