package cn.com.xiao8.pay.alipay.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    private Log log = LogFactory.getLog(TestController.class);


    @RequestMapping("/index")
    public Map<String,Object> index() {
        log.debug("begin index");
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("code","0000");
        result.put("msg","请求成功");
        return result;

    }

}
