package com.libii.sso.security.util;

import com.alibaba.fastjson.JSON;
import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.restResult.ResultCode;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class ResponseUtils {

    public static void addResponseHeader(HttpServletRequest request, HttpServletResponse response, String[] origins) {
        String origin = request.getHeader("Origin");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader( "Access-Control-Allow-Headers", "Content-Type");
        // response.setHeader("Access-Control-Allow-Origin", "*");
        // if (Arrays.asList(origins).contains(origin)) {
            // todo 可以用 * 测试
        response.setHeader("Access-Control-Allow-Origin", origin);
        // }
    }


    public static void renderJson(HttpServletRequest request, HttpServletResponse response, ResultCode code, String[] origins) {
        renderJson(request, response, code, null, origins);
    }

    /**
     * 往 response 写出 json
     *
     * @param response 响应
     * @param code 状态
     * @param data     返回数据
     */
    public static void renderJson(HttpServletRequest request, HttpServletResponse response, ResultCode code, Object data, String[] origins) {
        try {
            addResponseHeader(request, response, origins);
            response.setStatus(200);
            //  将JSON转为String的时候，忽略null值的时候转成的String存在错误
            RestResult result = null != data ? new RestResult(code, data) : new RestResult(code);
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error("Response写出JSON异常，", ex);
        }
    }

    /**
     * 往 response 写出 json
     *
     * @param response 响应
     * @param result 返回数据
     */
    public static void renderSuccessJson(HttpServletRequest request, HttpServletResponse response, RestResult result, String[] origins) {
        try {
            addResponseHeader(request, response, origins);
            response.setStatus(200);
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error("Response写出JSON异常，", ex);
        }
    }
}
