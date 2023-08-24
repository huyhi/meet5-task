package com.hongye.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    VALID(0),
    FRAUD(1);

    private final int code;

}
