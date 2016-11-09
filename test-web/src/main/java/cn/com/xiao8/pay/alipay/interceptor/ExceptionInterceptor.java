package cn.com.xiao8.pay.alipay.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.xiao8.pay.commons.util.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;



public class ExceptionInterceptor 
extends SimpleMappingExceptionResolver {

	private Log log = LogFactory.getLog(ExceptionInterceptor.class);
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
											HttpServletResponse response, 
											Object arg2,
											Exception ex) {
		log.error("ExceptionInterceptor 处理的异常",ex);
		
		String viewName = determineViewName(ex, request);
		if (viewName != null) {// JSP格式返回
			if (!(request.getHeader("accept").indexOf("application/json") > -1 || 
					(request.getHeader("X-Requested-With") != null 
					&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
				// 如果不是异步请求
				// Apply HTTP status code for error views, if specified.
				// Only apply it if we're processing a top-level request.
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				return getModelAndView(viewName, ex, request);
			} else {// JSON格式返回
				try {
					PrintWriter writer = response.getWriter();
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("success", false);
					map.put("msg", ex.getMessage());
					writer.write(JsonUtils.toJSON(map));
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		} else {
			return null;
		}
	}
}