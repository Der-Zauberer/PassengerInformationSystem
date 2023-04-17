package eu.derzauberer.pis.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserConfiguration {
	
	private ObjectNode config;
	private final Path path = Paths.get("config.json");
	
	private static final String DB_CLIENT_ID = "db-client-id";
	private static final String DB_API_KEY = "db-api-key";
	
	private static final ObjectMapper MAPPER = Pis.getSpringConfig().getObjectMapper();
	private static final Logger LOGGER = LoggerFactory.getLogger(UserConfiguration.class.getSimpleName());
	
	public UserConfiguration() {
		try {
			if (!Files.exists(path)) {
				config = MAPPER.createObjectNode();
				setDbClientId("");
				setDbApiKey("");
			} else {
				final String content = Files.readString(path);
				config = MAPPER.readValue(content.toString(), ObjectNode.class);
			}
		} catch (IOException exception) {
			config = MAPPER.createObjectNode();
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
			final String content = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(config);
			Files.writeString(path, content);
		} catch (IOException exception) {
			LOGGER.warn("Failed to save config.json: {} {}", exception.getClass(), exception.getMessage());
		}
	}

}
