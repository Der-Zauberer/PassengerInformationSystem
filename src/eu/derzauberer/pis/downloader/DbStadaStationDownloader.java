package eu.derzauberer.pis.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

public class DbStadaStationDownloader {
	
	private final UserConfiguration config;
	private final StationService stationService;
	
	private static final String NAME = "db/stada";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/station-data/v2/stations";
	private static final Logger LOGGER = LoggerFactory.getLogger(DbStadaStationDownloader.class);
	
	@Autowired
	public DbStadaStationDownloader(UserConfiguration config, StationService stationService) {
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
		request.request().map(this::logDownloadProcessing).map(HttpRequest::mapToJson).ifPresent(this::saveAll);
	}
	
	private void saveAll(ObjectNode json) {
		final List<String> warns = new ArrayList<>();
		final ProgressStatus progress = new ProgressStatus("Processing", NAME, json.withArray("result").size());
		int counter = 0;
		for (JsonNode node : json.withArray("result")) {
			final String name = node.get("name").asText();
			final Station station = stationService.getById(NameEntity.nameToId(name)).orElse(new Station(name));
			station.getOrCreateAddress().setStreet(node.at("/mailingAddress/street").asText());
			station.getOrCreateAddress().setPostalCode(node.at("/mailingAddress/zipcode").asInt());
			station.getOrCreateAddress().setCity(node.at("/mailingAddress/city").asText());
			station.getOrCreateAddress().setCountry("Germany");
			if (!node.get("evaNumbers").isEmpty() && !node.get("evaNumbers").get(0).isEmpty()) {
				JsonNode eva = node.get("evaNumbers").get(0);
				station.getOrCreateApiInformation().addId("eva", eva.get("number").asLong());
				final JsonNode location = eva.at("/geographicCoordinates/coordinates");
				if (!location.isEmpty()) {
					station.setLocation(new Location(location.get(1).asDouble(), location.get(0).asDouble()));
				} else {
					warns.add("Station " + station.getId() + " does not contain geo-coordinates!");
				}
			} else {
				warns.add("Station " + station.getId() + " does not contain geo-coordinates and an eva-number!");
			}
			station.getOrCreateServices().setParking(extractBoolean(node, "hasParking"));
			station.getOrCreateServices().setBicycleParking(extractBoolean(node, "hasBicycleParking"));
			station.getOrCreateServices().isLocalPublicTransport(extractBoolean(node, "hasLocalPublicTransport"));
			station.getOrCreateServices().setPublicFacilities(extractBoolean(node, "hasPublicFacilities"));
			station.getOrCreateServices().setLockerSystem(extractBoolean(node, "hasLockerSystem"));
			station.getOrCreateServices().setTaxiRank(extractBoolean(node, "hasTaxiRank"));
			station.getOrCreateServices().setTravelNecessities(extractBoolean(node, "hasTravelNecessities"));
			station.getOrCreateServices().setBarrierFree(extractBoolean(node, "hasSteplessAccess"));
			station.getOrCreateServices().setWifi(extractBoolean(node, "hasWiFi"));
			station.getOrCreateServices().setTravelCenter(extractBoolean(node, "hasTravelCenter"));
			station.getOrCreateServices().setRailwayMission(extractBoolean(node, "hasRailwayMission"));
			station.getOrCreateServices().setCarRental(extractBoolean(node, "hasCarRental"));
			station.getOrCreateApiInformation().addId("stada", node.get("number").asLong());
			station.getOrCreateApiInformation().addSource(URL);
			progress.count();
			counter++;
			stationService.save(station);
		}
		for (String warn : warns) {
			LOGGER.warn(warn);
		}
		LOGGER.info("Downloaded {} stations from {}", counter, NAME, URL);
	}
	
	private String logDownloadProcessing(String string) {
		LOGGER.info("Processing {}", NAME);
		return string;
	}
	
	private boolean extractBoolean(JsonNode node, String name) {
		return Optional.ofNullable(node.get(name)).map(entry -> {
			if (entry.isBoolean()) return entry.asBoolean();
			else if (entry.isTextual() && entry.asText().equalsIgnoreCase("yes")) return true;
			return false;
		}).orElse(false);
	}
	
	public static String getName() {
		return NAME;
	}

}
