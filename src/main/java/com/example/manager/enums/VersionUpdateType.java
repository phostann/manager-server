package com.example.manager.enums;

import lombok.Getter;

@Getter
public enum VersionUpdateType {
    MAJOR(1, "大版本"),
    MINOR(2, "小版本"),
    PATCH(3, "修复版本");

    private final int code;
    private final String description;

    VersionUpdateType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static VersionUpdateType fromCode(int code) {
        for (VersionUpdateType type : VersionUpdateType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
