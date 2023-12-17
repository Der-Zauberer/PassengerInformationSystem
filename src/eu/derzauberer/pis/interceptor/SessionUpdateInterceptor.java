package eu.derzauberer.pis.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import eu.derzauberer.pis.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionUpdateInterceptor implements HandlerInterceptor {
	
	@Autowired
	private AuthenticationService auth;
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		auth.updateSession(request.getSession());
		return true;
	}

}
