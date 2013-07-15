package com.biermacht.brews.recipe;
import com.biermacht.brews.utils.*;

public class MashStep
{
    // Beer XML 1.0 Required Fields ===================================
	// ================================================================
	private String name;		            // profile name
	private int version;			        // XML Version -- 1
	private String type;                    // infusion, temp, decoc
	private double infuseAmount;            // Amount
	private double stepTemp;				// Temp for this step in C
	private double stepTime;				// Time for this step


	// Beer XML 1.0 Optional Fields ===================================
	// ================================================================
	private double rampTime;		    // Time to ramp temp
	private double endTemp;             // Final temp for long steps
	
	// Custom Fields ==================================================
	// ================================================================
	private long ownerId;				// id for parent mash profile
	private long id;                    // id for use in database

	// Static values =================================================
	// ===============================================================
    public static String INFUSION = "infusion";
	public static String TEMPERATURE = "temperature";
	public static String DECOCTION = "decoction";
	
	// Basic Constructor
	public MashStep() {
		this.setName("Untitled Mash Step");
		this.setVersion(1);
		this.setType(MashStep.INFUSION);
		this.setBeerXmlStandardInfuseAmount(2.0);
		this.setBeerXmlStandardStepTemp(0.0);
		this.setStepTime(60);
		this.setRampTime(0);
		this.setBeerXmlStandardEndTemp(0.0);
		this.id = -1;
		this.ownerId = -1;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}
	
	public int getVersion()
	{
		return this.version;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getType()
	{
		return this.type;
	}

	public void setBeerXmlStandardInfuseAmount(double amt)
	{
		this.infuseAmount = amt;
	}
	
	public double getBeerXmlStandardInfuseAmount()
	{
		return this.infuseAmount;
	}

	public void setBeerXmlStandardStepTemp(double temp)
	{
		this.stepTemp = temp;
	}
	
	public double getBeerXmlStandardStepTemp()
	{
		return this.stepTemp;
	}
	
	public double getDisplayStepTemp()
	{
		return Units.celsiusToFarenheit(this.stepTemp);
	}

	public void setStepTime(double time)
	{
		this.stepTime = time;
	}
	
	public double getStepTime()
	{
		return this.stepTime;
	}

	public void setRampTime(double time)
	{
		this.rampTime = time;
	}
	
	public double getRampTime()
	{
		return this.rampTime;
	}

	public void setBeerXmlStandardEndTemp(double temp)
	{
		this.endTemp = temp;
	}
	
	public double getBeerXmlStandardEndTemp()
	{
		return this.endTemp;
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
}
