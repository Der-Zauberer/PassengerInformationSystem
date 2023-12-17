package eu.derzauberer.pis.interceptor;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class FilterInterceptor implements HandlerInterceptor {
	
	private static final HashSet<String> parametersToObserve = new HashSet<>();
	
	static {
		parametersToObserve.add("search");
		parametersToObserve.add("page");
		parametersToObserve.add("pageSize");
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final HttpSession session = request.getSession();
		if (session == null) return true;
		final List<Entry<String, String[]>> parameters = request.getParameterMap()
		.entrySet()
		.stream()
		.filter(entry -> parametersToObserve.contains(entry.getKey()))
		.toList();
		if (parameters.isEmpty() && matchParameters(session, request)) {
			String parameterString = session.getAttribute("filter").toString().split("\\?")[1];
			if (request.getQueryString() != null && !request.getQueryString().isBlank()) parameterString = request.getQueryString() + "&" +parameterString;
			response.sendRedirect(request.getRequestURI() + "?" + parameterString);
		} else if (!parameters.isEmpty()) {
			final StringBuilder parameterString = new StringBuilder();
			parameters.forEach(parameter -> parameterString.append(parameter.getKey() + "=" + parameter.getValue()[0] + "&"));
			parameterString.deleteCharAt(parameterString.length() - 1);
			session.setAttribute("filter", request.getRequestURI() + "?" + parameterString);
		}
		return true;
	}
	
	private boolean matchParameters(HttpSession session, HttpServletRequest request) {
		if (session.getAttribute("filter") == null) return false;
		final String url = session.getAttribute("filter").toString().split("\\?")[0];
		if (session.getAttribute("history") != null && session.getAttribute("history").equals(url)) return false;
		return url.equals(request.getRequestURI());
	}

}
