package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import eu.derzauberer.pis.util.Entity;

@JsonPropertyOrder({ "id", "type", "number", "operator", "stops", "api" })
public class LineSceduled extends Line<LineStop> implements Entity<LineSceduled> {
	
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;
	
	private Boolean holiday;
	private Boolean scoolHoliday;
	
	private List<LocalDate> inclusiveDates = new ArrayList<>();
	private List<LocalDate> exclusiveDates = new ArrayList<>();

	@ConstructorProperties({ "id", "type", "number" })
	public LineSceduled(String id, TrainType type, int number) {
		super(id, type, number);
	}

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public boolean isSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	public Boolean getHoliday() {
		return holiday;
	}

	public void setHoliday(Boolean holiday) {
		this.holiday = holiday;
	}

	public Boolean getScoolHoliday() {
		return scoolHoliday;
	}

	public void setScoolHoliday(Boolean scoolHoliday) {
		this.scoolHoliday = scoolHoliday;
	}

	public List<LocalDate> getInclusiveDates() {
		return inclusiveDates;
	}

	public List<LocalDate> getExclusiveDates() {
		return exclusiveDates;
	}
	
}
