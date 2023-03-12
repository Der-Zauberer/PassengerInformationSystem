package eu.derzauberer.pis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Downloader {
	
	private final String name;
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(Downloader.class.getSimpleName());
	
	public Downloader(String name) {
		this.name = name;
	}
	
	public abstract void download();
	
	protected Optional<ObjectNode> download(String url, Map<String, String> parameters, Map<String, String> header) {
		try {
			final HttpURLConnection connection = connect(url, parameters, header);
			final String response = readResponse(connection);
			connection.disconnect();
			return Optional.of(new ObjectMapper().readValue(response.toString(), ObjectNode.class));
		} catch (IOException exception) {
			LOGGER.error("Failed to download {} from {}: {} {}!", name, url, exception.getClass().getSimpleName(), exception.getMessage());
			return Optional.empty();
		}
	}
	
	private String getParameterString(Map<String, String> parameters) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}
		String resultString = result.toString();
		return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
	}
	
	private HttpURLConnection connect(String url, Map<String, String> parameters, Map<String, String> header) throws MalformedURLException, IOException {
		final String fullUrl = url + getParameterString(parameters);
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
	
}
