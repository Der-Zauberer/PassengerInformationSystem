package eu.derzauberer.pis.structure.model;


public class ColorModel {
	
	private String textColor ;
	private String backgroundColor;
	
	
	public ColorModel() {
		textColor = "#ffffff";
		backgroundColor = "#808080";
	}
	
	public ColorModel(String textColor, String backgroundColor) {
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
	}
	
	public ColorModel(ColorModel color) {
		this.textColor = color.textColor;
		this.backgroundColor = color.backgroundColor;
	}
	
	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}
