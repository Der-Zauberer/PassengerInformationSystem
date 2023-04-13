package eu.derzauberer.pis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class HttpRequest {
	
	private String url;
	private final Map<String, String> parameter = new HashMap<>();
	private final Map<String, String> header = new HashMap<>();
	private Consumer<IOException> exceptionAction;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Map<String, String> getParameter() {
		return parameter;
	}
	
	public Map<String, String> getHeader() {
		return header;
	}
	
	public Consumer<IOException> getExceptionAction() {
		return exceptionAction;
	}
	
	public void setExceptionAction(Consumer<IOException> exceptionAction) {
		this.exceptionAction = exceptionAction;
	}
	
	public Optional<String> request() {
		if (url == null) throw new IllegalArgumentException("The url is required to make a request!");
		try {
			final HttpURLConnection connection = connect(url, parameter, header);
			final String response = readResponse(connection);
			connection.disconnect();
			return Optional.of(response);
		} catch (IOException exception) {
			if (exceptionAction != null) exceptionAction.accept(exception);
			return Optional.empty();
		}
	}
	
	private String getParameterString(Map<String, String> parameters) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder(parameters.isEmpty() ? "" : "?");
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}
		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}
	
	private HttpURLConnection connect(String url, Map<String, String> parameter, Map<String, String> header) throws MalformedURLException, IOException {
		final String fullUrl = url + getParameterString(parameter);
		final HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
		connection.setRequestMethod("GET");
		header.forEach((key, value) -> {
			connection.setRequestProperty(key, value);
		});
		connection.setConnectTimeout(7000);
		connection.setReadTimeout(7000);
		return connection;
	}
	
	private String readResponse(HttpURLConnection connection) throws IOException {
		final BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String responseLine;
		StringBuilder response = new StringBuilder();
		while ((responseLine = input.readLine()) != null) {
		    response.append(responseLine);
		}
		input.close();
		return response.toString();
	}
	
	public static ObjectNode mapToJson(String string) {
		try {
			return new ObjectMapper().readValue(string, ObjectNode.class);
		} catch (JsonProcessingException exception) {
			exception.printStackTrace();
			return null;
		}
	}

}
