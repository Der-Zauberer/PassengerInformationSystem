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
		
		final String filter = (String) session.getAttribute(FILTER);
		final String history = (String) session.getAttribute(HISTORY);
		
		if (parameters.isEmpty() && filter != null && history != null && matchParameters(filter, history , request)) {
			response.sendRedirect(request.getRequestURI() + "?" + buildParametersFromFilter(filter, request));
		} else if (!parameters.isEmpty()) {
			session.setAttribute(FILTER, request.getRequestURI() + "?" + buildParametersForFilter(parameters, request));
		}
		return true;
	}
	
	private boolean matchParameters(final String filter, String history, HttpServletRequest request) {
		final String url = filter.split("\\?")[0];
		if (history.equals(url)) return false;
		return url.equals(request.getRequestURI());
	}
	
	private String buildParametersFromFilter(String filter, HttpServletRequest request) {
		String parameters = filter.split("\\?")[1];
		if (request.getQueryString() != null && !request.getQueryString().isBlank()) {
			parameters = request.getQueryString() + "&" + parameters;
		}
		return parameters;
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
