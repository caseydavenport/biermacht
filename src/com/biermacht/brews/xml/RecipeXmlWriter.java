package com.biermacht.brews.xml;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.recipe.*;
import java.util.*;
import com.biermacht.brews.ingredient.*;

public class RecipeXmlWriter 
{
	private Context c;
	
	public RecipeXmlWriter(Context c)
	{
		this.c = c;
	}
	
	public void WriteRecipe(List<Recipe> list) 
	{
		// Open the document.
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try 
		{
			// This throws the exceptions!
			docBuilder = docFactory.newDocumentBuilder();

			// Create root element.
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("RECIPES");
			doc.appendChild(rootElement);
		
			for  (Recipe r : list)
			{
	 
				// Create element objects for a recipe.
				Element recipeElement = doc.createElement("RECIPE");
				Element recipeNameElement = doc.createElement("NAME");
				Element recipeVersionElement = doc.createElement("VERSION");
				Element recipeTypeElement = doc.createElement("TYPE");
				Element recipeStyleElement = doc.createElement("STYLE");
				Element recipeEquipmentElement = doc.createElement("EQUIPMENT");
				Element recipeBrewerElement = doc.createElement("BREWER");
				Element recipeBatchSizeElement = doc.createElement("BATCH_SIZE");
				Element recipeBoilSizeElement = doc.createElement("BOIL_SIZE");
				Element recipeBoilTimeElement = doc.createElement("BOIL_TIME");
				Element recipeEfficiencyElement = doc.createElement("EFFICIENCY");
				Element recipeNotesElement = doc.createElement("NOTES");
				Element recipeOGElement = doc.createElement("OG");
				Element recipeFGElement = doc.createElement("FG");
				Element recipeStagesElement = doc.createElement("FERMENTATION_STAGES");
				Element recipePrimaryAgeElement = doc.createElement("PRIMARY_AGE");
				Element recipeSecondaryAgeElement = doc.createElement("SECONDARY_AGE");
				Element recipeTertiaryAgeElement = doc.createElement("TERTIARY_AGE");
				Element recipeAgeElement = doc.createElement("AGE"); 
				
				// Set values of elements per recipe.
				recipeNameElement.setTextContent(r.getRecipeName());
				recipeVersionElement.setTextContent(r.getVersion() + "");
				recipeTypeElement.setTextContent(r.getType());
				recipeStyleElement.appendChild(this.getStyleChild(doc, r.getStyle()));
				recipeEquipmentElement.setTextContent("");
				recipeBrewerElement.setTextContent("");
				recipeBatchSizeElement.setTextContent(r.getBeerXmlStandardBatchSize() + "");
				recipeBoilSizeElement.setTextContent(r.getBeerXmlStandardBoilSize() + "");
				recipeBoilTimeElement.setTextContent(r.getBoilTime() + "");
				recipeEfficiencyElement.setTextContent(r.getEfficiency() + "");
				recipeNotesElement.setTextContent(r.getNotes());
				recipeOGElement.setTextContent(r.getOG() + "");
				recipeFGElement.setTextContent(r.getFG() + "");
				recipeStagesElement.setTextContent(r.getFermentationStages() + "");
				recipePrimaryAgeElement.setTextContent(r.getFermentationAge(Recipe.STAGE_PRIMARY) + "");
				recipeSecondaryAgeElement.setTextContent(r.getFermentationAge(Recipe.STAGE_SECONDARY) + "");
				recipeTertiaryAgeElement.setTextContent(r.getFermentationAge(Recipe.STAGE_TERTIARY) + "");
				recipeAgeElement.setTextContent(r.getBottleAge() + "");
				
				// Add elements to recipe.
				recipeElement.appendChild(recipeNameElement);
				recipeElement.appendChild(recipeVersionElement);
				recipeElement.appendChild(recipeTypeElement);
				recipeElement.appendChild(recipeStyleElement);
				recipeElement.appendChild(recipeEquipmentElement);
				recipeElement.appendChild(recipeBrewerElement);
				recipeElement.appendChild(recipeBatchSizeElement);
				recipeElement.appendChild(recipeBoilSizeElement);
				recipeElement.appendChild(recipeBoilTimeElement);
				recipeElement.appendChild(recipeEfficiencyElement);
				recipeElement.appendChild(this.getHopsChild(doc, r.getHopsList()));
				recipeElement.appendChild(this.getFermentablesChild(doc, r.getFermentablesList()));
				recipeElement.appendChild(this.getMiscsChild(doc, r.getMiscList()));
				recipeElement.appendChild(this.getYeastsChild(doc, r.getYeastsList()));
				recipeElement.appendChild(this.getWatersChild(doc, r.getWatersList()));
				recipeElement.appendChild(this.getMashChild(doc, r.getMashProfile()));
				recipeElement.appendChild(recipeNotesElement);
				recipeElement.appendChild(recipeOGElement);
				recipeElement.appendChild(recipeFGElement);
				recipeElement.appendChild(recipeStagesElement);
				recipeElement.appendChild(recipePrimaryAgeElement);
				recipeElement.appendChild(recipeSecondaryAgeElement);
				recipeElement.appendChild(recipeTertiaryAgeElement);
				recipeElement.appendChild(recipeAgeElement);
				
				// Add recipe to root <RECIPES> element.
				rootElement.appendChild(recipeElement);
			}
			 
			// Write to XML file.
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File file = getStoragePath("recipes.xml");
			StreamResult result = new StreamResult(file);
			Log.d("WriteXmlFile", "Outputing file to:" + file);
 
			transformer.transform(source, result);
		
		} catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} catch (TransformerConfigurationException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		} catch (TransformerException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates and returns a style document for the given BeerStyle object.
	 */
	public Element getStyleChild(Document d, BeerStyle s)
	{
		return d.createElement("STYLE");
	}
	
	public Element getHopsChild(Document d, ArrayList<Hop> l)
	{
		// Create the element.
		Element hopsElement = d.createElement("HOPS"); 
		
		for (Hop h : l)
		{
			Element hopElement = d.createElement("HOP");
			
			// Create fields of element
			Element nameElement = d.createElement("NAME");
			Element versionElement = d.createElement("VERSION");
			Element alphaElement = d.createElement("ALPHA");
			Element amountElement = d.createElement("AMOUNT");
			Element useElement = d.createElement("USE");
			Element timeElement = d.createElement("TIME");
			Element notesElement = d.createElement("NOTES");
			Element typeElement = d.createElement("TYPE");
			Element formElement = d.createElement("FORM");
			
			// Assign values
			nameElement.setTextContent(h.getName());
			versionElement.setTextContent(h.getVersion() + "");
			alphaElement.setTextContent(h.getAlphaAcidContent() + "");
			amountElement.setTextContent(h.getBeerXmlStandardAmount() + "");
			useElement.setTextContent(h.getUse());
			timeElement.setTextContent(h.getTime() + "");
			notesElement.setTextContent(h.getDescription());
			typeElement.setTextContent(h.getType());
			formElement.setTextContent(h.getForm());
			
			// Attach to element.
			hopElement.appendChild(nameElement);
			hopElement.appendChild(versionElement);
			hopElement.appendChild(alphaElement);
			hopElement.appendChild(amountElement);
			hopElement.appendChild(useElement);
			hopElement.appendChild(timeElement);
			hopElement.appendChild(notesElement);
			hopElement.appendChild(typeElement);
			hopElement.appendChild(formElement);
			
			// Attach to list of elements.
			hopsElement.appendChild(hopElement);
		}
		
		return hopsElement;
	}
	
	public Element getFermentablesChild(Document d, ArrayList<Fermentable> l)
	{
		// Create the element.
		Element fermentablesElement = d.createElement("FERMENTABLES"); 
		
		for (Fermentable f : l)
		{
			Element fermentableElement = d.createElement("FERMENTABLE");
			
			// Create fields of element
			Element nameElement = d.createElement("NAME");
			Element versionElement = d.createElement("VERSION");
			Element typeElement = d.createElement("TYPE");
			Element amountElement = d.createElement("AMOUNT");
			Element yieldElement = d.createElement("YIELD");
			Element colorElement = d.createElement("COLOR");
			Element addAfterBoilElement = d.createElement("ADD_AFTER_BOIL");
			
			// Assign values
			nameElement.setTextContent(f.getName());
			versionElement.setTextContent(f.getVersion() + "");
			typeElement.setTextContent(f.getType());
			amountElement.setTextContent(f.getBeerXmlStandardAmount() + "");
			yieldElement.setTextContent(f.getYield() + "");
			colorElement.setTextContent(f.getLovibondColor() + "");
			addAfterBoilElement.setTextContent(f.isAddAfterBoil() + "");
			
			// Attach to element.
			fermentableElement.appendChild(nameElement);
			fermentableElement.appendChild(versionElement);
			fermentableElement.appendChild(typeElement);
			fermentableElement.appendChild(amountElement);
			fermentableElement.appendChild(yieldElement);
			fermentableElement.appendChild(colorElement);
			fermentableElement.appendChild(addAfterBoilElement);
			
			// Attach to list of elements.
			fermentablesElement.appendChild(fermentableElement);
		}
		
		return fermentablesElement;
	}
	
	public Element getMiscsChild(Document d, ArrayList<Misc> l)
	{
		// Create the element.
		Element miscsElement = d.createElement("MISCS"); 
		
		for (Misc m : l)
		{
			Element hopElement = d.createElement("MISC");
			
			// Create fields of element
			Element nameElement = d.createElement("NAME");
			Element versionElement = d.createElement("VERSION");
			Element typeElement = d.createElement("TYPE");
			Element useElement = d.createElement("USE");
			Element amountElement = d.createElement("AMOUNT");
			Element amountIsWeight = d.createElement("AMOUNT_IS_WEIGHT");
			Element notesElement = d.createElement("NOTES");
			
			// Assign values
			nameElement.setTextContent(m.getName());
			versionElement.setTextContent(m.getVersion() + "");
			typeElement.setTextContent(m.getType());
			useElement.setTextContent(m.getUse());
			amountElement.setTextContent(m.getBeerXmlStandardAmount() + "");
			amountIsWeight.setTextContent(m.amountIsWeight() + "");
			notesElement.setTextContent(m.getShortDescription());
			
			// Attach to element.
			hopElement.appendChild(nameElement);
			hopElement.appendChild(versionElement);
			hopElement.appendChild(typeElement);
			hopElement.appendChild(useElement);
			hopElement.appendChild(amountElement);
			hopElement.appendChild(notesElement);
			hopElement.appendChild(notesElement);
			
			// Attach to list of elements.
			miscsElement.appendChild(hopElement);
		}
		
		return miscsElement;
	}
	
	public Element getYeastsChild(Document d, ArrayList<Yeast> l)
	{
		// Create the element.
		Element yeastsElement = d.createElement("YEASTS"); 
		
		for (Yeast y : l)
		{
			Element yeastElement = d.createElement("YEAST");
			
			// Create fields of element
			Element nameElement = d.createElement("NAME");
			Element versionElement = d.createElement("VERSION");
			Element typeElement = d.createElement("TYPE");
			Element formElement = d.createElement("FORM");
			Element amountElement = d.createElement("AMOUNT");
			Element laboratoryElement = d.createElement("LABORATORY");
			Element productIdElement = d.createElement("PRODUCT_ID");
			Element minTempElement = d.createElement("MIN_TEMPERATURE");
			Element maxTempElement = d.createElement("MAX_TEMPERATURE");
			Element attenuationElement = d.createElement("ATTENUATION");
			
			// Assign values
			nameElement.setTextContent(y.getName());
			versionElement.setTextContent(y.getVersion() + "");
			typeElement.setTextContent(y.getType());
			formElement.setTextContent(y.getForm());
			amountElement.setTextContent(y.getBeerXmlStandardAmount() + "");
			laboratoryElement.setTextContent(y.getLaboratory());
			productIdElement.setTextContent(y.getProductId());
			minTempElement.setTextContent(y.getBeerXmlStandardFermentationTemp() + "");
			maxTempElement.setTextContent(y.getBeerXmlStandardFermentationTemp() + "");
			attenuationElement.setTextContent(y.getAttenuation() + "");
			
			// Attach to element.
			yeastElement.appendChild(nameElement);
			yeastElement.appendChild(versionElement);
			yeastElement.appendChild(typeElement);
			yeastElement.appendChild(amountElement);
			yeastElement.appendChild(laboratoryElement);
			yeastElement.appendChild(productIdElement);
			yeastElement.appendChild(minTempElement);
			yeastElement.appendChild(maxTempElement);
			yeastElement.appendChild(attenuationElement);
			
			// Attach to list of elements.
			yeastsElement.appendChild(yeastElement);
		}
		
		return yeastsElement;
	}
	
	public Element getWatersChild(Document d, ArrayList<Water> l)
	{
		return d.createElement("WATERS");
	}
	
	public Element getMashChild(Document d, MashProfile m)
	{
		return d.createElement("MASH");
	}
	
	public File getStoragePath(String fileName) throws IOException {
	    // Get the directory for the app's private pictures directory. 
		File sdCard = Environment.getExternalStorageDirectory();  
		File filePath = new File(sdCard.getAbsolutePath() + "/BiermachtBrews");
		File file = new File(filePath.getAbsolutePath() + "/", fileName);
		if (!filePath.mkdirs())
		{
			Log.e("XmlWriter", "Cannot create directory.");
		}
		if (!file.exists())
		{
		    if (!file.createNewFile()) 
		    {
		        Log.e("XmlWriter", "File not created.");
		    }
		}
		else
		{
			Log.d("XmlWriter", "File already exists.");
		}
	    return file;
	}
}
