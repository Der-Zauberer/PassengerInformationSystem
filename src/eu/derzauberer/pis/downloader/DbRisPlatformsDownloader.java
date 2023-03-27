package eu.derzauberer.pis.downloader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.Platform;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.util.Downloader;
import eu.derzauberer.pis.util.Repository;

public class DbRisPlatformsDownloader extends Downloader {
	
	private static final String NAME = "db/ris::platforms";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/ris-stations/v1/platforms/by-key";

	private static final Map<String, String> header = new HashMap<>();
	
	private Repository<Station> repository;
	
	public DbRisPlatformsDownloader() {
		super(NAME);
		header.put("DB-Client-Id", Pis.getUserConfig().getDbClientId());
		header.put("DB-Api-Key", Pis.getUserConfig().getDbApiKey());
	}

	@Override
	public void download() {
		repository = (Repository<Station>) Pis.getRepository("stations", Station.class);
		LOGGER.info("Downloading {} from {}", NAME, URL);
		int counter = 0;
		final List<Station> stations = repository.getList().stream().filter(station -> station.getApiIds().containsKey("eva")).toList();
		long millis = System.currentTimeMillis();
		for (Station station : stations) {
			final Map<String, String> parameters = new HashMap<>();
			parameters.put("keyType", "EVA");
			parameters.put("key", station.getApiIds().get("eva").toString());
			long wait = System.currentTimeMillis() - millis;
			try {
				Thread.sleep(wait < 120 ? 120 - wait : 120);
			} catch (InterruptedException exception) {}
			millis = System.currentTimeMillis();
			download(URL, parameters, header).ifPresent(json -> proccess(station, json));
			counter++;
		}
		LOGGER.info("Downloaded {} stations platforms from {}", counter, NAME, URL);
	}
	
	private void proccess(Station station, ObjectNode json) {
		for (JsonNode node : json.withArray("platforms")) {
			final Platform platfrom = new Platform(node.get("name").asText().toUpperCase());
			station.getPlatforms().stream().filter(entry -> entry.getName().equalsIgnoreCase(platfrom.getName())).findAny().ifPresent(station.getPlatforms()::remove);
			station.getPlatforms().add(platfrom);
			if (node.get("length") != null) platfrom.setLength(node.get("length").asInt());
			final JsonNode linkedPlatforms = node.get("linkedPlatforms");
			if (linkedPlatforms != null && !linkedPlatforms.isEmpty() && linkedPlatforms.isArray()) {
				for (JsonNode linkedPlatform : linkedPlatforms) {
					platfrom.getLinkedPlattforms().add(linkedPlatform.asText());
				}
			}
		}
		repository.add(station);
	}
	
	public static String getName() {
		return NAME;
	}
	
	public static String getUrl() {
		return URL;
	}
	
}
