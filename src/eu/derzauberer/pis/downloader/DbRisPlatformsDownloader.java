package eu.derzauberer.pis.downloader;

import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.Platform;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.repositories.Repository;
import eu.derzauberer.pis.util.HttpRequest;
import eu.derzauberer.pis.util.ProgressStatus;

public class DbRisPlatformsDownloader {
	
	private static final String NAME = "db/ris::platforms";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/ris-stations/v1/platforms/by-key";
	private static final Logger LOGGER = LoggerFactory.getLogger(DbRisPlatformsDownloader.class);
	private final Repository<Station> repository = (Repository<Station>) Pis.getRepository("stations", Station.class);
	
	public DbRisPlatformsDownloader() {
		LOGGER.info("Downloading {} from {}", NAME, URL);
		final HttpRequest request = new HttpRequest();
		request.setUrl(URL);
		request.getHeader().put("DB-Client-Id", Pis.getUserConfig().getDbClientId());
		request.getHeader().put("DB-Api-Key", Pis.getUserConfig().getDbApiKey());
		request.setExceptionAction(exception -> LOGGER.error("Downloading {} from {} failed: {} {}", repository.getName(), NAME, exception.getClass().getSimpleName(), exception.getMessage()));
		final List<Station> stations = repository.getList();
		int counter = 0;
		long millis = System.currentTimeMillis();
		final ProgressStatus progress = new ProgressStatus("Processing", NAME, stations.size());
		for (Station station : stations) {
			if (station.getApiInformation() == null || station.getApiInformation().getIds() == null || !station.getApiInformation().getIds().containsKey("eva")) continue;
			request.getParameter().put("keyType", "EVA");
			request.getParameter().put("key", station.getApiInformation().getIds().get("eva").toString());
			long wait = System.currentTimeMillis() - millis;
			try {
				Thread.sleep(wait < 120 ? 120 - wait : 120);
			} catch (InterruptedException exception) {}
			millis = System.currentTimeMillis();
			request.request().map(HttpRequest::mapToJson).ifPresent(json -> save(station, json));
			progress.count(station.getId());
			counter++;
		}
		LOGGER.info("Downloaded {} stations platforms from {}", counter, NAME, URL);
	}
	
	private void save(Station station, ObjectNode json) {
		if (station.getPlatforms() == null && !json.withArray("platforms").isEmpty()) station.setPlatforms(new TreeSet<>());
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
			station.getOrCreateApiInformation().addSource(URL);
		}
		repository.add(station);
	}
	
	public static String getName() {
		return NAME;
	}
	
}
