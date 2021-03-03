package com.libii.sso.common.model;

/**
 * 常量
 */
public interface Constant {

    /**
     * 时间格式化
     */
    String DATE_FORMATTER_TIME = "YYYY-MM-dd HH:mm:ss";
    String DATE_FORMATTER_DATE = "YYYY-MM-dd";

    /**
     * 公共状态： 启用、未启用
     */
    int STATUS_ENABLE = 1;
    int STATUS_DISABLE = 0;

    int STATUS_TEST = 1;
    int STATUS_PROD = 2;

    /**
     * 登录用户的权限信息
     */
    String REDIS_PERM_KEY_PREFIX = "security:user:permissions:";


    /**
     * 选择: 全选、半选、不选
     */
    String PROJECT = "unity_up";

    String ADD = "add";
    String REMOVE = "remove";
}
