package eu.derzauberer.pis.downloader;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.Entity;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.util.Downloader;
import eu.derzauberer.pis.util.Repository;

public class DbStationDownloader extends Downloader {
	
	private static final String NAME = "db/stada";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/station-data/v2/stations";
	
	private Repository<Station, String> repository;
	
	public DbStationDownloader() {
		super(NAME);
	}
	
	@Override
	public void download() {
		final Map<String, String> parameters = new HashMap<>();
		final Map<String, String> header = new HashMap<>();
		header.put("DB-Client-Id", Pis.getUserConfig().getDbClientId());
		header.put("DB-Api-Key", Pis.getUserConfig().getDbApiKey());
		repository = Pis.getRepository("stations", Station.class, String.class);
		LOGGER.info("Downloading {} from {}", NAME, URL);
		download(URL, parameters, header).ifPresent(this::proccess);
	}
	
	private void proccess(ObjectNode json) {
		int counter = 0;
		for (JsonNode node : json.withArray("result")) {
			final String name = node.get("name").asText();
			final Station station = repository.get(Entity.nameToId(name)).orElse(new Station(name));
			station.getAdress().setStreet(node.at("/mailingAddress/street").asText());
			station.getAdress().setPostalCode(node.at("/mailingAddress/zipcode").asInt());
			station.getAdress().setCity(node.at("/mailingAddress/city").asText());
			station.getAdress().setCountry("Germany");
			if (!node.get("evaNumbers").isEmpty() && !node.get("evaNumbers").get(0).isEmpty()) {
				final JsonNode location = node.get("evaNumbers").get(0).at("/geographicCoordinates/coordinates");
				if (!location.isEmpty()) {
					station.getLocation().setLongitude(location.get(0).asDouble());
					station.getLocation().setLatitude(location.get(1).asDouble());
				} else {
					LOGGER.warn("Station {} does not contain geo-coordinates!", station.getId());
				}
			} else {
				LOGGER.warn("Station {} does not contain geo-coordinates!", station.getId());
			}
			counter++;
			repository.add(station);
		}
		LOGGER.info("Downloaded {} stations from {}", counter, NAME, URL);
	}
	
	public static String getName() {
		return NAME;
	}
	
	public static String getUrl() {
		return URL;
	}

}
