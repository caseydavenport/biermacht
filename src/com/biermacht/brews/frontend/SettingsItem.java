package com.biermacht.brews.frontend;

public class SettingsItem
{
	private String title;
	private String text;
	private String type;
	
	public static String TYPE_TEXT = "Text";
	public static String TYPE_CHECK = "Check";
	
	public SettingsItem()
	{
		this.title = "No Title";
		this.text = "No text";
		this.type = TYPE_TEXT;
	}
	
	public SettingsItem(String title, String text, String type)
	{
		this.title = title;
		this.text = text;
		this.type = type;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public String getType()
	{
		return this.type;
	}
}
