package com.example.manager.utils;

import com.example.manager.enums.VersionUpdateType;

public class VersionUtils {

    // 校验版本号格式是否正确
    public static boolean isValidVersion(String version) {
        String regex = "^(\\d+)\\.(\\d+)\\.(\\d+)$";
        return version.matches(regex);
    }

    // 实现 semver 版本号工具类，实现版本号的比较、解析、升级等功能
    // 版本号格式：MAJOR.MINOR.PATCH
    // MAJOR 版本号：当你做了不兼容的 API 修改，
    // MINOR 版本号：当你做了向下兼容的功能性新增，
    // PATCH 版本号：当你做了向下兼容的问题修正
    // 版本号比较：返回值大于0，表示当前版本大于传入版本；返回值小于0，表示当前版本小于传入版本；返回值等于0，表示当前版本等于传入版本
    public static int compare(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int len = Math.max(v1.length, v2.length);
        for (int i = 0; i < len; i++) {
            int num1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int num2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;
            if (num1 != num2) {
                return num1 - num2;
            }
        }
        return 0;
    }

    // 版本号升级：根据传入的版本号和升级类型，返回升级后的版本号
    public static String upgrade(String version, VersionUpdateType type) {
        String[] v = version.split("\\.");
        int major = Integer.parseInt(v[0]);
        int minor = Integer.parseInt(v[1]);
        int patch = Integer.parseInt(v[2]);
        switch (type) {
            case MAJOR:
                major++;
                minor = 0;
                patch = 0;
                break;
            case MINOR:
                minor++;
                patch = 0;
                break;
            case PATCH:
                patch++;
                break;
            default:
                throw new IllegalArgumentException("Unknown update type: " + type);
        }
        return major + "." + minor + "." + patch;
    }
}
