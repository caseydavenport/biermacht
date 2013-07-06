package com.biermacht.brews.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.biermacht.brews.ingredient.Fermentable;
 
public class FermentableHandler extends DefaultHandler {
 
    boolean currentElement = false;
    String currentValue = null;
    
    Fermentable fermentable = null;
    
    public Fermentable getFermentable() {
        return fermentable;
    }
 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 
        currentElement = true;
 
        if (qName.equals("FERMENTABLE"))
        {
            fermentable = new Fermentable("");
        } 
    }
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
 
        currentElement = false;
 
        if (qName.equalsIgnoreCase("NAME"))
        {
        	fermentable.setName(currentValue);
        }
        	
        else if (qName.equalsIgnoreCase("VERSION"))
        {
        	// TODO: Set version!
        }

        else if (qName.equalsIgnoreCase("TYPE"))
        {
        	String type = "NULL";
        	
        	if (currentValue.equalsIgnoreCase(Fermentable.ADJUNCT))
        		type = Fermentable.ADJUNCT;
        	if (currentValue.equalsIgnoreCase(Fermentable.EXTRACT))
        		type = Fermentable.EXTRACT;
			if (currentValue.contains("Extract"))
				type = Fermentable.EXTRACT;
        	if (currentValue.equalsIgnoreCase(Fermentable.GRAIN))
        		type = Fermentable.GRAIN;
			if (currentValue.equalsIgnoreCase(Fermentable.SUGAR))
				type = Fermentable.SUGAR;
        	
        	fermentable.setFermentableType(type);
        }
        
        else if (qName.equalsIgnoreCase("AMOUNT"))
        {
        	double amt = Double.parseDouble(currentValue);
        	fermentable.setBeerXmlStandardAmount(amt);
        }
        
        else if (qName.equalsIgnoreCase("YIELD"))
        {
        	double yield = Double.parseDouble(currentValue);
        	fermentable.setYield(yield);
        }
        
        else if (qName.equalsIgnoreCase("COLOR"))
        {
        	double color = Double.parseDouble(currentValue);
        	fermentable.setLovibondColor(color);
        }
        
        else if (qName.equalsIgnoreCase("ADD_AFTER_BOIL"))
        {
        	boolean aab = (currentValue.equalsIgnoreCase("FALSE")) ? false : true;
        	fermentable.setAddAfterBoil(aab);
        }
        
        else if (qName.equalsIgnoreCase("ORIGIN"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("SUPPLIER"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("NOTES"))
        {
        	fermentable.setShortDescription(currentValue);
        }
        
        else if (qName.equalsIgnoreCase("COARSE_FINE_DIFF"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("MOISTURE"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("DIASTATIC_POWER"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("PROTEIN"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("MAX_IN_BATCH"))
        {
        	if (currentElement != false)
        	{
	        	double maxInBatch = Double.parseDouble(currentValue);
	        	fermentable.setMaxInBatch(maxInBatch);
        	}
        }
        
        else if (qName.equalsIgnoreCase("RECOMMEND_MASH"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("IBU_GAL_PER_LB"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("DISPLAY_AMOUNT"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("INVENTORY"))
        {
        	// TODO: Add support for this field
        }
        
        else if (qName.equalsIgnoreCase("POTENTIAL"))
        {
        	// TODO: FUCK THIS SHIT
        }
        
        else if (qName.equalsIgnoreCase("DISPLAY_COLOR"))
        {
        	// TODO: Add support for this field
        }

        else if (qName.equalsIgnoreCase("FERMENTABLES"))
        {
			// This is where we will add the current fermentable
			// to the list.
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
