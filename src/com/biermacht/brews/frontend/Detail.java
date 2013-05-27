package com.biermacht.brews.frontend;

public class Detail
{
	private String title;
	private String type;
	private String content;
	private Double value;
	private Double min;
	private Double max;
	private String format;
	
	public static String TYPE_RANGE = "range";
	public static String TYPE_TEXT = "text";
	public static String TYPE_BLANK = "blank";
	
	public Detail()
	{
		this.title = "Empty Detail";
		this.type = TYPE_RANGE;
		setContent("");
		this.value = 0.0;
		this.min = 0.0;
		this.max = 0.0;
		setFormat("%s");
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public void setValue(Double value)
	{
		this.value = value;
	}
	
	public void setMin(Double min)
	{
		this.min = min;
	}
	
	public void setMax(Double max)
	{
		this.max = max;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public Double getValue()
	{
		return this.value;
	}
	
	public Double getMin()
	{
		return this.min;
	}
	
	public Double getMax()
	{
		return this.max;
	}
	
	public String getFormat()
	{
		return this.format;
	}
	
	public void setFormat(String format)
	{
		this.format = format;
	}
}
