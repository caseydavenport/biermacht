package com.biermacht.brews.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.biermacht.brews.ingredient.Yeast;
 
public class YeastHandler extends DefaultHandler {
 
    boolean currentElement = false;
    String currentValue = null;
    
    Yeast yeast = null;
    
    public Yeast getYeast() {
        return yeast;
    }
 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 
        currentElement = true;
 
        if (qName.equals("YEAST"))
        {
        	yeast = new Yeast("");
        } 
        
    }
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
 
        currentElement = false;
 
        if (qName.equalsIgnoreCase("NAME"))
        {
        	yeast.setName(currentValue);
        }
        	
        else if (qName.equalsIgnoreCase("VERSION"))
        {
        	int version = Integer.parseInt(currentValue);
        	yeast.setVersion(version);
        }

        else if (qName.equalsIgnoreCase("TYPE"))
        {
        	String type = "Invalid Type";
        	
        	if (currentValue.equalsIgnoreCase(Yeast.ALE))
        		type = Yeast.ALE;
        	if (currentValue.equalsIgnoreCase(Yeast.LAGER))
        		type = Yeast.LAGER;
        	if (currentValue.equalsIgnoreCase(Yeast.WHEAT))
        		type = Yeast.WHEAT;
        	if (currentValue.equalsIgnoreCase(Yeast.WINE))
        		type = Yeast.WINE;
        	if (currentValue.equalsIgnoreCase(Yeast.CHAMPAGNE))
        		type = Yeast.CHAMPAGNE;
        	
        	yeast.setType(type);
        }
        
        else if (qName.equalsIgnoreCase("FORM"))
        {
        	String form = "Invalid Form";
        	
        	if (currentValue.equalsIgnoreCase(Yeast.CULTURE))
        		form = Yeast.CULTURE;
        	if (currentValue.equalsIgnoreCase(Yeast.DRY))
        		form = Yeast.DRY;
        	if (currentValue.equalsIgnoreCase(Yeast.LIQUID))
        		form = Yeast.LIQUID;

        	yeast.setForm(form);
        }
        
        else if (qName.equalsIgnoreCase("AMOUNT"))
        {
        	double amt = Double.parseDouble(currentValue);
        	yeast.setAmount(amt);
        }
        
        else if (qName.equalsIgnoreCase("AMOUNT_IS_WEIGHT"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("LABORATORY"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("PRODUCT_ID"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("MIN_TEMPERATURE"))
        {
        	double minTemp = Double.parseDouble(currentValue);
        	yeast.setMinTemp(minTemp);
        }
        
        else if (qName.equalsIgnoreCase("MAX_TEMPERATURE"))
        {
        	double maxTemp = Double.parseDouble(currentValue);
        	yeast.setMaxTemp(maxTemp);
        }
        
        else if (qName.equalsIgnoreCase("FLOCCULATION"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("ATTENUATION"))
        {
        	double attenuation = Double.parseDouble(currentValue);
        	yeast.setAttenuation(attenuation);
        }
        
        else if (qName.equalsIgnoreCase("NOTES"))
        {
        	yeast.setNotes(currentValue);
        }
        
        else if (qName.equalsIgnoreCase("BEST_FOR"))
        {
        	yeast.setBestFor(currentValue);
        }
        
        else if (qName.equalsIgnoreCase("MAX_REUSE"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("TIMES_CULTURED"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("ADD_TO_SECONDARY"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("DISPLAY_AMOUNT"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("DISP_MIN_TEMP"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("DISP_MAX_TEMP"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("INVENTORY"))
        {
        	// TODO: FUCK THIS SHIT
        }
        
        else if (qName.equalsIgnoreCase("CULTURE_DATE"))
        {
        	// TODO: Add support for this field
        }

        else
        {
        	// TODO: Add support for this field
        }
    }
 
    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException {
 
        if (currentElement) {
            currentValue = new String(ch, start, length);
            currentElement = false;
        }
 
    }
 
}