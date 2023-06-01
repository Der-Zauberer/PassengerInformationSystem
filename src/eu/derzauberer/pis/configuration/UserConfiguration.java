package eu.derzauberer.pis.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class UserConfiguration {
	
	private ObjectNode config;
	private final Path path = Paths.get("config.json");
	
	private static final String DB_CLIENT_ID = "db-client-id";
	private static final String DB_API_KEY = "db-api-key";
	private static final Logger LOGGER = LoggerFactory.getLogger(UserConfiguration.class.getSimpleName());
	private final ObjectMapper objectMapper;
	
	@Autowired
	public UserConfiguration(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		try {
			if (!Files.exists(path)) {
				config = objectMapper.createObjectNode();
				setDbClientId("");
				setDbApiKey("");
			} else {
				final String content = Files.readString(path);
				config = objectMapper.readValue(content.toString(), ObjectNode.class);
			}
		} catch (IOException exception) {
			config = objectMapper.createObjectNode();
			LOGGER.warn("Failed to laod config.json: {} {}", exception.getClass(), exception.getMessage());
		}
	}
	
	public String getDbClientId() {
		return config.get(DB_CLIENT_ID).asText("");
	}
	
	public void setDbClientId(String string) {
		config.put(DB_CLIENT_ID, string);
		save();
	}
	
	public String getDbApiKey() {
		return config.get(DB_API_KEY).asText("");
	}
	
	public void setDbApiKey(String string) {
		config.put(DB_API_KEY, string);
		save();
	}
	
	private void save() {
		try {
			final String content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
			Files.writeString(path, content);
		} catch (IOException exception) {
			LOGGER.warn("Failed to save config.json: {} {}", exception.getClass(), exception.getMessage());
		}
	}

}
