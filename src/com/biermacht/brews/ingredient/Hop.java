package com.biermacht.brews.ingredient;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;

import java.util.ArrayList;

public class Hop extends Ingredient {
	
	// Beer XML 1.0 Required Fields ===================================
	// ================================================================
	
	// Name - Inherited
	// Version - Inherited
	private double alpha;                       // Alpha in %
	private String use;                         // Boil, Dry Hop, etc
	
	// Beer XML 1.0 Optional Fields ===================================
	// ================================================================
	private String type;                        // Bittering, Armoa, Both
	private String form;                        // Pellet, plug, whole
	private String origin;                      // Place of origin
	private ArrayList<String> substitutes;      // Substitute options
	
	// Custom Fields ==================================================
	// ================================================================
	private String description;                 // Short description of flavor / use
	
	// Static values =================================================
	// ===============================================================
	public static final String FORM_PELLET = "Pellet";
	public static final String FORM_WHOLE = "Whole";
	public static final String FORM_PLUG = "Plug";
	
	// Hop types
	public static final String TYPE_BITTERING = "Bittering";
	public static final String TYPE_AROMA = "Aromatic";
	public static final String TYPE_BOTH = "Bittering / Aromatic";

	public Hop(String name)
	{
		super(name);
		this.amount = 0;
		this.alpha = 0;
		this.use = USE_BOIL;
		this.type = TYPE_BOTH;
		this.form = FORM_PELLET;
		this.origin = "";
		this.substitutes = new ArrayList<String>(); // TODO Get this from somewhere
		this.description = "No description";
	}

    public Hop(Parcel p)
    {
        super(p);
        this.substitutes = new ArrayList<String>();
        alpha = p.readDouble();
        use = p.readString();

        type = p.readString();
        form = p.readString();
        origin = p.readString();
        p.readStringList(this.substitutes);
        description = p.readString();
    }

    @Override
    public void writeToParcel(Parcel p, int flags)
    {
        super.writeToParcel(p, flags);

        // Required
        p.writeDouble(alpha);
        p.writeString(use);

        // Optional
        p.writeString(type);
        p.writeString(form);
        p.writeString(origin);
        p.writeStringList(substitutes);
        p.writeString(description);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Parcelable.Creator<Hop> CREATOR =
            new Parcelable.Creator<Hop>() {
                @Override
                public Hop createFromParcel(Parcel p)
                {
                    return new Hop(p);
                }

                @Override
                public Hop[] newArray(int size) {
                    return null;
                }
            };

	@Override
	public String getType() {
		return Ingredient.HOP;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAlphaAcidContent() {
		return alpha;
	}

	public void setAlphaAcidContent(double alpha) {
		this.alpha = alpha;
	}

	@Override
	public String getShortDescription() {
		return this.description;
	}

	@Override
	public double getDisplayAmount()
    {
        if (getDisplayUnits().equals(Units.OUNCES))
		    return Units.kilosToOunces(this.amount);
        else
            return Units.kilosToGrams(this.amount);
	}
	
	public int getDisplayTime()
	{
		if (this.getUse().equals(Hop.USE_DRY_HOP))
			return (int) Units.minutesToDays(this.time);
		else
			return this.time;
	}
	
	public void setDisplayTime(int time)
	{
		if (this.getUse().equals(Hop.USE_DRY_HOP))
			this.time = (int) Units.daysToMinutes(time);
		else
			this.time = time;
	}
	
	@Override 
	public double getBeerXmlStandardAmount() {
		return this.amount;
	}

	@Override
	public void setDisplayAmount(double amt)
    {
        if (getDisplayUnits().equals(Units.OUNCES))
		    this.amount = Units.ouncesToKilos(amt);
        else
            this.amount = Units.gramsToKilos(amt);
	}
	
	@Override
	public void setBeerXmlStandardAmount(double amt) {
		this.amount = amt;
	}

	public String getForm() {
		return form;
	}
	
	public void setForm(String form) {
		this.form = form;
	}

	@Override
	public String getDisplayUnits()
    {
        return Units.getHopUnits();
	}
	
	public void setDisplayUnits(String s) {
	}
	
	@Override
	public String getBeerXmlStandardUnits() {
		return Units.KILOGRAMS;
	}

	@Override
	public int hashCode() {
        int hc = this.getName().hashCode();
        hc = hc ^ (int) (getAlphaAcidContent() * 1234);
        return hc;
	}

	@Override
	public boolean equals(Object o) {
        if (o instanceof Hop)
            if (this.hashCode() == o.hashCode())
                return true;
        return false;
	}

	/**
	 * @return the use
	 */
	public String getUse() {
		return use;
	}

	/**
	 * @param use the use to set
	 */
	public void setUse(String use) {
		this.use = use;
	}

	/**
	 * @return the time
	 */
	public int getBeerXmlStandardTime() {
		return time;
	}

	@Override 
	public int getTime()
	{
		return this.getDisplayTime();
	}

    @Override
    public void setTime(int time)
    {
        setDisplayTime(time);
    }
	
	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the substitutes
	 */
	public ArrayList<String> getSubstitutes() {
		return substitutes;
	}

	/**
	 * @param substitutes the substitutes to set
	 */
	public void setSubstitutes(ArrayList<String> substitutes)
    {
		this.substitutes = substitutes;
	}

	/**
	 * @return the hopType
	 */
	public String getHopType() {
		return type;
	}

	/**
	 * @param type the hopType to set
	 */
	public void setHopType(String type) {
		this.type = type;
	}

	/**
	 * @param time the time to set
	 */
	public void setBeerXmlStandardTime(int time) {
		this.time = time;
	}

	@Override
	public void setShortDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int compareTo(Ingredient other)
	{
		/**
		 * Returns:
		 * 		0 if argument is equal to this
		 * 		< 0 if argument is greater than this
		 * 		> 0 if argument is less than this
		 */
		// If not the same type, sort based on type.
		if (!this.getType().equals(other.getType()))
			return this.getType().compareTo(other.getType());
		
		// If they are not the same use, sort based on use.
		if (!this.getUse().equals(other.getUse()))
			return this.getUse().compareTo(other.getUse()); 
		
		// If they are the same, sort based on time.
		return this.getTime() > other.getTime() ? -1 : 1;
	}
}
