package com.biermacht.brews.recipe;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BrewNote implements Parcelable {

  private long snapshotId;                           // ID of snapshot.
  private String type;                               // Type of brew note.
  private String textNotes;                        // Short textNotes, e.g. "For Event X"
  private String date;                               // Date
  private String time;                               // Time of day that this snapshot was taken.
  private double gravity;
  private double temperature;

  public static String TYPE_GRAVITY_MEASUREMENT = "Gravity Mesurement";
  public static String TYPE_NOTE = "Text Note";

  public static final Parcelable.Creator<BrewNote> CREATOR =
          new Parcelable.Creator<BrewNote>() {
            @Override
            public BrewNote createFromParcel(Parcel p) {
              return new BrewNote(p);
            }

            @Override
            public BrewNote[] newArray(int size) {
              return new BrewNote[]{};
            }
          };

  // Public constructors
  public BrewNote(RecipeSnapshot s, String type) {
    super();
    this.snapshotId = s.getId();
    this.type = type;
    this.setTextNotes("New Measurement/Note");

    String date = new SimpleDateFormat("dd MMM yyyy").format(new Date());
    String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
    this.setTime(time);
    this.setDate(date);
  }

  public BrewNote(Parcel p) {
    this.snapshotId = p.readLong();
    this.textNotes = p.readString();
    this.date = p.readString();
    this.time = p.readString();
    this.gravity = p.readDouble();
    this.temperature = p.readDouble();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {
    p.writeLong(snapshotId);
    p.writeString(this.textNotes);
    p.writeString(this.date);
    p.writeString(this.time);
    p.writeDouble(this.gravity);
    p.writeDouble(this.temperature);
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDate() { return this.date; }

  public void setDate(String date) { this.date = date; }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  /**
   * Gets the full date and time - used for comparing notes when ordering
   * within a list.  Use getBrewDate() for the date on which this note occurred.
   * @return
   * @throws ParseException
   */
  public Date getDateAndTime() throws ParseException {
    String dateString = this.getDate() + " " + this.getTime();
    DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    Date result =  df.parse(dateString);
    return result;
  }

  public void setGravity(double g) {
    this.gravity = g;
  }

  public double getGravity() {
    return this.gravity;
  }

  public void setTemperature(double t) {
    this.temperature = t;
  }

  public double getTemperature() {
    return this.temperature;
  }

  public void setSnapshotId(long id) {
    this.snapshotId = id;
  }

  public long getSnapshotId() {
    return this.snapshotId;
  }

  public void setTextNotes(String s) {
    this.textNotes = s;
  }

  public String getTextNotes() {
    return this.textNotes;
  }

  /**
   * Saves to the database.
   */
  public void save() {
    Log.d("BrewNote::save()", "Saving brew note");
    //DatabaseAPI.updateSnapshot(this);
  }

}
