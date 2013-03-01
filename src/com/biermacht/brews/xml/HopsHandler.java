package com.biermacht.brews.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.biermacht.brews.ingredient.Hop;
 
public class HopsHandler extends DefaultHandler {
 
    boolean currentElement = false;
    String currentValue = null;
    
    Hop hop = null;
    
    public Hop getHop() {
        return hop;
    }
 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
 
        currentElement = true;
 
        if (qName.equals("HOP"))
        {
            hop = new Hop("");
        } 
        
    }
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
 
        currentElement = false;
 
        if (qName.equalsIgnoreCase("NAME"))
        {
        	hop.setName(currentValue);
        }
        	
        else if (qName.equalsIgnoreCase("VERSION"))
        {
        	// TODO: Set version!
        }
        
        else if (qName.equalsIgnoreCase("ORIGIN"))
        {
        	hop.setOrigin(currentValue);
        }
        
        else if (qName.equalsIgnoreCase("ALPHA"))
        {
        	hop.setAlphaAcidContent(Double.parseDouble(currentValue));
        }
        
        else if (qName.equalsIgnoreCase("AMOUNT"))
        {
        	hop.setAmount(Double.parseDouble(currentValue));
        }
        
        else if (qName.equalsIgnoreCase("USE"))
        {
        	String use = "";
        	
        	if (currentValue.equalsIgnoreCase(Hop.USE_AROMA))
        		use = Hop.USE_AROMA;
        	if (currentValue.equalsIgnoreCase(Hop.USE_BOIL));
        		use = Hop.USE_BOIL;
        	if (currentValue.equalsIgnoreCase(Hop.USE_DRY_HOP))
        		use = Hop.USE_DRY_HOP;
        	if (currentValue.equalsIgnoreCase(Hop.USE_MASH))
        		use = Hop.USE_MASH;
        	if (currentValue.equalsIgnoreCase(Hop.USE_FIRST_WORT))
        		use = Hop.USE_FIRST_WORT;
        	
        	hop.setUse(use);
        }
        
        else if (qName.equalsIgnoreCase("TIME"))
        {
        	hop.setTime(Integer.parseInt(currentValue));
        }
        
        else if (qName.equalsIgnoreCase("NOTES"))
        {
        	hop.setDescription(currentValue);
        }

        else if (qName.equalsIgnoreCase("TYPE"))
        {
        	String type = "";
        	
        	if (currentValue.equalsIgnoreCase(Hop.TYPE_AROMA))
        		type = Hop.TYPE_AROMA;
        	if (currentValue.equalsIgnoreCase(Hop.TYPE_BITTERING))
        		type = Hop.TYPE_BITTERING;
        	if (currentValue.equalsIgnoreCase(Hop.TYPE_BOTH))
        		type = Hop.TYPE_BOTH;
        	
        	hop.setHopType(type);
        }
        
        else if (qName.equalsIgnoreCase("FORM"))
        {
        	String form = "";
        	
        	if (currentValue.equalsIgnoreCase(Hop.FORM_PELLET))
        		form = Hop.FORM_PELLET;
        	if (currentValue.equalsIgnoreCase(Hop.FORM_WHOLE))
        		form = Hop.FORM_WHOLE;
        	if (currentValue.equalsIgnoreCase(Hop.FORM_PLUG))
        		form = Hop.FORM_PLUG;
        	
        	hop.setForm(form);
        }
        
        else
        {
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
