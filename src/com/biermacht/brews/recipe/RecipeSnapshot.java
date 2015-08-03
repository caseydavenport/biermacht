package com.biermacht.brews.recipe;

import android.os.Parcel;

import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.InstructionGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecipeSnapshot extends Recipe {

  private long ownerId;                             // ID of the Recipe of which this is a snapshot.
  private String description;                       // Short description, e.g. "For Event X"

  // Public constructors
  public RecipeSnapshot(String s) {
    super(s);
    this.ownerId = -1;
    this.setDescription("");
  }

  public static RecipeSnapshot fromRecipe(Recipe r) {
    RecipeSnapshot s = new RecipeSnapshot(r.getRecipeName());
    // Set the owner ID to be the given recipe.
    s.setOwnerId(r.getId());

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
    s.setBrewDate(snapshotDate);

    return s;
  }

  public RecipeSnapshot(Parcel p) {
    super(p);
    this.ownerId = p.readLong();
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {
    super.writeToParcel(p, flags);
    p.writeLong(ownerId);
  }

  public void setOwnerId(long id) {
    this.ownerId = id;
  }

  public long getOwnerId() {
    return this.ownerId;
  }

  public void setDescription(String s) {
    this.description = s;
  }

  public String getDescription() {
    return this.description;
  }

}
