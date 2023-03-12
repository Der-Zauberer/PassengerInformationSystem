package eu.derzauberer.pis.commands;

import java.util.HashMap;
import java.util.Map;

import eu.derzauberer.pis.downloader.DbStationDownloader;
import eu.derzauberer.pis.util.Command;
import eu.derzauberer.pis.util.Downloader;

public class DownloadCommand extends Command {

	public DownloadCommand() {
		super("download");
		setDescription("Downloads entities from an api to the data directory. Download an existing entity will only update the downloaded fields instead of overwriting the whole entity.");
		setUsage("<api>");
		setMinArguments(1);
		setAction(args -> {
			final Map<String, Downloader> downloaderMap = new HashMap<>();
			downloaderMap.put(DbStationDownloader.getName(), new DbStationDownloader());
			final Downloader downloader = downloaderMap.get(args[0]);
			if (downloader == null) {
				LOGGER.error("The downloader {} does not exist!", args[0]);
				return;
			}
			downloader.download();
		});
	}

}
