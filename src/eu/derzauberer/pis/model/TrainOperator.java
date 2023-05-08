package eu.derzauberer.pis.model;

import java.beans.ConstructorProperties;

public class TrainOperator implements Entity<TrainOperator>, NameEntity {

	private final String id;
	private String name;
	private Adress adress;
	private int backgorundColor;
	private int textColor;
	private ApiInformation api;
	
	public TrainOperator(String name) {
		this(NameEntity.nameToId(name), name);
	}
	
	@ConstructorProperties({ "id", "name" })
	public TrainOperator(String id, String name) {
		this.id = id;
		this.name = name;
		this.backgorundColor = 0x000000;
		this.textColor = 0xffffff;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Adress getAdress() {
		return adress;
	}
	
	public Adress getOrCreateAdress() {
		if (adress == null) adress = new Adress();
		return adress;
	}
	
	public void setAdress(Adress adress) {
		this.adress = adress;
	}
	
	public int getPrimaryColor() {
		return backgorundColor;
	}
	
	public void setPrimaryColor(int primaryColor) {
		this.backgorundColor = primaryColor;
	}
	
	public int getSecondaryColor() {
		return textColor;
	}
	
	public void setSecondaryColor(int secondaryColor) {
		this.textColor = secondaryColor;
	}
	
	public ApiInformation getApi() {
		return api;
	}
	
	public ApiInformation getOrCreateApi() {
		if (api == null) api = new ApiInformation();
		return api;
	}
	
	public void setApiInformation(ApiInformation api) {
		this.api = api;
	}

}
