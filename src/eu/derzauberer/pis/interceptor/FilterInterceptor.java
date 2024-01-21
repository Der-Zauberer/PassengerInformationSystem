package eu.derzauberer.pis.interceptor;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class FilterInterceptor implements HandlerInterceptor {
	
	private static final HashSet<String> parametersToObserve = new HashSet<>();
	
	private static final String FILTER = "filter";
	private static final String HISTORY = "history";
	
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
		
		final SavedRequest filter = (SavedRequest) session.getAttribute(FILTER);
		final SavedRequest history = (SavedRequest) session.getAttribute(HISTORY);
		
		if (parameters.isEmpty() && filter != null && history != null && matchParameters(filter, history , request)) {
			response.sendRedirect(filter.getRedirectUrl());
		} else if (!parameters.isEmpty()) {
			final SavedRequest savedRequest = new SimpleSavedRequest(request.getRequestURI() + "?" + buildParametersForFilter(parameters, request));
			session.setAttribute(FILTER, savedRequest);
		}
		return true;
	}
	
	private boolean matchParameters(final SavedRequest filter, SavedRequest history, HttpServletRequest request) {
		final String filterPath = filter.getRedirectUrl().split("\\?")[0];
		final String historyPath = history.getRedirectUrl().split("\\?")[0];
		if (filterPath.equals(historyPath)) return false;
		return filterPath.equals(request.getRequestURI());
	}
	
	private String buildParametersForFilter(List<Entry<String, String[]>> parameters, HttpServletRequest request) {
		final StringBuilder parameterString = new StringBuilder();
		for (final Entry<String, String[]> parameter : parameters) {
			parameterString.append(parameter.getKey() + "=" + parameter.getValue()[0] + "&");
		}
		parameterString.deleteCharAt(parameterString.length() - 1);
		return parameterString.toString();
	}

}
