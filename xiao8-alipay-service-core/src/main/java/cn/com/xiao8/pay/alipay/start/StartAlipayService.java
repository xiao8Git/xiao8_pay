package cn.com.xiao8.pay.alipay.start;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xiao8 on 2016/11/7.
 */
public class StartAlipayService {

    private static final Log log = LogFactory.getLog(StartAlipayService.class);

    public static void main(String [] args){
        try {
            String[] config={"classpath:application-context.xml"};
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
            context.start();
            System.out.println("== AlipayService Provider  started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error("== AlipayService Provider context start error:",e);
        }
    }
}
