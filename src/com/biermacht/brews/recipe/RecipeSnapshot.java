package com.biermacht.brews.recipe;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.InstructionGenerator;
import com.biermacht.brews.utils.comparators.BrewNoteComparator;
import com.biermacht.brews.utils.comparators.RecipeSnapshotComparator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class RecipeSnapshot extends Recipe {

  private long recipeId;                             // ID of the Recipe of which this is a snapshot.
  private String description;                        // Short description, e.g. "For Event X"
  private String snapshotTime;                       // Time of day that this snapshot was taken.
  private ArrayList<BrewNote> notes;                 // List of notes.

  public static final Parcelable.Creator<Recipe> CREATOR =
          new Parcelable.Creator<Recipe>() {
            @Override
            public RecipeSnapshot createFromParcel(Parcel p) {
              return new RecipeSnapshot(p);
            }

            @Override
            public RecipeSnapshot[] newArray(int size) {
              return new RecipeSnapshot[]{};
            }
          };

  // Public constructors
  public RecipeSnapshot(String s) {
    super(s);
    this.recipeId = -1;
    this.setDescription("Brew Day Snapshot");
    this.setSnapshotTime("00:00:00");
    this.notes = new ArrayList<BrewNote>();
  }

  public static RecipeSnapshot fromRecipe(Recipe r) {
    RecipeSnapshot s = new RecipeSnapshot(r.getRecipeName());
    // Set the owner ID to be the given recipe.
    s.setRecipeId(r.getId());

    // Set all the other fields.
    s.setVersion(r.getVersion());
    s.setType(r.getType());
    s.setStyle(r.getStyle());
    s.setBrewer(r.getBrewer());
    s.setBeerXmlStandardBatchSize(r.getBeerXmlStandardBatchSize());
    s.setBeerXmlStandardBoilSize(r.getBeerXmlStandardBoilSize());
    s.setBoilTime(r.getBoilTime());
    s.setEfficiency(r.getEfficiency()); //TODO

    // Add all ingredients. TODO: Why is this the API?
    for (Ingredient i : r.getIngredientList()) {
      s.addIngredient(i);
    }

    s.setMashProfile(r.getMashProfile());
    s.setOG(r.getOG());
    s.setFG(r.getFG());
    s.setFermentationStages(r.getFermentationStages());
    s.setFermentationAge(Recipe.STAGE_PRIMARY, r.getFermentationAge(Recipe.STAGE_PRIMARY));
    s.setFermentationAge(Recipe.STAGE_SECONDARY, r.getFermentationAge(Recipe.STAGE_SECONDARY));
    s.setFermentationAge(Recipe.STAGE_TERTIARY, r.getFermentationAge(Recipe.STAGE_TERTIARY));
    s.setBeerXmlStandardFermentationTemp(Recipe.STAGE_PRIMARY, r.getBeerXmlStandardFermentationTemp(Recipe.STAGE_PRIMARY));
    s.setBeerXmlStandardFermentationTemp(Recipe.STAGE_SECONDARY, r.getBeerXmlStandardFermentationTemp(Recipe.STAGE_SECONDARY));
    s.setBeerXmlStandardFermentationTemp(Recipe.STAGE_TERTIARY, r.getBeerXmlStandardFermentationTemp(Recipe.STAGE_TERTIARY));
    s.setBottleAge(r.getBottleAge());
    s.setBeerXmlStandardBottleTemp(r.getBeerXmlStandardBottleTemp());
    s.setIsForceCarbonated(r.isForceCarbonated());
    s.setCarbonation(r.getCarbonation());
    s.setPrimingSugarEquiv(r.getPrimingSugarEquiv());
    s.setKegPrimingFactor(r.getKegPrimingFactor());
    s.setBeerXmlStandardCarbonationTemp(r.getBeerXmlStandardCarbonationTemp());
    s.setCalories(r.getCalories());
    s.setABV(r.getABV());
    s.setBitterness(r.getBitterness());
    s.setColor(r.getColor());
    s.instructionGenerator = new InstructionGenerator(s);
    s.setDisplaySteepTemp(r.getDisplaySteepTemp());
    s.setCalculateBoilVolume(r.getCalculateBoilVolume());
    s.setCalculateStrikeTemp(r.getCalculateStrikeTemp());
    s.setCalculateStrikeVolume(r.getCalculateStrikeVolume());
    s.setBatchTime(r.getBatchTime());

    // The following fields should always be zeroed for new RecipeSnapshots.
    s.setMeasuredOG(0);
    s.setMeasuredFG(0);
    s.setBeerXmlMeasuredBatchSize(0);
    s.setTasteNotes("");
    s.setTasteRating(0);
    s.setNotes("");

    // Set the date based on the current date.
    String snapshotDate = new SimpleDateFormat("dd MMM yyyy").format(new Date());
    String snapshotTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
    s.setBrewDate(snapshotDate);
    s.setSnapshotTime(snapshotTime);

    return s;
  }

  public RecipeSnapshot(Parcel p) {
    super(p);
    this.recipeId = p.readLong();
    this.description = p.readString();
    this.snapshotTime = p.readString();

    this.notes = new ArrayList<BrewNote>();
    p.readTypedList(this.notes, BrewNote.CREATOR);
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {
    super.writeToParcel(p, flags);
    p.writeLong(recipeId);
    p.writeString(this.description);
    p.writeString(this.snapshotTime);
    p.writeTypedList(this.notes);
  }

  public String getSnapshotTime() {
    return snapshotTime;
  }

  public void setSnapshotTime(String snapshotTime) {
    this.snapshotTime = snapshotTime;
  }

  /**
   * Gets the full date and time - used for comparing snapshots when ordering
   * within a list.  Use getBrewDate() for the date on which this snapshot occurred.
   * @return
   * @throws ParseException
   */
  public Date getSnapshotDate() throws ParseException {
    String dateString = this.getBrewDate() + " " + this.getSnapshotTime();
    DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    Date result =  df.parse(dateString);
    return result;
  }

  public void setRecipeId(long id) {
    this.recipeId = id;
  }

  public long getRecipeId() {
    return this.recipeId;
  }

  public void setDescription(String s) {
    this.description = s;
  }

  public String getDescription() {
    return this.description;
  }

  public ArrayList<BrewNote> getBrewNotes() {
    return this.notes;
  }
  /**
   * Adds a BrewNote and sorts the list.
   */
  public void addNote(BrewNote n) {
    // Add note.
    this.notes.add(0, n);

    // Sort list.
    Collections.sort(this.notes, new BrewNoteComparator<BrewNote>());
  }

  /**
   * Saves this RecipeSnapshot to the database.
   */
  @Override
  public void save(Context c) {
    Log.d("Snapshot::save()", "Saving " + this.getRecipeName() + " snapshot with id: " + this.getId());
    new DatabaseAPI(c).updateSnapshot(this);
  }

}
