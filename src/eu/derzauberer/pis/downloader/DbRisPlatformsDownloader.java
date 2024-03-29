package eu.derzauberer.pis.downloader;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.derzauberer.pis.configuration.UserConfiguration;
import eu.derzauberer.pis.model.PlatformModel;
import eu.derzauberer.pis.model.StationModel;
import eu.derzauberer.pis.persistence.Lazy;
import eu.derzauberer.pis.service.StationService;
import eu.derzauberer.pis.util.HttpRequest;
import eu.derzauberer.pis.util.ProgressStatus;

public class DbRisPlatformsDownloader {
	
	private final UserConfiguration config;
	private final StationService stationService;
	
	private static final String NAME = "db/ris::platforms";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/ris-stations/v1/platforms/by-key";
	private static final Logger LOGGER = LoggerFactory.getLogger(DbRisPlatformsDownloader.class);
	
	@Autowired
	public DbRisPlatformsDownloader(UserConfiguration config, StationService stationService) {
		this.config = config;
		this.stationService = stationService;
		download();
	}
	
	private void download() {
		LOGGER.info("Downloading {} from {}", NAME, URL);
		final HttpRequest request = new HttpRequest();
		request.setUrl(URL);
		request.getHeader().put("DB-Client-Id", config.getDbClientId());
		request.getHeader().put("DB-Api-Key", config.getDbApiKey());
		request.setExceptionAction(exception -> LOGGER.error("Downloading {} from {} failed: {} {}", stationService.getName(), NAME, exception.getClass().getSimpleName(), exception.getMessage()));
		final List<StationModel> stations = stationService.getAll().stream().map(Lazy::get).toList();
		int counter = 0;
		long millis = System.currentTimeMillis();
		final ProgressStatus progress = new ProgressStatus("Processing", NAME, stations.size());
		for (StationModel station : stations) {
			if (station.getApiInformation().isEmpty() || station.getOrCreateApiInformation().getIds() == null || !station.getOrCreateApiInformation().getIds().containsKey("eva")) continue;
			request.getParameter().put("keyType", "EVA");
			request.getParameter().put("key", station.getOrCreateApiInformation().getIds().get("eva").toString());
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
	
	private void save(StationModel station, ObjectNode json) {
		for (JsonNode node : json.withArray("platforms")) {
			final PlatformModel platfrom = new PlatformModel(node.get("name").asText().toUpperCase());
			station.getPlatforms().stream().filter(entry -> entry.getName().equalsIgnoreCase(platfrom.getName())).findAny().ifPresent(station.getPlatforms()::remove);
			station.getPlatforms().add(platfrom);
			if (node.get("length") != null) platfrom.setLength(node.get("length").asInt());
			final JsonNode linkedPlatforms = node.get("linkedPlatforms");
			if (linkedPlatforms != null && !linkedPlatforms.isEmpty() && linkedPlatforms.isArray()) {
				for (JsonNode linkedPlatform : linkedPlatforms) {
					platfrom.getLinkedPlatforms().add(linkedPlatform.asText());
				}
			}
			station.getOrCreateApiInformation().addSource(URL);
		}
		stationService.save(station);
	}
	
	public static String getName() {
		return NAME;
	}
	
}
