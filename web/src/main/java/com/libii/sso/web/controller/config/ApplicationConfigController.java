package com.libii.sso.web.controller.config;

import com.libii.sso.common.config.ApplicationConfig;
import com.libii.sso.common.restResult.RestResult;
import com.libii.sso.common.restResult.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lirong
 * @Date: 2019-8-19 15:11:35
 */
@RestController
@RequestMapping("/config")
public class ApplicationConfigController {

    @Autowired
    ApplicationConfig applicationConfig;

    /**
     * 返回配置信息
     */
    @GetMapping(value = "/info")
    public RestResult info() {
        return ResultGenerator.genSuccessResult(applicationConfig);
    }

}
