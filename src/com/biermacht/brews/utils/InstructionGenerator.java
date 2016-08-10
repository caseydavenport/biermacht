package com.biermacht.brews.utils;

import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.comparators.InstructionComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class InstructionGenerator {

  // Holds all the instructions
  private ArrayList<Instruction> list;

  // Holds instructions of each type
  private ArrayList<Instruction> steepsList;
  private ArrayList<Instruction> boilsList;
  private ArrayList<Instruction> dryHopsList;
  private ArrayList<Instruction> yeastsList;
  private ArrayList<Instruction> mashesList;
  private ArrayList<Instruction> mashStepsList;
  private ArrayList<Instruction> bottlingList;

  private Recipe r;
  private Instruction inst;

  public InstructionGenerator(Recipe r) {
    this.list = new ArrayList<Instruction>();
    this.steepsList = new ArrayList<Instruction>();
    this.boilsList = new ArrayList<Instruction>();
    this.dryHopsList = new ArrayList<Instruction>();
    this.yeastsList = new ArrayList<Instruction>();
    this.mashesList = new ArrayList<Instruction>();
    this.mashStepsList = new ArrayList<Instruction>();
    this.bottlingList = new ArrayList<Instruction>();

    this.r = r;
    this.inst = new Instruction(r);
  }

  public void generate() {
    // Clear the lists
    this.list.removeAll(this.list);
    this.steepsList.removeAll(this.steepsList);
    this.boilsList.removeAll(this.boilsList);
    this.dryHopsList.removeAll(this.dryHopsList);
    this.yeastsList.removeAll(this.yeastsList);
    this.mashesList.removeAll(this.mashesList);
    this.mashStepsList.removeAll(this.mashStepsList);
    this.bottlingList.removeAll(this.bottlingList);

    // Generate instructions for each instruction type
    this.steeps();
    this.boils();
    this.dryHops();
    this.yeasts();
    this.mashSteps();
    this.bottling();

    // Configure the lists
    this.combineLists();

    // We run postBoils after all of this, because it depends
    // on items being in 'list'
    this.postBoils();

    // Set next task starting times.
    for (int i = 0; i < (list.size() - 1); i++) {
      if (! list.get(i).isLastInType()) {
        list.get(i).setNextDuration(list.get(i + 1).getDuration());
      }
    }

    // Sort based on order and then start time
    Collections.sort(list, new InstructionComparator<Instruction>());
  }

  public ArrayList<Instruction> getInstructions() {
    return list;
  }

  private void combineLists() {
    // Sort based on order and then start time
    Collections.sort(steepsList, new InstructionComparator<Instruction>());
    Collections.sort(boilsList, new InstructionComparator<Instruction>());
    Collections.sort(dryHopsList, new InstructionComparator<Instruction>());
    Collections.sort(yeastsList, new InstructionComparator<Instruction>());
    Collections.sort(mashesList, new InstructionComparator<Instruction>());
    Collections.sort(mashStepsList, new InstructionComparator<Instruction>());
    Collections.sort(bottlingList, new InstructionComparator<Instruction>());

    // Mark the end of each
    if (! steepsList.isEmpty()) {
      steepsList.get(steepsList.size() - 1).setLastInType(true);
    }

    if (! boilsList.isEmpty()) {
      boilsList.get(boilsList.size() - 1).setLastInType(true);
    }

    if (! dryHopsList.isEmpty()) {
      dryHopsList.get(dryHopsList.size() - 1).setLastInType(true);
    }

    if (! yeastsList.isEmpty()) {
      yeastsList.get(yeastsList.size() - 1).setLastInType(true);
    }

    if (! mashesList.isEmpty()) {
      mashesList.get(mashesList.size() - 1).setLastInType(true);
    }

    if (! mashStepsList.isEmpty()) {
      mashStepsList.get(mashStepsList.size() - 1).setLastInType(true);
    }

    if (! bottlingList.isEmpty()) {
      bottlingList.get(bottlingList.size() - 1).setLastInType(true);
    }

    // Add all the lists to the main list
    list.addAll(steepsList);
    list.addAll(boilsList);
    list.addAll(dryHopsList);
    list.addAll(yeastsList);
    list.addAll(mashesList);
    list.addAll(mashStepsList);
    if (mashStepsList.size() > 0) {
      list.addAll(getSpargeInstructions());
    }
    list.addAll(bottlingList);
  }

  private ArrayList<Instruction> getSpargeInstructions() {
    ArrayList<Instruction> list = new ArrayList<>();

    // If there are first wort hops, create a first wort instruction.
    if (r.getHops(Ingredient.USE_FIRST_WORT).size() != 0) {
      Instruction i = new Instruction(r);
      i.setRelevantIngredients(r.getHops(Ingredient.USE_FIRST_WORT));
      i.setInstructionType(Instruction.TYPE_SPARGE);
      i.setOrder(1);
      i.setDurationUnits(Units.HOURS);
      i.setLastInType(false);
      i.setInstructionText("Add First Wort Hops");
      i.setSubtitleFromIngredients();
      i.setDuration(0);  // Set duration to 0 so we don't show timer.
      list.add(i);
    }

    // Create the sparge instruction.
    Instruction i = new Instruction(r);
    i.setInstructionType(Instruction.TYPE_SPARGE);
    i.setOrder(1);
    i.setDurationUnits(Units.HOURS);
    i.setLastInType(true);

    if (r.getMashProfile().getSpargeType().equals(MashProfile.SPARGE_TYPE_BIAB)) {
      i.setInstructionText("Sparge - remove grains");
      i.setSubtitle("Top off to " + String.format("%2.2f", r.getDisplayBoilSize()) + Units.getVolumeUnits());
    }
    else {
      i.setInstructionText(r.getMashProfile().getSpargeType() + " sparge");
      i.setSubtitle("Until " + String.format("%2.2f", r.getDisplayBoilSize()) + Units.getVolumeUnits());
    }

    i.setDuration(0);  // Set duration to 0 so we don't show timer.
    list.add(i);

    return list;
  }

  /**
   * Generates steep instructions from the recipe
   */
  private void steeps() {
    // Do nothing if this is not an extract recipe
    if (r.getType().equals(Recipe.EXTRACT)) {
      HashMap<Integer, ArrayList<Ingredient>> ingredients = new HashMap<Integer, ArrayList<Ingredient>>();
      for (Fermentable f : r.getFermentablesList()) {
        // We build up a map with K = steep duration
        // and V = steeped grains at duration K
        if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN)) {
          if (! ingredients.containsKey(f.getTime())) {
            // Create the list for this duration
            ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();
            ingList.add(f);
            ingredients.put(f.getTime(), ingList);
          }
          else {
            // Create the list for this duration
            ArrayList<Ingredient> ingList = ingredients.get(f.getTime());
            ingList.add(f);
            ingredients.put(f.getTime(), ingList);
          }
        }
      }

      // Build up the steep instructions
      if (ingredients.size() > 0) {
        // for each k=steep_duration
        for (Integer time : ingredients.keySet()) {
          inst = new Instruction(r);
          inst.setRelevantIngredients(ingredients.get(time));
          inst.setInstructionType(Instruction.TYPE_STEEP);
          inst.setDuration(time);
          inst.setDurationUnits(Units.MINUTES);
          inst.setOrder(- 1 * time);                // Inversely proportional to time
          inst.setInstructionTextFromIngredients();
          inst.setSubtitle("@" + String.format("%2.0f", r.getDisplaySteepTemp()) + Units.getTemperatureUnits());
          steepsList.add(inst);
        }
      }
    }
  }

  /**
   * Generates boil instructions from the recipe
   */
  private void boils() {
    HashMap<Integer, ArrayList<Ingredient>> ingredients = new HashMap<Integer, ArrayList<Ingredient>>();
    ArrayList<Ingredient> potentialBoils = new ArrayList<Ingredient>();

    // Generate list of potentially boil-able ingredients
    potentialBoils.addAll(r.getHopsList());
    potentialBoils.addAll(r.getMiscList());
    potentialBoils.addAll(r.getFermentablesList());

    for (Ingredient i : potentialBoils) {
      if (i.getUse().equals(Ingredient.USE_BOIL)) {
        if (! ingredients.containsKey(i.getTime())) {
          // Create the list for this duration
          ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();
          ingList.add(i);
          ingredients.put(i.getTime(), ingList);
        }
        else {
          // Add to the existing list for this duration.
          ArrayList<Ingredient> ingList = ingredients.get(i.getTime());
          ingList.add(i);
          ingredients.put(i.getTime(), ingList);
        }
      }
    }

    if (ingredients.size() > 0) {
      // for each time=boil duration
      for (Integer time : ingredients.keySet()) {
        inst = new Instruction(r);
        inst.setRelevantIngredients(ingredients.get(time));
        inst.setInstructionType(Instruction.TYPE_BOIL);
        inst.setDuration(time);
        inst.setDurationUnits(Units.MINUTES);
        inst.setOrder(1 + r.getBoilTime() - time);
        inst.setInstructionTextFromIngredients();
        boilsList.add(inst);
      }

      // There is a special case where the boil lasts longer than the longest ingredient's
      // boil time.  For this, we must add a special step to indicate the start of the boil.
      if (Collections.max(ingredients.keySet()) < r.getBoilTime()) {
        inst = new Instruction(r);
        inst.setInstructionType(Instruction.TYPE_BOIL);
        inst.setDuration(r.getBoilTime());
        inst.setDurationUnits(Units.MINUTES);
        inst.setOrder(0);
        inst.setInstructionText("Begin boil");
        boilsList.add(inst);
      }
    }
  }

  /**
   * Generates dry hop instructions from the recipe
   */
  private void dryHops() {
    HashMap<Integer, ArrayList<Ingredient>> ingredients = new HashMap<Integer, ArrayList<Ingredient>>();
    for (Hop h : r.getHopsList()) {
      if (h.getUse().equals(Hop.USE_DRY_HOP)) {
        if (! ingredients.containsKey(h.getTime())) {
          // Create the list for this duration
          ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();
          ingList.add(h);
          ingredients.put(h.getTime(), ingList);
        }
        else {
          // Create the list for this duration
          ArrayList<Ingredient> ingList = ingredients.get(h.getTime());
          ingList.add(h);
          ingredients.put(h.getTime(), ingList);
        }
      }
    }

    if (ingredients.size() > 0) {
      // for each time=dry hop duration
      for (Integer time : ingredients.keySet()) {
        inst = new Instruction(r);
        inst.setRelevantIngredients(ingredients.get(time));
        inst.setInstructionType(Instruction.TYPE_DRY_HOP);
        inst.setDuration(time);
        inst.setDurationUnits(Units.DAYS);
        inst.setOrder(r.getBoilTime() - time);
        inst.setInstructionTextFromIngredients();
        dryHopsList.add(inst);
      }
    }
  }

  /**
   * Generates dryHop instructions from the recipe
   */
  private void bottling() {
    ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    for (Misc m : r.getMiscList()) {
      if (m.getUse().equals(Misc.USE_BOTTLING)) {
        ingredients.add(m);
      }
    }

    if (ingredients.size() > 0) {
      inst = new Instruction(r);
      inst.setRelevantIngredients(ingredients);
      inst.setInstructionType(Instruction.TYPE_BOTTLING);
      inst.setDurationUnits(Units.HOURS);
      inst.setInstructionTextFromIngredients();
      inst.setOrder(0);

      // Set duration to 0 so that we don't show a timer
      inst.setDuration(0);

      bottlingList.add(inst);
    }
  }

  /**
   * Generates yeast instructions from the recipe
   */
  private void yeasts() {
    ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    for (Yeast y : r.getYeastsList()) {
      ingredients.add(y);
    }

    if (ingredients.size() > 0) {
      inst = new Instruction(r);
      inst.setRelevantIngredients(ingredients);
      inst.setInstructionType(Instruction.TYPE_YEAST);
      inst.setInstructionTextFromIngredients();
      inst.setOrder(0);

      // Set duration to 0 so that we don't show a timer
      inst.setDuration(0);

      yeastsList.add(inst);
    }
  }

  /**
   * Generates misc instructions from the recipe
   */
  private void postBoils() {
    // Only generate these if there are already steps in the list.
    if (list.size() > 0) {
      // Check for USE_AROMA / whirlpool / flame out hops.
      ArrayList<Ingredient> aromaHops = r.getHops(Ingredient.USE_AROMA);
      ArrayList<Instruction> aromaInstructions = new ArrayList<>();
      if (aromaHops.size() != 0) {
        // There are aroma hops.  We need to sort them based on time.  Generate the mapping.
        HashMap<Integer, ArrayList<Ingredient>> hopMap = new HashMap<Integer, ArrayList<Ingredient>>();
        for (Ingredient i : aromaHops) {
          Hop h = (Hop) i;
          if (! hopMap.containsKey(h.getTime())) {
            // Create the list for this duration
            ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();
            ingList.add(h);
            hopMap.put(h.getTime(), ingList);
          }
          else {
            // Add hop to the existing list for this time.
            ArrayList<Ingredient> ingList = hopMap.get(h.getTime());
            ingList.add(h);
            hopMap.put(h.getTime(), ingList);
          }
        }

        // We need an instruction to indicate that the boil is over and that the temperature
        // should be adjusted to the desired steep temperature.
        inst = new Instruction(r);
        inst.setInstructionType(Instruction.TYPE_AROMA);
        inst.setOrder(0);
        inst.showInBrewTimer();
        inst.setInstructionText("Boil Complete");
        inst.setDurationUnits(Units.MINUTES);
        inst.setDuration(0);
        aromaInstructions.add(inst);

        for (Integer time : hopMap.keySet()) {
          // Generate instructions for each time slot.
          inst = new Instruction(r);
          inst.setInstructionType(Instruction.TYPE_AROMA);
          inst.setOrder(1 + Collections.max(hopMap.keySet()) - time);
          inst.setDurationUnits(Units.MINUTES);
          inst.setDuration(time);
          inst.setRelevantIngredients(hopMap.get(time));
          inst.setInstructionTextFromIngredients();
          aromaInstructions.add(inst);
        }

        // Set last in Aroma type and add to instruction list.
        Collections.sort(aromaInstructions, new InstructionComparator<Instruction>());
        aromaInstructions.get(aromaInstructions.size() - 1).setLastInType(true);
        list.addAll(aromaInstructions);
      }

      // Add a cool wort stage.
      inst = new Instruction(r);
      inst.setInstructionType(Instruction.TYPE_COOL);
      inst.setInstructionText("Cool wort to " + r.getDisplayCoolToFermentationTemp() + Units.getTemperatureUnits());
      inst.setOrder(3);
      inst.setDurationUnits(Units.HOURS);
      inst.setDuration(0); // Set to zero so we don't show a timer.
      list.add(inst);

      int numStages = r.getFermentationStages();
      if (r.getFermentationAge(Recipe.STAGE_PRIMARY) > 0 && (numStages > 0)) {
        inst = new Instruction(r);
        inst.setInstructionType(Instruction.TYPE_PRIMARY);
        inst.setInstructionText("Primary fermentation");
        inst.setDuration(r.getFermentationAge(Recipe.STAGE_PRIMARY));
        inst.setDurationUnits(Units.DAYS);
        inst.setOrder(5);
        list.add(inst);
      }

      if (r.getFermentationAge(Recipe.STAGE_SECONDARY) > 0 && (numStages > 1)) {
        inst = new Instruction(r);
        inst.setInstructionType(Instruction.TYPE_SECONDARY);
        inst.setInstructionText("Secondary fermentation");
        inst.setDuration(r.getFermentationAge(Recipe.STAGE_SECONDARY));
        inst.setDurationUnits(Units.DAYS);
        inst.setOrder(6);
        list.add(inst);
      }

      if (r.getFermentationAge(Recipe.STAGE_TERTIARY) > 0 && (numStages > 2)) {
        inst = new Instruction(r);
        inst.setInstructionType(Instruction.TYPE_TERTIARY);
        inst.setInstructionText("Tertiary fermentation");
        inst.setDuration(r.getFermentationAge(Recipe.STAGE_TERTIARY));
        inst.setDurationUnits(Units.DAYS);
        inst.setOrder(7);
        list.add(inst);
      }

      inst = new Instruction(r);
      inst.setInstructionType(Instruction.TYPE_CALENDAR);
      inst.setInstructionText("");
      inst.setOrder(0);
      inst.setDurationUnits(Units.HOURS);
      inst.setDuration(0); // Set to zero to indicate we shouldn't show timer.
      list.add(inst);
    }
  }

  /**
   * Generates mash step instructions from the recipe
   */
  private void mashSteps() {
    // Do nothing if this is an extract recipe
    if (! r.getType().equals(Recipe.EXTRACT)) {
      // Calculate relevant ingredients.
      ArrayList<Ingredient> relevantIngredients = new ArrayList<Ingredient>();
      for (Fermentable f : r.getFermentablesList()) {
        if (f.getUse().equals(Ingredient.USE_MASH)) {
          relevantIngredients.add(f);
        }
      }

      // Create the instructions.
      ArrayList<MashStep> mashSteps = r.getMashProfile().getMashStepList();
      if (mashSteps.size() > 0) {
        for (MashStep s : mashSteps) {
          inst = new Instruction(r);

          // Determine the correct text for this instruction.
          String text = s.getName();

          // Determine subtitle.
          String subtitle = "";

          // If an infuse step, add the infusion amount.
          if (s.getType().equals(MashStep.INFUSION)) {
            subtitle += "Infuse "
                    + String.format("%2.2f", s.getDisplayInfuseAmount()) + Units.getVolumeUnits()
                    + " @ "
                    + String.format("%2.0f", s.getDisplayInfuseTemp()) + Units.getTemperatureUnits();
          }
          else if (s.getType().equals(MashStep.DECOCTION)) {
            subtitle += "Decoct " + String.format("%2.2f", s.getDisplayDecoctAmount()) + Units.getVolumeUnits();
          }
          else if (s.getType().equals(MashStep.TEMPERATURE)) {
            subtitle += "Ramp over " + String.format("%2.0f", s.getRampTime()) + "m";
          }

          // Add the hold time and temperature.
          subtitle += "\nHold "
                  + String.format("%2.0f", s.getStepTime())
                  + "m @ "
                  + String.format("%2.0f", s.getDisplayStepTemp())
                  + Units.getTemperatureUnits();

          inst.setInstructionText(text);
          inst.setSubtitle(subtitle);
          inst.setInstructionType(Instruction.TYPE_MASH);
          inst.setDuration(s.getStepTime());
          inst.setOrder(s.getOrder());
          inst.setMashStep(s);
          inst.setLastInType(true);
          inst.setRelevantIngredients(relevantIngredients);
          mashStepsList.add(inst);
        }
      }
    }
  }
}
