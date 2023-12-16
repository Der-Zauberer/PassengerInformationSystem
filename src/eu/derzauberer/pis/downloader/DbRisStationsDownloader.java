package eu.derzauberer.pis.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.derzauberer.pis.configuration.UserConfiguration;
import eu.derzauberer.pis.service.StationService;
import eu.derzauberer.pis.structure.container.Location;
import eu.derzauberer.pis.structure.model.NameEntity;
import eu.derzauberer.pis.structure.model.Station;
import eu.derzauberer.pis.util.HttpRequest;
import eu.derzauberer.pis.util.ProgressStatus;

public class DbRisStationsDownloader {
	
	private final UserConfiguration config;
	private final StationService stationService;
	
	private static final String NAME = "db/ris::stations";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/ris-stations/v1/stations";
	private static final Logger LOGGER = LoggerFactory.getLogger(DbRisStationsDownloader.class);
	
	@Autowired
	public DbRisStationsDownloader(UserConfiguration config, StationService stationService) {
		this.config = config;
		this.stationService = stationService;
		download();
	}
	
	private void download() {
		LOGGER.info("Downloading {} from {}", NAME, URL);
		final HttpRequest request = new HttpRequest();
		request.setUrl(URL);
		request.getParameter().put("limit","10000");
		request.getHeader().put("DB-Client-Id", config.getDbClientId());
		request.getHeader().put("DB-Api-Key", config.getDbApiKey());
		request.setExceptionAction(exception -> LOGGER.error("Downloading {} from {} failed: {} {}", stationService.getName(), NAME, exception.getClass().getSimpleName(), exception.getMessage()));
		request.request().map(this::logDownloadProcessing).map(HttpRequest::mapToJson).ifPresent(this::saveAll);
	}
	
	private void saveAll(ObjectNode json) {
		final ProgressStatus progress = new ProgressStatus("Processing", NAME, json.withArray("stations").size());
		int counter = 0;
		for (JsonNode node : json.withArray("stations")) {
			final String name = node.at("/names/DE/name").asText();
			final Station station = stationService.getById(NameEntity.nameToId(name)).orElse(new Station(name));
			if (node.at("/owner/name").asText().equalsIgnoreCase("DB S&S")) {
				station.getOrCreateAddress().setName("DB Station&Service AG");
			} else {
				station.getOrCreateAddress().setName(node.at("/owner/name").asText());
			}
			station.getOrCreateAddress().setStreet(node.at("/address/street").asText() + " " + node.at("/address/houseNumber").asText());
			station.getOrCreateAddress().setPostalCode(node.at("/address/postalCode").asInt());
			station.getOrCreateAddress().setCity(node.at("/address/city").asText());
			if (node.at("/address/country").asText().equalsIgnoreCase("de")) {
				station.getOrCreateAddress().setCountry("Germany");
			} else if (node.at("/address/country").asText().equalsIgnoreCase("ch")) {
				station.getOrCreateAddress().setCountry("Switzerland");
			} else {
				station.getOrCreateAddress().setCountry(node.at("/address/country").asText());
			}
			station.setLocation(new Location(node.at("/position/latitude").asDouble(), node.at("/position/longitude").asDouble()));
			station.getOrCreateApiInformation().addId("stada", node.get("stationID").asLong());
			station.getOrCreateApiInformation().addSource(URL);
			progress.count();
			counter++;
			stationService.save(station);
		}
		LOGGER.info("Downloaded {} stations from {}", counter, NAME, URL);
	}
	
	private String logDownloadProcessing(String string) {
		LOGGER.info("Processing {}", NAME);
		return string;
	}
	
	public static String getName() {
		return NAME;
	}
	
}
