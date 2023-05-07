package eu.derzauberer.pis.downloader;

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

public class DbRisStationsDownloader {
	
	private static final String NAME = "db/ris::stations";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/ris-stations/v1/stations";
	private static final Logger LOGGER = LoggerFactory.getLogger(DbRisStationsDownloader.class);
	private final Repository<Station> repository = (Repository<Station>) Pis.getRepository("stations", Station.class);
	
	public DbRisStationsDownloader() {
		LOGGER.info("Downloading {} from {}", NAME, URL);
		final HttpRequest request = new HttpRequest();
		request.setUrl(URL);
		request.getParameter().put("limit","10000");
		request.getHeader().put("DB-Client-Id", Pis.getUserConfig().getDbClientId());
		request.getHeader().put("DB-Api-Key", Pis.getUserConfig().getDbApiKey());
		request.setExceptionAction(exception -> LOGGER.error("Downloading {} from {} failed: {} {}", repository.getName(), NAME, exception.getClass().getSimpleName(), exception.getMessage()));
		request.request().map(this::logDownloadProcessing).map(HttpRequest::mapToJson).ifPresent(this::saveAll);
	}
	
	private void saveAll(ObjectNode json) {
		final ProgressStatus progress = new ProgressStatus("Processing", NAME, json.withArray("stations").size());
		int counter = 0;
		for (JsonNode node : json.withArray("stations")) {
			final String name = node.at("/names/DE/name").asText();
			final Station station = repository.getById(Entity.nameToId(name)).orElse(new Station(name));
			if (node.at("/owner/name").asText().equalsIgnoreCase("DB S&S")) {
				station.getOrCreateAdress().setName("DB Station&Service AG");
			} else {
				station.getOrCreateAdress().setName(node.at("/owner/name").asText());
			}
			station.getOrCreateAdress().setStreet(node.at("/address/street").asText() + " " + node.at("/address/houseNumber").asText());
			station.getOrCreateAdress().setPostalCode(node.at("/address/postalCode").asInt());
			station.getOrCreateAdress().setCity(node.at("/address/city").asText());
			if (node.at("/address/country").asText().equalsIgnoreCase("de")) {
				station.getOrCreateAdress().setCountry("Germany");
			} else if (node.at("/address/country").asText().equalsIgnoreCase("ch")) {
				station.getOrCreateAdress().setCountry("Switzerland");
			} else {
				station.getOrCreateAdress().setCountry(node.at("/address/country").asText());
			}
			station.setLocation(new Location(node.at("/position/latitude").asDouble(), node.at("/position/longitude").asDouble()));
			station.getOrCreateApiInformation().addId("stada", node.get("stationID").asLong());
			station.getOrCreateApiInformation().addSource(URL);
			progress.count();
			counter++;
			repository.add(station);
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
