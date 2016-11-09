package cn.com.xiao8.pay.alipay.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiao8 on 2016/11/8.
 */
@RestController
@RequestMapping("/alipay")
public class AlipayCallback {

    private Log log = LogFactory.getLog(TestController.class);

    @RequestMapping("/callback")
    public Map<String,Object> callback(Map<String,Object> params){

        log.debug(params);

        Map<String,Object> result = new HashMap<String,Object>();
        result.put("code","10000");
        return result;
    }
}
