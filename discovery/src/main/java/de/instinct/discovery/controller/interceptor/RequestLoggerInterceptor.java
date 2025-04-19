package de.instinct.discovery.controller.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import ch.qos.logback.classic.Logger;
import de.instinct.discovery.controller.interceptor.wrapper.CachedBodyHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class RequestLoggerInterceptor implements HandlerInterceptor {
	
	private static Logger LOGGER = (Logger) LoggerFactory.getLogger(RequestLoggerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
		String requestLog = "[preHandle][" + cachedRequest.getMethod() + "]" + cachedRequest.getRequestURI() + getParameters(cachedRequest) + " - Body: " + getBody(cachedRequest);
		LOGGER.info(requestLog);
		return true;
	}

	private String getParameters(final HttpServletRequest request) {
		final StringBuffer posted = new StringBuffer();
		final Enumeration<?> e = request.getParameterNames();

		while (e != null && e.hasMoreElements()) {
			if (posted.length() > 1)
				posted.append("&");
			final String curr = (String) e.nextElement();
			posted.append(curr).append("=");
			if (curr.contains("password") || curr.contains("answer") || curr.contains("pwd")) {
				posted.append("*****");
			} else {
				posted.append(request.getParameter(curr));
			}
		}

		return posted.toString();
	}
	
	private String getBody(HttpServletRequest request) throws IOException {
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;
	    try {
	        bufferedReader = request.getReader();
	        char[] charBuffer = new char[128];
	        int bytesRead;
	        while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
	            stringBuilder.append(charBuffer, 0, bytesRead);
	        }
	    } finally {
	        if (bufferedReader != null) {
	            bufferedReader.close();
	        }
	    }
	    return stringBuilder.toString();
	}

}
