package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

public class Information implements Comparable<Information> {
	
	private String title;
	private String text;
	private InformationType type;
	
	@ConstructorProperties({ "title", "text", "type" })
	public Information(String title, String text, InformationType type) {
		super();
		this.title = title;
		this.text = text;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public InformationType getType() {
		return type;
	}
	
	public void setType(InformationType type) {
		this.type = type;
	}

	@Override
	public int compareTo(Information o) {
		return Integer.compare(o.getType().getPriority(), getType().getPriority());
	}

}
