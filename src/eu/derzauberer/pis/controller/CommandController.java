package eu.derzauberer.pis.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.derzauberer.pis.configuration.UserConfiguration;
import eu.derzauberer.pis.downloader.DbRisPlatformsDownloader;
import eu.derzauberer.pis.downloader.DbRisStationsDownloader;
import eu.derzauberer.pis.downloader.DbStadaStationDownloader;
import eu.derzauberer.pis.service.EntityService;
import eu.derzauberer.pis.service.LineService;
import eu.derzauberer.pis.service.OperatorService;
import eu.derzauberer.pis.service.StationService;
import eu.derzauberer.pis.service.TypeService;
import eu.derzauberer.pis.service.UserService;

@RestController
@RequestMapping("/api/commands")
@SuppressWarnings("unused")
public class CommandController {
	
	private final UserConfiguration config;
	private final LineService lineService;
	private final OperatorService operatorService;
	private final StationService stationService;
	private final TypeService typeService;
	private final UserService userService;
	private final Map<String, EntityService<?>> services;
	
	@Autowired
	public CommandController(
			UserConfiguration config,
			LineService lineService,
			OperatorService operatorService,
			StationService stationService,
			TypeService typeService,
			UserService userService
			) {
		this.config = config;
		this.lineService = lineService;
		this.operatorService = operatorService;
		this.stationService = stationService;
		this.typeService = typeService;
		this.userService = userService;
		services = new HashMap<>();
		services.put(lineService.getName(), lineService);
		services.put(operatorService.getName(), operatorService);
		services.put(stationService.getName(), stationService);
		services.put(typeService.getName(), typeService);
		services.put(userService.getName(), userService);
	}

	@GetMapping("/extract")
	public void extractEntities(
			@RequestParam(name = "type", required = true) String typeName, 
			@RequestParam(name = "path", required = true) String pathString
			) {
		final EntityService<?> service = services.get(typeName);
		Path path;
		if (service == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type " + typeName + " does not exist!");
		}
		try {
			path = Paths.get(pathString);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The path " + pathString + " is not valid!");
		}
		if (!Files.exists(path)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file " + pathString + " does not exist!");
		}
		service.extractEntities(path);
	}
	
	@GetMapping("/package")
	public void packageEntities(
			@RequestParam(name = "type", required = true) String typeName, 
			@RequestParam(name = "path", required = true) String pathString
			) {
		final EntityService<?> service = services.get(typeName);
		Path path;
		if (service == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type " + typeName + " does not exist!");
		}
		try {
			path = Paths.get(pathString);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The path " + pathString + " is not valid!");
		}
		service.packageEntities(path);
	}
	
	@GetMapping("/download")
	public void downloadEntities(@RequestParam(name = "downloader", required = true) String donwloaderName) {
		final Map<String, Runnable> downloaderMap = new HashMap<>();
		downloaderMap.put(DbStadaStationDownloader.getName(), () -> new DbStadaStationDownloader(config, stationService));
		downloaderMap.put(DbRisStationsDownloader.getName(), () -> new DbRisStationsDownloader(config, stationService));
		downloaderMap.put(DbRisPlatformsDownloader.getName(), () -> new DbRisPlatformsDownloader(config, stationService));
		final Runnable downloader = downloaderMap.get(donwloaderName);
		if (downloader == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The downloader " + donwloaderName + " does not exist!");
		}
		downloader.run();
	}
	
}
