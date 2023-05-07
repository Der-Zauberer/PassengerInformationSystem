package eu.derzauberer.pis.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.Location;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.repositories.Repository;
import eu.derzauberer.pis.util.HttpRequest;
import eu.derzauberer.pis.util.ProgressStatus;

public class DbStadaStationDownloader {
	
	private static final String NAME = "db/stada";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/station-data/v2/stations";
	private static final Logger LOGGER = LoggerFactory.getLogger(DbStadaStationDownloader.class);
	private final Repository<Station> repository = (Repository<Station>) Pis.getRepository("stations", Station.class);
	
	public DbStadaStationDownloader() {
		LOGGER.info("Downloading {} from {}", NAME, URL);
		final HttpRequest request = new HttpRequest();
		request.setUrl(URL);
		request.getHeader().put("DB-Client-Id", Pis.getUserConfig().getDbClientId());
		request.getHeader().put("DB-Api-Key", Pis.getUserConfig().getDbApiKey());
		request.setExceptionAction(exception -> LOGGER.error("Downloading {} from {} failed: {} {}", repository.getName(), NAME, exception.getClass().getSimpleName(), exception.getMessage()));
		request.request().map(this::logDownloadProcessing).map(HttpRequest::mapToJson).ifPresent(this::saveAll);
	}
	
	private void saveAll(ObjectNode json) {
		final List<String> warns = new ArrayList<>();
		final ProgressStatus progress = new ProgressStatus("Processing", NAME, json.withArray("result").size());
		int counter = 0;
		for (JsonNode node : json.withArray("result")) {
			final String name = node.get("name").asText();
			final Station station = repository.getById(Entity.nameToId(name)).orElse(new Station(name));
			station.getOrCreateAdress().setStreet(node.at("/mailingAddress/street").asText());
			station.getOrCreateAdress().setPostalCode(node.at("/mailingAddress/zipcode").asInt());
			station.getOrCreateAdress().setCity(node.at("/mailingAddress/city").asText());
			station.getOrCreateAdress().setCountry("Germany");
			if (!node.get("evaNumbers").isEmpty() && !node.get("evaNumbers").get(0).isEmpty()) {
				JsonNode eva = node.get("evaNumbers").get(0);
				station.getOrCreateApiInformation().addId("eva", "eva.get(\"number\").asLong()");
				final JsonNode location = eva.at("/geographicCoordinates/coordinates");
				if (!location.isEmpty()) {
					station.setLocation(new Location(location.get(1).asDouble(), location.get(0).asDouble()));
				} else {
					warns.add("Station " + station.getId() + " does not contain geo-coordinates!");
				}
			} else {
				warns.add("Station " + station.getId() + " does not contain geo-coordinates and an eva-number!");
			}
			station.getorCreateServices().setParking(extractBoolean(node, "hasParking"));
			station.getorCreateServices().setBicycleParking(extractBoolean(node, "hasBicycleParking"));
			station.getorCreateServices().setLocalPublicTransport(extractBoolean(node, "hasLocalPublicTransport"));
			station.getorCreateServices().setPublicFacilities(extractBoolean(node, "hasPublicFacilities"));
			station.getorCreateServices().setLockerSystem(extractBoolean(node, "hasLockerSystem"));
			station.getorCreateServices().setTaxiRank(extractBoolean(node, "hasTaxiRank"));
			station.getorCreateServices().setTravelNecessities(extractBoolean(node, "hasTravelNecessities"));
			station.getorCreateServices().setBarrierFree(extractBoolean(node, "hasSteplessAccess"));
			station.getorCreateServices().setWifi(extractBoolean(node, "hasWiFi"));
			station.getorCreateServices().setTravelCenter(extractBoolean(node, "hasTravelCenter"));
			station.getorCreateServices().setRailwayMission(extractBoolean(node, "hasRailwayMission"));
			station.getorCreateServices().setHasCarRental(extractBoolean(node, "hasCarRental"));
			station.getOrCreateApiInformation().addId("stada", node.get("number").asLong());
			station.getOrCreateApiInformation().addSource(URL);
			progress.count();
			counter++;
			repository.add(station);
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
