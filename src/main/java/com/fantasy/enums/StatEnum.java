package com.fantasy.enums;

import lombok.Getter;

@Getter
public enum StatEnum {
    UNREAD(0, "未读"),
    UNREVIEW(1, "未审"),
    APPROVE(2, "批准"),
    SENDBACK(3, "退回");

    private final int code;
    private final String name;

    StatEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}