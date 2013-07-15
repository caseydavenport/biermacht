package com.biermacht.brews.recipe;

import java.util.ArrayList;
import com.biermacht.brews.utils.*;

public class MashProfile
{
	// Beer XML 1.0 Required Fields ===================================
	// ================================================================
	private String name;		            // profile name
	private Integer version;			    // XML Version -- 1
	private double grainTemp;               // Grain temp in C
	private ArrayList<MashStep> mashSteps;  // List of steps
	
	
	// Beer XML 1.0 Optional Fields ===================================
	// ================================================================
	private double tunTemp;		    // TUN Temperature in C
	private double spargeTemp;      // Sparge Temp in C
	private double pH;              // pH of water
	private double tunWeight;       // Weight of TUN in kG
	private double tunSpecificHeat; // Specific heat of TUN
	private String notes;			// Notes
	private Boolean equipAdj;       // Adjust for heating of equip?
	
	// Custom Fields ==================================================
	// ================================================================
	private long id;                  // id for use in database
	private long ownerId;			  // id for parent recipe

	// Static values =================================================
	// ===============================================================
	

    // Basic Constructor	
	public MashProfile() {
		this.setName("Unnamed Mash Profile");
		this.setVersion(1);
		this.setBeerXmlStandardGrainTemp(0);
		this.mashSteps = new ArrayList<MashStep>();
	    this.setBeerXmlStandardTunTemp(0);
		this.setBeerXmlStandardSpargeTemp(0);
		this.setpH(7);
		this.setBeerXmlStandardTunWeight(0);
		this.setBeerXmlStandardTunSpecHeat(0);
		this.setEquipmentAdjust(false);
		this.setNotes("");
		this.id = -1;
		this.ownerId = -1;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	@Override 
	public boolean equals(Object o)
	{
		if (!(o instanceof MashProfile))
			return false;
		
		MashProfile other = (MashProfile) o;
		if(this.hashCode() != other.hashCode())
			return false;
		return true;
	}
	
	@Override
	public int hashCode()
	{
		int hc = this.getName().hashCode();
		return hc;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	public void setVersion(Integer v)
	{
		this.version = v;
	}
	
	public Integer getVersion()
	{
		return this.version;
	}

	public void setBeerXmlStandardGrainTemp(double temp)
	{
		this.grainTemp = temp;
	}
	
	public void setDisplayGrainTemp(double temp)
	{
		this.grainTemp = Units.farenheitToCelsius(temp);
	}
	
	public double getBeerXmlStandardGrainTemp()
	{
		return this.grainTemp;
	}

	public double getDisplayGrainTemp()
	{
		return Units.celsiusToFarenheit(this.grainTemp);
	}
	
	public void setBeerXmlStandardTunTemp(double temp)
	{
		this.tunTemp = temp;
	}
	
	public void setDisplayTunTemp(double temp)
	{
		this.tunTemp = Units.farenheitToCelsius(temp);
	}
	
	public double getBeerXmlStandardTunTemp()
	{
		return this.tunTemp;
	}
	
	public double getDisplayTunTemp()
	{
		return Units.celsiusToFarenheit(this.tunTemp);
	}
	
	public void setBeerXmlStandardSpargeTemp(double temp)
	{
		this.spargeTemp = temp;
	}
	
	public void setDisplaySpargeTemp(double temp)
	{
		this.spargeTemp = Units.farenheitToCelsius(temp);
	}
	
	public double getBeerXmlStandardSpargeTemp()
	{
		return this.spargeTemp;
	}
	
	public double getDisplaySpargeTemp()
	{
		return Units.celsiusToFarenheit(this.spargeTemp);
	}

	public void setpH(double pH)
	{
		this.pH = pH;
	}
	
	public double getpH()
	{
		return this.pH;
	}

	public void setBeerXmlStandardTunWeight(double weight)
	{
		this.tunWeight = weight;
	}
	
	public void setDisplayTunWeight(double weight)
	{
		this.tunWeight = Units.poundsToKilos(weight);
	}
	
	public double getBeerXmlStandardTunWeight()
	{
		return this.tunWeight;
	}
	
	public double getDisplayTunWeight()
	{
		return Units.kilosToPounds(this.tunWeight);
	}

	public void setBeerXmlStandardTunSpecHeat(double heat)
	{
		this.tunSpecificHeat = heat;
	}
	
	public double getBeerXmlStandardTunSpecHeat()
	{
		// Cal / (g * C)
		return this.tunSpecificHeat;
	}

	public void setEquipmentAdjust(boolean adj)
	{
		this.equipAdj = adj;
	}
	
	public Boolean getEquipmentAdjust()
	{
		return this.equipAdj;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return this.id;
	}

	public void setOwnerId(long id)
	{
		this.ownerId = id;
	}

	public long getOwnerId()
	{
		return this.ownerId;
	}
	
	public void setNotes(String s)
	{
		this.notes = s;
	}
	
	public String getNotes()
	{
		return this.notes;
	}
	
	public ArrayList<MashStep> getMashStepList()
	{
		return this.mashSteps;
	}
	
	public void setMashStepList(ArrayList<MashStep> list)
	{
		this.mashSteps = list;
	}
	
	public void addMashStep(MashStep step)
	{
		this.mashSteps.add(step);
	}
}
