package com.wangguo.enums;

/**
 * 文件类型的枚举
 */
public enum FileTypeEnum {
    BGIMG(1, "用户背景图"),
    FACE(2, "用户头像");
    public final Integer type;
    public final String value;

    FileTypeEnum(Integer type, String value) {
        this.value = value;
        this.type = type;
    }
}
