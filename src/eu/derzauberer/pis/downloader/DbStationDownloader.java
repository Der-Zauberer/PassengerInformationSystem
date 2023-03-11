package eu.derzauberer.pis.downloader;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.Station;

public class DbStationDownloader extends DataDownloader {

	private final Map<String, String> parameters = new HashMap<>();
	private final Map<String, String> header = new HashMap<>();
	
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/station-data/v2/stations";
	
	public DbStationDownloader() {
		super("db/stada");
		header.put("DB-Client-Id", Pis.getUserConfig().getDbClientId());
		header.put("DB-Api-Key", Pis.getUserConfig().getDbApiKey());
		download(URL, parameters, header).ifPresent(json -> {
			for (JsonNode node : json.withArray("result")) {
				final Station station = new Station(node.get("name").asText());
				station.getAdress().setStreet(node.at("/mailingAddress/street").asText());
				station.getAdress().setPostalCode(node.at("/mailingAddress/zipcode").asInt());
				station.getAdress().setCity(node.at("/mailingAddress/city").asText());
				station.getAdress().setCountry("Germany");
				if (!node.get("evaNumbers").isEmpty() && !node.get("evaNumbers").get(0).isEmpty()) {
					final JsonNode location = node.get("evaNumbers").get(0).at("/geographicCoordinates/coordinates");
					if (!location.isEmpty()) {
						station.getLocation().setLongitude(location.get(0).asDouble());
						station.getLocation().setLatitude(location.get(1).asDouble());
					}
				}
				//Pis.getRepository("stations").add(station);
			}
		});
	}

}
