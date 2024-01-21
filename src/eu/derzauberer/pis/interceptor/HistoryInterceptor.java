package eu.derzauberer.pis.interceptor;

import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class HistoryInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final HttpSession session = request.getSession();
		if (session == null) return true;
		String url = request.getRequestURI();
		if (request.getQueryString() != null) url += ("?" + request.getQueryString());
		session.setAttribute("history", new SimpleSavedRequest(url));
		return true;
	}
	
}
