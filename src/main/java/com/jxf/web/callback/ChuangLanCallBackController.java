package com.jxf.web.callback;

import com.jxf.svc.cache.RedisUtils;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@RequestMapping(value = "/callback/chuanglan")
public class ChuangLanCallBackController {

    @GetMapping
    public String callback(String mobile) {
        if (StringUtils.isNotBlank(mobile)) {
            String verifyCode = mobile.substring(7);
            RedisUtils.setForTimeMIN("smsCode" + mobile, verifyCode, 10);
        }
        return "success";
    }

}