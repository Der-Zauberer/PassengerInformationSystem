package eu.derzauberer.pis.structure.model;

import java.beans.ConstructorProperties;
import java.time.LocalTime;

public class StationTrafficEntryModel implements Comparable<StationTrafficEntryModel> {
	
	private final LocalTime time;
	private final String lineId;
	private final int stationPosition;
	private final boolean lastStation;
	
	@ConstructorProperties({ "time", "lineId", "stationPosition", "platform", "platfromArea", "lastStation" })
	public StationTrafficEntryModel(LocalTime time, String lineId, int stationPosition, int platform, String platfromArea, boolean lastStation) {
		super();
		this.time = time;
		this.lineId = lineId;
		this.stationPosition = stationPosition;
		this.lastStation = lastStation;
	}
	
	public StationTrafficEntryModel(StationTrafficEntryModel stationTrafficEntry) {
		this.time = stationTrafficEntry.time;
		this.lineId = stationTrafficEntry.lineId;
		this.stationPosition = stationTrafficEntry.stationPosition;
		this.lastStation = stationTrafficEntry.lastStation;
	}
	
	public LocalTime getTime() {
		return time;
	}
	
	public String getLineId() {
		return lineId;
	}
	
	public int getStationPosition() {
		return stationPosition;
	}
	
	public boolean isLastStation() {
		return lastStation;
	}
	
	@Override
	public int compareTo(StationTrafficEntryModel entry) {
		return time.compareTo(entry.getTime());
	}

}
