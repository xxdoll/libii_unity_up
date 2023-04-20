package com.libii.sso.common.restResult;

/**
 * @author lirong
 * @ClassName: ResultCode
 * @Description: 返回的状态码
 * @date 2018-12-03 9:26
 */
public enum ResultCode {

    /**
     * HTTP通信使用
     */
    SUCCESS(200, "SUCCESS"),

    ERROR(500, "服务器内部错误"),

    NOT_FOUND(404, "接口不存在"),

    FAIL(400, "接口异常,请联系管理员"),

    UNAUTHORIZED(401, "未认证（签名错误）"),

    /**
     * security
     */
    LOGIN_ERROR(-990, "登录失败"),

    OVER_MAX_LOGIN_NUM(-991, "当前在线人数过多，请稍后登录"),

    REFRESH_TOKEN_EXPIRED(-997, "用户 刷新令牌过期"),

    LIMITED_AUTHORITY(-1000, "权限不够"),

    UNLOGIN(-999, "用户未登录或登录已失效"),

    IP_NOT_ALLOW(-980, "登录的IP不被允许"),

    VERSION_IS_EXIST(2001, "此版本的项目已存在"),


    /**
     * 导入导出
     */
    DATA_IS_NULL(4901, "请选择数据导出"),

    IMPORT_FAIL(4902, "未知错误，导入失败"),

    FILE_MD5_FAIL(6002, "文件md5码生成失败"),

    FILE_UPLOAD_ERROR(6001, "资源包上传失败"),

    /**
     * 导入导出
     */
    DEVICE_ID_EXIST(5001, "设备编号已存在"),

    GAME_DEVICE_EXIST(5002, "当前设备白名单已存在"),

    ;



    public int code;

    public String message;

    ResultCode (int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }
}
