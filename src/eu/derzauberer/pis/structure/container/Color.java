package eu.derzauberer.pis.structure.container;


public class Color {
	
	private String textColor ;
	private String backgroundColor;
	
	
	public Color() {
		textColor = "#ffffff";
		backgroundColor = "#808080";
	}
	
	public Color(String textColor, String backgroundColor) {
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
	}
	
	public Color(Color color) {
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
