package com.biermacht.brews.recipe;

public class BeerStyle {
	
	// Categories based on beerXMl standard
	private String name;
	private String category;
	private Integer version;
	private String categoryNumber;
	private String styleLetter;
	private String styleGuide;
	private String  type;
	private String notes;
	private String profile;
	private String ingredients;
	private String examples;
	private RecipeReccomendedValues reccomendedValues;
	
	// More
	private long ownerId;
	
	// Defines
	public static String TYPE_ALE = "Ale";
	public static String TYPE_LAGER = "Lager";
	public static String TYPE_MEAD = "Mead";
	public static String TYPE_WHEAT = "Wheat";
	public static String TYPE_MIXED = "Mixed";
	public static String TYPE_CIDER = "Cider";
	
	public BeerStyle(String name)
	{
		setName(name);
		this.reccomendedValues = new RecipeReccomendedValues();
		setType("");
		setCategory("");
		setStyleLetter("");
		setNotes("");
		setExamples("");
		setIngredients("");
		setProfile("");
		setStyleGuide("");
		setCategoryNumber("");
		setVersion(1);
		setOwnerId(-1);
	}
	
	public BeerStyle(String name, RecipeReccomendedValues rrv)
	{
		setName(name);
		this.reccomendedValues = rrv;
		setType("");
		setCategory("");
		setStyleLetter("");
		setNotes("");
		setExamples("");
		setIngredients("");
		setProfile("");
		setStyleGuide("");
		setCategoryNumber("");
		setVersion(1);
		setOwnerId(-1);
	}
	
	@Override
	public boolean equals(Object o)
	{
		// Fist make sure its a BeerStyle
		if (!(o instanceof BeerStyle))
			return false;
		
		// Based only off the name
		if (this.toString().equals(o.toString()))
			return true;
		else
			return false;
		
	}
	
	public String toString()
	{
		return name;
	}
	
	public void setOwnerId(long i)
	{
		this.ownerId = i;
	}
	
	public long getOwnerId()
	{
		return this.ownerId;
	}
	
	public void setMinCarb(double d)
	{
		// TODO
	}
	
	public void setMaxCarb(double d)
	{
		// TODO
	}
	
	public void setName(String s)
	{
		this.name = s;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setType(String s)
	{
		this.type = s;
	}
	
	public void setCategory(String s)
	{
		this.category = s;
	}
	
	public void setStyleLetter(String s)
	{
		this.styleLetter = s;
	}
	
	public void setNotes(String s)
	{
		this.notes = s;
	}
	
	public void setExamples(String s)
	{
		this.examples = s;
	}
	
	public void setProfile(String s)
	{
		this.profile = s;
	}
	
	public void setCategoryNumber(String s)
	{
		this.categoryNumber = s;
	}
	
	public void setStyleGuide(String s)
	{
		this.styleGuide = s;
	}
	
	public void setIngredients(String s)
	{
		this.ingredients = s;
	}
	
	public void setVersion(int i)
	{
		this.version = i;
	}
	
	public RecipeReccomendedValues getReccomendedValues()
	{
		return this.reccomendedValues;
	}
	
	public void setMinOg(double d)
	{
		this.reccomendedValues.setMinOG(d);
	}
	
	public void setMaxOg(double d)
	{
		this.reccomendedValues.setMaxOG(d);
	}
	
	public void setMinFg(double d)
	{
		this.reccomendedValues.setMinFG(d);
	}
	
	public void setMaxFg(double d)
	{
		this.reccomendedValues.setMaxFG(d);
	}
	
	public void setMinIbu(double d)
	{
		this.reccomendedValues.setMinBitter(d);
	}
	
	public void setMaxIbu(double d)
	{
		this.reccomendedValues.setMaxBitter(d);
	}
	
	public void setMinColor(double d)
	{
		this.reccomendedValues.setMinColor(d);
	}
	
	public void setMaxColor(double d)
	{
		this.reccomendedValues.setMaxColor(d);
	}
	
	public void setMinAbv(double d)
	{
		this.reccomendedValues.setMinAbv(d);
	}

	public void setMaxAbv(double d)
	{
		this.reccomendedValues.setMaxAbv(d);
	}
	
	public String getCategory()
	{
		return this.category;
	}
	
	public String getCatNum()
	{
		return this.categoryNumber;
	}
	
	public String getStyleLetter()
	{
		return this.styleLetter;
	}
	
	public String getStyleGuide()
	{
		return this.styleGuide;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public double getMinOg()
	{
		return this.reccomendedValues.getMinOG();
	}
	
	public double getMaxOg()
	{
		return this.reccomendedValues.getMaxOG();
	}
	
	public double getMinFg()
	{
		return this.reccomendedValues.getMinFG();
	}
	
	public double getMaxFg()
	{
		return this.reccomendedValues.getMaxFG();
	}
	
	public double getMinIbu()
	{
		return this.reccomendedValues.getMinBitter();
	}
	
	public double getMaxIbu()
	{
		return this.reccomendedValues.getMaxBitter();
	}
	
	public double getMinColor()
	{
		return this.reccomendedValues.getMinColor();
	}
	
	public double getMaxColor()
	{
		return this.reccomendedValues.getMaxColor();
	}
	
	public double getMinCarb()
	{
		return 0; // TODO
	}
	
	public double getMaxCarb()
	{
		return 0; // TODO
	}
	
	public double getMinAbv()
	{
		return this.reccomendedValues.getMinAbv();
	}
	
	public double getMaxAbv()
	{
		return this.reccomendedValues.getMaxAbv();
	}
	
	public String getNotes()
	{
		return this.notes;
	}
	
	public String getProfile()
	{
		return this.profile;
	}
	
	public String getIngredients()
	{
		return this.ingredients;
	}
	
	public String getExamples()
	{
		return this.examples;
	}
}
