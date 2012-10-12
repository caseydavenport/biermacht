package com.biermacht.brews.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
        	fermentable.setFermentableType(qName);
        }
        
        else if (qName.equalsIgnoreCase("AMOUNT"))
        {
        	double amt = Double.parseDouble(currentValue);
        	fermentable.setAmount(amt);
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
        	// TODO: Add support for this field
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
        	double maxInBatch = Double.parseDouble(currentValue);
        	fermentable.setMaxInBatch(maxInBatch);
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
        	Double gravity = Double.parseDouble(currentValue);
        	fermentable.setGravity(gravity);
        }
        
        else if (qName.equalsIgnoreCase("DISPLAY_COLOR"))
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