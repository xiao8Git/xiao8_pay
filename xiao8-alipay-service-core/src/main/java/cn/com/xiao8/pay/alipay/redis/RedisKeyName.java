package cn.com.xiao8.pay.alipay.redis;

/**
 * 过期时间用的是秒作单位
 * @author 
 *
 */
public interface RedisKeyName {
    //规则： 系统名：模块名：功能名 "nts_tms:common:switch"
	//过期时间不用定义要这里
	/**
	 * -----------------------------------消息模块相关的redis的key-------------------------------
	 */
    //注册短信验证码，后面拼手机号码
    public static final String SMS_REGISTER_CHECK_CODE = "sms:register:check:code:";
    
    //门店任务当前字母后面拼门店ID
    //public static final String STORE_TASK_NO = "store:task:no:";
    //理发师的排队号，后面拼理发师ID
    public static final String BARBER_TASK_NO = "barber:task:no:";
    //理发师排队人数,后面拼理发师ID
    //public static final String BARBER_WAIT_COUNT = "barber:wait:count:";
    
    //任务是否在重排中，后面拼taskId
    public static final String TASK_AGAIN_ING = "task:again:ing:";
    
    public static final String TASK_TRANSFER_ING = "task:transfer:ing:";
    
    //后面拼openId
    public static final String WEIXIN_SUBSCRIBE_ING = "weixin:subscribe:ing:";
    
  //理发师队列,后面拼理发师ID
    public static final String BARBER_QUEUE_INFO = "barber:queue:info:";
    
    //门店ipad 主机ip 
    public static final String STORE_IPAD_CLIENT_HOST = "store:ipad:client:host";
    public static final String STORE_IPAD_CLIENT_TCP_HOST = "store:ipad:client:tcp:host";
    
    public static final String WEB_SOCKET_CLIENT_LIST = "web:socket:client:list";
    
    // 微信token
    public static final String WEIXIN_ACCESS_TOKEN = "weixin:access:token";
    // 微信webview code 查token  后面拼code
    public static final String WEIXIN_WEBVIEW_ACCESS_TOKEN = "weixin:webview:access:token:";
    
    public static final String WEIXIN_JSAPI_TICKET="weixin:jsapi:ticket";
    
    //微信用户 纬,经,精度 后面拼openId
    public static final String WEIXIN_USER_DISTANCE_LATITUDE_LONGITUDE_PRECISION = "weixin:user:distance:latitude:longitude:precision:";
    
  //任务是否在完成中，后面拼taskId
    public static final String TASK_FINISH_ING="task:finish:ing:";
    
    public static final String AUTO_UPDATE_COMMENT_BAT_ING = "auto:update:comment:bat:ing";
    
    //是否正在生成券
    public static final String GENERATE_COUPON_ING="generate:coupon:ing";
    public static final String AUTO_UPDATE_COUPON_ING="auto:update:coupon:ing";
    public static final String AUTO_SEND_COUPON_EXPIRED_SMS_ING="auto:send:coupon:expired:sms:ing";
    
    public static final String AUTO_SEND_WECHAT_MSG_ING = "auto:send:wechat:msg:ing";
    


}