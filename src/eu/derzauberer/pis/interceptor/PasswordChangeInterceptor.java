package eu.derzauberer.pis.interceptor;

import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import eu.derzauberer.pis.structure.data.UserData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class PasswordChangeInterceptor implements HandlerInterceptor {
	
	private static final String passwordChange = "/password/change";
	private static final String passwordChangeReqired = "passwordChangeReqired";
	private static final String passwordChangeRequest = "passwordChangeRequest";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getMethod().equals("POST") && request.getRequestURI().equals(passwordChange)) return true;
		final HttpSession session = request.getSession();
		if (session == null || session.getAttribute(passwordChangeReqired) == null) return true;
		session.removeAttribute(passwordChangeReqired);
		
		final SavedRequest savedRequest = new SimpleSavedRequest(request.getRequestURI() + "?" + request.getQueryString());
		session.setAttribute(passwordChangeRequest, savedRequest);
		response.sendRedirect(passwordChange + "?user=" + ((UserData) session.getAttribute("user")).getId());
		return true;
	}

}
