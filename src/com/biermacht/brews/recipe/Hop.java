package com.biermacht.brews.recipe;

public class Hop extends Ingredient {
	
	private String description;
	private double alphaAcidContent;
	private int boilTime;
	private String hopType;
	
	public static final String TYPE_PELLET = "Pellet";
	public static final String TYPE_WHOLE = "Whole";
	public static final String TYPE_PLUG = "Plug";

	public Hop(String name)
	{
		super(name);
		this.setAlphaAcidContent(0);
		this.setDescription("No Description");
		this.setBoilTime(60);
		this.setHopType(TYPE_PELLET);
	}
	
	public Hop(String name, double aAcid, String desc) {
		super(name);
		this.setAlphaAcidContent(aAcid);
		this.setDescription(desc);
		this.setBoilTime(60);
		this.setHopType(TYPE_PELLET);
	}

	@Override
	public String getType() {
		return Ingredient.HOP;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAlphaAcidContent() {
		return alphaAcidContent;
	}

	public void setAlphaAcidContent(double alphaAcidContent) {
		this.alphaAcidContent = alphaAcidContent;
	}

	public int getBoilTime() {
		return boilTime;
	}

	public void setBoilTime(int boilTime) {
		this.boilTime = boilTime;
	}	
	
	public void setWeight(double w)
	{
		setAmount(w);
	}
	
	public double getWeight()
	{
		return getAmount();
	}

	public String getHopType() {
		return hopType;
	}

	public void setHopType(String hopType) {
		this.hopType = hopType;
	}
}