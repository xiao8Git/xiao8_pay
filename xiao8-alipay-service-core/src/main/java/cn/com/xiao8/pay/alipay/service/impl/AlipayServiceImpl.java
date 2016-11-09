package cn.com.xiao8.pay.alipay.service.impl;

import cn.com.xiao8.pay.alipay.service.AlipayService;

import cn.com.xiao8.pay.commons.util.JsonUtils;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiao8 on 2016/11/5.
 */
@Service("alipayService")
public class AlipayServiceImpl implements AlipayService {

    private String CHARSET = "UTF-8";

    //上线
    private String APP_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANoc3iFXBZYWmb1O5+ZN9bIF2t8ona7KIXzbtqTQxyYsjP2oWWy0q6lJ1d3hRRX3tzFtvOOU+Vhd80sPgO82zoWPMk8ZARytHLo0wldLBtcABzIZoI0E+GBVBdZckbBwRyis6MVsVW0w0lyVtc3QrIm2LlRb/GLDQG/EwmgPBc8nAgMBAAECgYBfOEkVXwtWScmWZtNArPo9XOb/k5i7TQ0vmC/1Zhv7I2nt90gK+BkVD+HpOsZRX9K/JnxXEpuEo4dqVntgc5nW0IBeEfYjY7KWL1u1U6NOGgpjFEQO4nZ7p/mT+EMKr8Jq5xfcE+5i98lICwn3dpm1KM4qSimav88x4cFPnFS/SQJBAPXalVGnz0vzVsylhx1xUfgn7iRozsQKskt6seKcw5U1jl1R1+O4nnXXwh3LuQurr6diRAxwlWk3SEl01o6nGo0CQQDjHTQG5JPcfubSGREkv0M3zx0sUN3g9klPWcUkHQCD8enS3vjz4Zj/mtazandCffNP82DvR5z8dlFbXgUN1l2DAkAJCIcEAQ500/QUgOB+DHPeklkFrXQrb9ktPMzcbrYg/V7FvwQXsejgm5TeU2XxL9W8aLfnXRugSg+14z0e8LThAkAKziLhiRJ31RnKOSMVqnvaNeJfb2F/CHioa87zTuXtYR8Mx0r033DWCFnao2AVK8TdsuiTucaARGjOfgSjieVdAkEAoCpJW2OxUGaY2phIgF3HSAc0YlZH3c35v0Iba72UgsPS5lAW78DlfCJoOczop9/sUvhxbUbUzW6fMdPs6mLvPg==";
    private String APP_ID = "2016110502554816";
    private String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

    //未上线
//    private String APP_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMy0L9Bj1xH//9apNkOR0Tug+wqk4krX7Dr2DkkyPajW1ouyJL0H86VI6puUc6HY2GfksvLUMWuH5XvImmrvQnBMINPk0xDxZmOIZ5l49Ft6sl7bvAemGXtvoIrI4S9QLWzZ8GUyjJuew2xxh3Ii30u17lGzgvkRemF653y/a7CTAgMBAAECgYA3PSRt8pM160QJlC3te+rfUWiNDcN2+N9pZb6jJ+iVqCvLFK5vSDcKUivlAeFWOmLjXkaf2nzfjJNznMV2t8vQcF35Hhx7JkZdq3AOUJ5kaP4PFBahMYTPA2HsAEMdW2tbvaEFlAXICXmM7xvsMjEF0dQ747yl6wdl8vRAZu2eAQJBAOXFYvmvsVvziMiCu3QsBBkrOAcwTEemMglV429Lu4MhKUl5D9uY9GEwnosRpWSrneW3VK5URQzUtQRJwABXT5MCQQDkEkKcKljIp9mqsPncIAE7P3BVvol4N365huU3O03eCCaKgXLiM4hyhDi0qpZxjPp6XAzBbdm7S4UzWGxYvLsBAkAtc5JUmfCXo+EW6OVz4ZEd+XKn2WH9el7Dgf3vciexlMm6AvykD+mnoBp8oAz7kQc2/cD+iyTtmNYF9yDH8H2JAkB3tp4AGro/mNtIHV0JjkMXNlVgrIK792UtPQFT3G0i/mQ8j3YeBa65bW3WZzRQpg9T6r1HSsHe7eUc7mzwpyABAkEAt7ApCMmJSenedp+xIgUCsXqbE9345Fk/tGKjKdj/Ygz27kL6Rj0V+DSYdHH0ve2CdYCcV7HpOBjivcEEQv1uxA==";
//    private String APP_ID = "2016110802640265";
//    private String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";






//沙箱
//    private String APP_ID = "2016110300788652";
//    private String APP_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKyFdUp5Uvj1S0Tmr02a4xFI1SNhVIfGF6MlMGV9KgF9TYBrQFiEJU2u7Fpst55N62Av6T769XwhAj42FPIEh96HbLX546Na9KnfV1naRFDOKNxukvTP1grwVtkEJYrdt+xgunwVreBR9jAX/OuTiACOaTW2S+G6nn9g7j6kXq+XAgMBAAECgYEAia+5fEZ3wdmCyOqS3gzPS+wkrqfvHq0qEDqN9XF/JNEsGWF/aSWN4AHLAB/kK6NkRs/2Bx2i7f4qHBxtevnCtLcce0kxiLrnjAeHjBTPCVjW9wbHSOCK1rXTZbNCdG6AFBK5OBVUqez8e6v7EGKsQ1wvhnmEjs9gilhbXsHloQECQQDZXTmsIGAzwEU2cJUopiTCg0nlZ00os0RP/AWBAzqx/An42bcy9WhY9lhrD1KbgK4Tp0xvsQbglLMgdF/sk8vZAkEAyy+89hHMm2rdL4BRwCEeJVTVgMyqMudNUZozuXE2ovrpmMHwlyp+YthUxpTPaAfDNOO6jtUT6+V9+aeRrEJg7wJBALWdJSDidD8JOUtBSf+KwlAbrlSUuW12S2+hHWAsOJCaDCL2lhFn5uXd/waE6Pqwy8H0GPOBsENZO0m0sqlS3BECQGfhNjiQDQ2Aayj379PMmr73SliZSoLP4qXPspYinY5hvcANl1WRsiAS/fSw7AEBxvlpXF3d7ltybx+OXlm1bh0CQGsZcN8AheyEtwOjtLUTLJR23d71enJCWQlvzgHL1ja8NQhBf8AFzDdYxS1mbkgIcO1hPuLXUG4jtRNf+Qbo/G4=";
//    private String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";


    public String pre(String orderNo, double totalAmount, String title) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY);
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY);

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("http://xiao8.t.xiao8.com.cn/alipay/callback");

        Map<String, Object> params = new HashMap<String, Object>();
        // 商户订单号
        params.put("out_trade_no", orderNo);
        // 订单金额
        params.put("total_amount", totalAmount);
        // 订单标题
        params.put("subject", title);

        AlipayResponse response = alipayClient.execute(request);
        if ("10000".equals(response.getCode())) {
            if ("".equals(response.getSubCode())) {
                return response.getBody();
            }
        } else {

        }
        return null;
    }

    public void pay() throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY);


        AlipayTradePayRequest request = new AlipayTradePayRequest(); //创建API对应的request类

        Map params = new HashMap<String, Object>();
        params.put("out_trade_no", "20150320010101001");
        params.put("scene", "bar_code");
        params.put("auth_code", "28763443825664394");
        params.put("subject", "Iphone6 16G");
        params.put("store_id", "NJ_001");
        params.put("timeout_express", "2m");
        params.put("total_amount", "0.01");

        request.setBizContent(JsonUtils.toJSON(params)); //设置业务参数
        //通过alipayClient调用API，获得对应的response类
        AlipayTradePayResponse response = alipayClient.execute(request);
        System.out.println(response.toString());

// TODO 根据response中的结果继续业务逻辑处理
    }

    public static void main(String[] args) throws Exception {
        AlipayServiceImpl pay = new AlipayServiceImpl();
        pay.pre("T"+System.currentTimeMillis() , 0.01, "测试");
    }
}
