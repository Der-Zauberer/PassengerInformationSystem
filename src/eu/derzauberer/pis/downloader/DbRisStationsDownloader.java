package eu.derzauberer.pis.downloader;

import java.time.LocalDate;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.derzauberer.pis.main.Pis;
import eu.derzauberer.pis.model.Adress;
import eu.derzauberer.pis.model.ApiInformation;
import eu.derzauberer.pis.model.Location;
import eu.derzauberer.pis.model.Station;
import eu.derzauberer.pis.util.Entity;
import eu.derzauberer.pis.util.HttpRequest;
import eu.derzauberer.pis.util.ProgressStatus;
import eu.derzauberer.pis.util.Repository;

public class DbRisStationsDownloader {
	
	private static final String NAME = "db/ris::stations";
	private static final String URL = "https://apis.deutschebahn.com/db-api-marketplace/apis/ris-stations/v1/stations";
	private static final Logger LOGGER = LoggerFactory.getLogger("Downloader");
	private final Repository<Station> repository = (Repository<Station>) Pis.getRepository("stations", Station.class);
	
	public DbRisStationsDownloader() {
		LOGGER.info("Downloading {} from {}", NAME, URL);
		final HttpRequest request = new HttpRequest();
		request.setUrl(URL);
		request.getParameter().put("limit","10000");
		request.getHeader().put("DB-Client-Id", Pis.getUserConfig().getDbClientId());
		request.getHeader().put("DB-Api-Key", Pis.getUserConfig().getDbApiKey());
		request.setExceptionAction(exception -> LOGGER.error("Downloading {} from {} failed: {} {}", repository.getName(), NAME, exception.getClass().getSimpleName(), exception.getMessage()));
		request.request().map(HttpRequest::mapToJson).ifPresent(this::saveAll);
	}
	
	private void saveAll(ObjectNode json) {
		final ProgressStatus progress = new ProgressStatus(NAME, json.withArray("stations").size());
		int counter = 0;
		for (JsonNode node : json.withArray("stations")) {
			final String name = node.at("/names/DE/name").asText();
			final Station station = repository.getById(Entity.nameToId(name)).orElse(new Station(name));
			if (station.getAdress() == null) station.setAdress(new Adress());
			if (node.at("/owner/name").asText().equalsIgnoreCase("DB S&S")) {
				station.getAdress().setName("DB Station&Service AG");
			} else {
				station.getAdress().setName(node.at("/owner/name").asText());
			}
			station.getAdress().setStreet(node.at("/address/street").asText() + " " + node.at("/address/houseNumber").asText());
			station.getAdress().setPostalCode(node.at("/address/postalCode").asInt());
			station.getAdress().setCity(node.at("/address/city").asText());
			if (node.at("/address/country").asText().equalsIgnoreCase("de")) {
				station.getAdress().setCountry("Germany");
			} else if (node.at("/address/country").asText().equalsIgnoreCase("ch")) {
				station.getAdress().setCountry("Switzerland");
			} else {
				station.getAdress().setCountry(node.at("/address/country").asText());
			}
			if (station.getLocation() == null) station.setLocation(new Location(0, 0));
			station.getLocation().setLongitude(node.at("/position/longitude").asDouble());
			station.getLocation().setLatitude(node.at("/position/latitude").asDouble());
			if (station.getApi() == null) station.setApi(new ApiInformation());
			if (station.getApi().getIds() == null) station.getApi().setIds(new HashMap<>());
			if (station.getApi().getSources() == null) station.getApi().setSources(new HashMap<>());
			station.getApi().getIds().put("stada", node.get("stationID").asLong());
			station.getApi().getSources().put(URL, LocalDate.now());
			progress.count();
			counter++;
			repository.add(station);
		}
		LOGGER.info("Downloaded {} stations from {}", counter, NAME, URL);
	}
	
	public static String getName() {
		return NAME;
	}
	
}
