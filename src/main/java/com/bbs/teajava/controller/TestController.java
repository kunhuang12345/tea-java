package com.bbs.teajava.controller;

import com.bbs.teajava.service.TestService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    TestService testService;
    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiResponse(description = "test")
    public ApiResultUtils test () {
        testService.test();
        return ApiResultUtils.success();
    }
}
