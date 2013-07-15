package com.biermacht.brews.recipe;

public class RecipeReccomendedValues {
	
	private double minOG;
	private double maxOG;
	private double minFG;
	private double maxFG;
	private double minBitter;
	private double maxBitter;
	private double minColor;
	private double maxColor;
	private double minAbv;
	private double maxAbv;
	
	public RecipeReccomendedValues()
	{
		this.minOG = 1;
		this.maxOG = 2;
		this.minFG = 1;
		this.maxFG = 2;
		this.minBitter = 0;
		this.maxBitter = 200;
		this.minColor = 0;
		this.maxColor = 100;
		this.minAbv = 0;
		this.maxAbv = 100;
	}
	
	public RecipeReccomendedValues(double minOG, double maxOG, double minFG, double maxFG, double minBit, double maxBit, double minCol, double maxCol, double minAbv, double maxAbv)
	{
		this.minOG = minOG;
		this.maxOG = maxOG;
		this.minFG = minFG;
		this.maxFG = maxFG;
		this.minBitter = minBit;
		this.maxBitter = maxBit;
		this.minColor = minCol;
		this.maxColor = maxCol;
		this.minAbv = minAbv;
		this.maxAbv = maxAbv;
	}
	
	public boolean isEmpty(double d)
	{
		return (d <= 0);
	}
	
	// Methods for getting RANGE
	public String getOGRange()
	{
		if (isEmpty(minOG))
			return "Custom";
		
		String s = "";
		s += String.format("%2.3f", minOG);
		s += " - ";
		s += String.format("%2.3f", maxOG);
		
		return s;
	}
	
	public String getFGRange()
	{
		if (isEmpty(minFG))
			return "Custom";
		
		String s = "";
		s += String.format("%2.3f", minFG);
		s += " - ";
		s += String.format("%2.3f", maxFG);
		
		return s;
	}
	
	public String getColorRange()
	{
		if (isEmpty(minColor))
			return "Custom";
		
		String s = "";
		s += String.format("%2.1f", minColor);
		s += " - ";
		s += String.format("%2.1f", maxColor);
		
		return s;
	}
	
	public String getBitterRange()
	{
		if (isEmpty(minBitter))
			return "Custom";
			
		String s = "";
		s += String.format("%2.1f", minBitter);
		s += " - ";
		s += String.format("%2.1f", maxBitter);
		
		return s;
	}
	
	public String getAbvRange()
	{
		if (isEmpty(minAbv))
			return "Custom";
			
		String s = "";
		s += String.format("%2.1f", minAbv);
		s += " - ";
		s += String.format("%2.1f", maxAbv);
		
		return s;
	}
	
	
	// Methods for getting individual mins and maxes
	public double getMinOG() {
		return minOG;
	}

	public void setMinOG(double minGrav) {
		this.minOG = minGrav;
	}

	public double getMaxOG() {
		return maxOG;
	}

	public void setMaxOG(double maxGrav) {
		this.maxOG = maxGrav;
	}

	public double getMinBitter() {
		return minBitter;
	}

	public void setMinBitter(double minBitter) {
		this.minBitter = minBitter;
	}

	public double getMaxBitter() {
		return maxBitter;
	}

	public void setMaxBitter(double maxBitter) {
		this.maxBitter = maxBitter;
	}

	public double getMinColor() {
		return minColor;
	}

	public void setMinColor(double minColor) {
		this.minColor = minColor;
	}

	public double getMaxColor() {
		return maxColor;
	}

	public void setMaxColor(double maxColor) {
		this.maxColor = maxColor;
	}

	public double getMinAbv() {
		return minAbv;
	}

	public void setMinAbv(double minAbv) {
		this.minAbv = minAbv;
	}

	public double getMaxAbv() {
		return maxAbv;
	}

	public void setMaxAbv(double maxAbv) {
		this.maxAbv = maxAbv;
	}

	public double getMaxFG() {
		return maxFG;
	}

	public void setMaxFG(double maxFG) {
		this.maxFG = maxFG;
	}

	public double getMinFG() {
		return minFG;
	}

	public void setMinFG(double minFG) {
		this.minFG = minFG;
	}
}
