package com.biermacht.brews;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Instruction implements Parcelable {
	
	private String instructionText;

	public Instruction(String i)
	{
		this.setInstructionText(i);
	}
	
	public Instruction(Parcel p)
	{
		instructionText = p.readString();
	}

	public String getInstructionText() {
		return instructionText;
	}

	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
	}
	
	
	// Parcelable stuff
	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(instructionText);
	}
	
    public static Creator<Instruction> CREATOR = new Creator<Instruction>() {
        public Instruction createFromParcel(Parcel parcel) {
            return new Instruction(parcel);
        }

        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
