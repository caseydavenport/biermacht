package com.biermacht.brews.ingredient;
import android.os.Parcel;
import android.os.Parcelable;

import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

public class Misc extends Ingredient {
	
	// Beer XML 1.0 Required Fields (To be inherited) =================
	// ================================================================
	private String miscType;
	private String use;
	private boolean amountIsWeight;
	private String useFor;
	private String notes;
	
	// Custom Fields ==================================================
	// ================================================================
	private String displayUnits;
	
	// Static values =================================================
	// ===============================================================
	public static String TYPE_SPICE = "Spice";
	public static String TYPE_FINING = "Fining";
	public static String TYPE_WATER_AGENT = "Water Agent";
	public static String TYPE_HERB = "Herb";
	public static String TYPE_FLAVOR = "Flavor";
	public static String TYPE_OTHER = "Other";
	
	public Misc(String name)
	{
		super(name);
		setDisplayUnits("");
		setAmountIsWeight(false);
		setBeerXmlStandardAmount(0);
		setShortDescription("");
		this.amount = 0;
		setUse("");
		setVersion(1);
		setTime(0);
		setMiscType(TYPE_OTHER);
	}

    public Misc(Parcel p)
    {
        super(p);

        // Required
        this.miscType = p.readString();
        this.use = p.readString();
        this.amount = p.readDouble();
        this .amountIsWeight = p.readInt() > 0 ? true : false;
        this.useFor = p.readString();
        this.notes = p.readString();

        // Optional
        this.displayUnits = p.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int flags)
    {
        super.writeToParcel(p, flags);

        // Required
        p.writeString(this.miscType);
        p.writeString(this.use);
        p.writeDouble(this.amount);
        p.writeInt(amountIsWeight ? 1 : 0);
        p.writeString(this.useFor);
        p.writeString(this.notes);

        // Optional
        p.writeString(this.displayUnits);
    }

    public static final Parcelable.Creator<Misc> CREATOR =
            new Parcelable.Creator<Misc>() {
                @Override
                public Misc createFromParcel(Parcel p)
                {
                    return new Misc(p);
                }

                @Override
                public Misc[] newArray(int size) {
                    return null;
                }
            };
	
	@Override
	public String toString()
	{
        return this.getName();
	}

	public void setMiscType(String type)
	{
		this.miscType = type;
	}
	
	public String getMiscType()
	{
		if (!miscType.isEmpty() || this.miscType == null)
		    return this.miscType;
		else
			return Misc.TYPE_OTHER;
	}
	
	public void setUse(String u)
	{
		this.use = u;
	}
	
	public String getUse()
	{
		return this.use;
	}
	
	public void setAmountIsWeight(boolean b)
	{
		this.amountIsWeight = b;
	}
	
	public boolean amountIsWeight()
	{
		return this.amountIsWeight;
	}
	
	public void setUseFor(String s)
	{
		this.useFor = s;
	}
	
	public String getUseFor()
	{
		return this.useFor;
	}
	
	@Override
	public String getType() {
		return Ingredient.MISC;
	}

	@Override
	public String getShortDescription() {
		return notes;
	}

	@Override
	public double getDisplayAmount() {
		
		String unit = this.getDisplayUnits();
		
		if (unit.equalsIgnoreCase(Units.GALLONS))
			return Units.litersToGallons(this.amount);
		if (unit.equalsIgnoreCase(Units.GRAMS))
			return Units.kilosToGrams(this.amount);
        if (unit.equalsIgnoreCase(Units.KILOGRAMS))
            return this.amount;
        if (unit.equalsIgnoreCase(Units.LITERS))
            return this.amount;
        if (unit.equalsIgnoreCase(Units.MILLILITERS))
            return Units.litersToMillis(this.amount);
		if (unit.equalsIgnoreCase(Units.TEASPOONS))
			return Units.litersToTeaspoons(this.amount);
		if (unit.equalsIgnoreCase(Units.OUNCES))
			if (this.amountIsWeight())
			    return Units.kilosToOunces(this.amount);
			else
			    return Units.litersToOunces(this.amount);
		if (unit.equalsIgnoreCase(Units.POUNDS))
			return Units.kilosToPounds(this.amount);
		if (unit.equalsIgnoreCase(Units.CUP) || unit.equalsIgnoreCase(Units.CUPS))
			return Units.litersToCups(this.amount);
		if (unit.equalsIgnoreCase(Units.ITEMS))
			return this.amount;
		if (unit.equalsIgnoreCase(Units.PACKAGES))
			return this.amount;
		else
			return 0; // Should show that we couldn't compute
	}
	
	@Override
	public double getBeerXmlStandardAmount()
    {
		return this.amount;
	}

	@Override
	public void setDisplayAmount(double amt)
	{
		// Not implemented - must use other method.
		// Get punished! (Can't throw exceptions here, but to make debugging
		// easier lets cause a problem!)
		int x = (Integer) null;
		x = x / 2;
	}
	
	public void setDisplayAmount(double amt, String unit) 
	{

		if (unit.equalsIgnoreCase(Units.GALLONS))
			this.amount = Units.gallonsToLiters(amt);
		else if (unit.equalsIgnoreCase(Units.GRAMS))
			this.amount = Units.gramsToKilos(amt);
        else if (unit.equalsIgnoreCase(Units.KILOGRAMS))
            this.amount = amt;
        else if (unit.equalsIgnoreCase(Units.LITERS))
        {
            this.amount = amt;
            this.amountIsWeight = false;
        }
        else if (unit.equalsIgnoreCase(Units.MILLILITERS))
        {
            this.amount = Units.millisToLiters(amt);
            this.amountIsWeight = false;
        }
		else if (unit.equalsIgnoreCase(Units.TEASPOONS))
			this.amount = Units.teaspoonsToLiters(amt);
		else if (unit.equalsIgnoreCase(Units.OUNCES))
			if (this.amountIsWeight())
			    this.amount = Units.ouncesToKilos(amt);
			else
			    this.amount = Units.ouncesToLiters(amt);
		else if (unit.equalsIgnoreCase(Units.POUNDS))
			this.amount = Units.poundsToKilos(amt);
		else if (unit.equalsIgnoreCase(Units.CUP) || unit.equalsIgnoreCase(Units.CUPS))
			this.amount = Units.cupsToLiters(amt);
		else if (unit.equalsIgnoreCase(Units.ITEMS))
			this.amount = amt;
		else if (unit.equalsIgnoreCase(Units.PACKAGES))
			this.amount = amt;
		else
			this.amount = -1.0; // Should show that we couldn't compute
	}
	
	@Override
	public void setBeerXmlStandardAmount(double amt){
		this.amount = amt;
	}

    // This method is used when we want the value of the stored displayUnits
    // Not for use to determine the display units.
    public String getUnits()
    {
        return this.displayUnits;
    }

	@Override
	public String getDisplayUnits()
    {
        // If not, call the appropriate method based on which unit system the user has chosen
        if (Units.getUnitSystem().equals(Units.IMPERIAL))
            return getDisplayUnitsImperial();
        else
            return getDisplayUnitsMetric();
	}

    // Called when preferred units are metric
    private String getDisplayUnitsMetric()
    {
        // First check if we have units specified
        if (!this.displayUnits.equals(""))
            return Units.getMetricEquivalent(this.displayUnits, this.amountIsWeight);

        if (this.amountIsWeight())
            if (this.amount > 1)
                return Units.KILOGRAMS;
            else
                return Units.GRAMS;
        else
            if (this.amount > .5)
                return Units.LITERS;
            else
                return Units.MILLILITERS;
    }

    // Called when preferred units are imperial
    private String getDisplayUnitsImperial()
    {
        // First check if we have units specified
        if (!this.displayUnits.equals(""))
            return this.displayUnits;

        if (this.amountIsWeight())
            if (this.amount > .113)// .25lbs
                return Units.POUNDS;
            else
                return Units.OUNCES;
        else
            if (this.amount > .946) // .25gal
                return Units.GALLONS;
            else
                return Units.OUNCES;
    }

	@Override
	public String getBeerXmlStandardUnits()
	{
		if (this.amountIsWeight())
			return Units.KILOGRAMS;
		else
			return Units.LITERS;
	}
	
	@Override
	public int hashCode() {
        int hc = this.getName().hashCode();
        hc = hc ^ this.use.hashCode();
        hc = hc ^ this.miscType.hashCode();
        hc = hc + this.displayUnits.hashCode();
        return hc;
	}

	@Override
	public boolean equals(Object o) {
        if (o instanceof Misc)
            if (this.hashCode() == o.hashCode())
                return true;
        return false;
	}

	@Override
	public int getTime()
    {
		return time;
	}

    @Override
	public void setTime(int i) {
        this.time = i;
	}

	@Override
	public void setShortDescription(String description) {
		if (description.isEmpty())
			description = "No description provided";
		this.notes = description;
	}

	@Override
	public void setDisplayUnits(String units) {
		this.displayUnits = units;
	}
}
