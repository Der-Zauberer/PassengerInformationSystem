package eu.derzauberer.pis.downloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.util.Downloader;
import eu.derzauberer.pis.util.Entity;
import eu.derzauberer.pis.util.ProgressStatus;
import eu.derzauberer.pis.util.Repository;

public class DbStationDownloader extends Downloader {
	
	private static final String NAME = "db/stada";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/station-data/v2/stations";
	
	private static final Map<String, String> parameters = new HashMap<>();
	private static final Map<String, String> header = new HashMap<>();
	
	private Repository<Station> repository;
	
	public DbStationDownloader() {
		super(NAME);
		header.put("DB-Client-Id", Pis.getUserConfig().getDbClientId());
		header.put("DB-Api-Key", Pis.getUserConfig().getDbApiKey());
	}
	
	@Override
	public void download() {
		repository = (Repository<Station>) Pis.getRepository("stations", Station.class);
		LOGGER.info("Downloading {} from {}", NAME, URL);
		download(URL, parameters, header).ifPresent(this::proccess);
	}
	
	private void proccess(ObjectNode json) {
		final List<String> warns = new ArrayList<>();
		final ProgressStatus progress = new ProgressStatus(getName(), json.withArray("result").size());
		int counter = 0;
		for (JsonNode node : json.withArray("result")) {
			final String name = node.get("name").asText();
			final Station station = repository.getById(Entity.nameToId(name)).orElse(new Station(name));
			station.getAdress().setStreet(node.at("/mailingAddress/street").asText());
			station.getAdress().setPostalCode(node.at("/mailingAddress/zipcode").asInt());
			station.getAdress().setCity(node.at("/mailingAddress/city").asText());
			station.getAdress().setCountry("Germany");
			if (!node.get("evaNumbers").isEmpty() && !node.get("evaNumbers").get(0).isEmpty()) {
				JsonNode eva = node.get("evaNumbers").get(0);
				station.getApiIds().put("eva", eva.get("number").asLong());
				final JsonNode location = eva.at("/geographicCoordinates/coordinates");
				if (!location.isEmpty()) {
					station.getLocation().setLongitude(location.get(0).asDouble());
					station.getLocation().setLatitude(location.get(1).asDouble());
				} else {
					warns.add("Station " + station.getId() + " does not contain geo-coordinates!");
				}
			} else {
				warns.add("Station " + station.getId() + " does not contain geo-coordinates and an eva-number!");
			}
			station.getServices().setParking(extractBoolean(node, "hasParking"));
			station.getServices().setBicycleParking(extractBoolean(node, "hasBicycleParking"));
			station.getServices().setLocalPublicTransport(extractBoolean(node, "hasLocalPublicTransport"));
			station.getServices().setPublicFacilities(extractBoolean(node, "hasPublicFacilities"));
			station.getServices().setLockerSystem(extractBoolean(node, "hasLockerSystem"));
			station.getServices().setTaxiRank(extractBoolean(node, "hasTaxiRank"));
			station.getServices().setTravelNecessities(extractBoolean(node, "hasTravelNecessities"));
			station.getServices().setBarrierFree(extractBoolean(node, "hasSteplessAccess"));
			station.getServices().setWifi(extractBoolean(node, "hasWiFi"));
			station.getServices().setTravelCenter(extractBoolean(node, "hasTravelCenter"));
			station.getServices().setRailwayMission(extractBoolean(node, "hasRailwayMission"));
			station.getServices().setHasCarRental(extractBoolean(node, "hasCarRental"));
			station.getApiIds().put("stada", node.get("number").asLong());
			station.getApiSources().add(URL);
			progress.count();
			counter++;
			repository.add(station);
		}
		for (String warn : warns) {
			LOGGER.warn(warn);
		}
		LOGGER.info("Downloaded {} stations from {}", counter, NAME, URL);
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
	
	public static String getUrl() {
		return URL;
	}

}
