package com.biermacht.brews.recipe;
import android.os.Parcel;
import android.os.Parcelable;

import com.biermacht.brews.utils.*;

public class MashStep implements Parcelable
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
    private String description;         // Description of step
    private double waterToGrainRatio;   // Water to grain ratio (L/kg)
	
	// Custom Fields ==================================================
	// ================================================================
	private long ownerId;				// id for parent mash profile
	private long id;                    // id for use in database
    private int order;                  // Order in step list
    private double infuseTemp;          // Temperature of infuse water

	// Static values =================================================
	// ===============================================================
    public static String INFUSION = "Infusion";
	public static String TEMPERATURE = "Temperature";
	public static String DECOCTION = "Decoction";
	
	// Basic Constructor
	public MashStep() {
		this.setName("Untitled Mash Step");
		this.setVersion(1);
		this.setType(MashStep.INFUSION);
		this.setDisplayInfuseAmount(10.0);
		this.setBeerXmlStandardStepTemp(0.0);
		this.setStepTime(60);
		this.setRampTime(0);
		this.setBeerXmlStandardEndTemp(0.0);
        this.setDescription("");
        this.setDisplayWaterToGrainRatio(1.25);
        this.infuseTemp = 0.0;
		this.id = -1;
		this.ownerId = -1;
        this.order = 1;
	}

    public MashStep(Parcel p)
    {
        // Beer XML 1.0 Required Fields ===================================
        // ================================================================
        name = p.readString();
        version = p.readInt();
        type = p.readString();
        infuseAmount = p.readDouble();
        stepTemp = p.readDouble();
        stepTime = p.readDouble();


        // Beer XML 1.0 Optional Fields ===================================
        // ================================================================
        rampTime = p.readDouble();
        endTemp = p.readDouble();
        description = p.readString();
        waterToGrainRatio = p.readDouble();

        // Custom Fields ==================================================
        // ================================================================
        ownerId = p.readLong();
        id = p.readLong();
        order = p.readInt();
    }

    @Override
    public void writeToParcel(Parcel p, int flags)
    {
        // Beer XML 1.0 Required Fields ===================================
        // ================================================================
        p.writeString(name);		            // profile name
        p.writeInt(version);			        // XML Version -- 1
        p.writeString(type);                    // infusion, temp, decoc
        p.writeDouble(infuseAmount);            // Amount
        p.writeDouble(stepTemp);				// Temp for this step in C
        p.writeDouble(stepTime);				// Time for this step


        // Beer XML 1.0 Optional Fields ===================================
        // ================================================================
        p.writeDouble(rampTime);		    // Time to ramp temp
        p.writeDouble(endTemp);             // Final temp for long steps
        p.writeString(description);         // Description of step
        p.writeDouble(waterToGrainRatio);   // Water to grain ratio (L/kg)

        // Custom Fields ==================================================
        // ================================================================
        p.writeLong(ownerId);				// id for parent mash profile
        p.writeLong(id);                    // id for use in database
        p.writeInt(order);                  // Order in step list
    }

    public static final Parcelable.Creator<MashStep> CREATOR =
            new Parcelable.Creator<MashStep>() {
                @Override
                public MashStep createFromParcel(Parcel p)
                {
                    return new MashStep(p);
                }

                @Override
                public MashStep[] newArray(int size) {
                    return new MashStep[size];
                }
            };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return this.name;
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

    public void setDisplayInfuseAmount(double amt)
    {
        this.infuseAmount = Units.gallonsToLiters(amt);
    }

    public double getDisplayInfuseAmount()
    {
        return Units.litersToGallons(this.infuseAmount);
    }

	public void setBeerXmlStandardInfuseAmount(double amt)
	{
		this.infuseAmount = amt;
	}
	
	public double getBeerXmlStandardInfuseAmount()
	{
		return this.infuseAmount;
	}

    public double getDisplayInfuseTemp()
    {
        return Units.celsiusToFarenheit(this.infuseTemp);
    }

    public void setDisplayInfuseTemp(double d)
    {
        this.infuseTemp = Units.farenheitToCelsius(d);
    }

    public void setDisplayStepTemp(double temp)
    {
        this.stepTemp = Units.farenheitToCelsius(temp);
    }

    public double getDisplayStepTemp()
    {
        return Units.celsiusToFarenheit(this.stepTemp);
    }

	public void setBeerXmlStandardStepTemp(double temp)
	{
		this.stepTemp = temp;
	}
	
	public double getBeerXmlStandardStepTemp()
	{
		return this.stepTemp;
	}

    public double getBeerXmlStandardWaterToGrainRatio()
    {
        return this.waterToGrainRatio;
    }

    public double getDisplayWaterToGrainRatio()
    {
        return Units.LPKGtoQPLB(this.waterToGrainRatio);
    }

    public void setBeerXmlStandardWaterToGrainRatio(double d)
    {
        this.waterToGrainRatio = d;
    }

    public void setDisplayWaterToGrainRatio(double d)
    {
        this.waterToGrainRatio = Units.QPLBtoLPKG(d);
    }

    public void setOrder(int i)
    {
        this.order = i;
    }

    public int getOrder()
    {
        return this.order;
    }

    public void setDescription(String s)
    {
        this.description = s;
    }

    public String getDescription()
    {
        return this.description;
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
