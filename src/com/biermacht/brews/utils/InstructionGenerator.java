package com.biermacht.brews.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import com.biermacht.brews.ingredient.Fermentable;
import com.biermacht.brews.ingredient.Hop;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.MashStep;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.comparators.InstructionComparator;

public class InstructionGenerator {
	
	private ArrayList<Instruction> list;
	private Recipe r;
	private Instruction inst;
	
	public InstructionGenerator(Recipe r)
	{
		this.list = new ArrayList<Instruction>();
		this.r = r;
		this.inst = new Instruction();
	}
	
	public void generate()
	{
		// Clear the list
		this.list.removeAll(this.list);
		
		// Generate instructions for each instruction type
		this.steeps();
		this.boils();
		this.dryHops();
		this.yeasts();
		this.miscs();
		this.mashes();
		this.mashSteps();
		this.bottling();
		
		// Sort based on order and then start time
		Collections.sort(list, new InstructionComparator<Instruction>());
	}
	
	public ArrayList<Instruction> getInstructions()
	{
		return list;
	}
	
	/**
	 * Generates steep instructions from the recipe
	 */
	private void steeps()
	{
		// Do nothing if this is not an extract recipe
		if (r.getType().equals(Recipe.EXTRACT))
		{
			HashMap<Integer,  ArrayList<Ingredient>> ingredients = new HashMap<Integer, ArrayList<Ingredient>>();
			for(Fermentable f : r.getFermentablesList())
			{
				// We build up a map with K = steep duration
				// and V = steeped grains at duration K
				if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN))
				{
					if (!ingredients.containsKey(f.getTime()))
					{
						// Create the list for this duration
						ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();
						ingList.add(f);
						ingredients.put(f.getTime(), ingList);
					}
					else
					{
						// Create the list for this duration
						ArrayList<Ingredient> ingList = ingredients.get(f.getTime());
						ingList.add(f);
						ingredients.put(f.getTime(), ingList);
					}
				}
			}
			
			// Build up the steep instructions
			if (ingredients.size() > 0)
			{
				// for each k=steep_duration
				for (Integer time : ingredients.keySet())
				{
					inst = new Instruction();
					inst.setRelevantIngredients(ingredients.get(time));
					inst.setInstructionType(Instruction.TYPE_STEEP);
					inst.setDuration(time);
					inst.setDurationUnits(Units.MINUTES);
					inst.setOrder(0); // Steep instructions go first
					inst.setInstructionTextFromIngredients();
					list.add(inst);
				}
			}
		}
	}
	
	/**
	 * Generates boil instructions from the recipe
	 */
	private void boils()
	{
		HashMap<Integer,  ArrayList<Ingredient>> ingredients = new HashMap<Integer, ArrayList<Ingredient>>();
		ArrayList<Ingredient> potentialBoils = new ArrayList<Ingredient>();
		
		// Generate list of potentially boil-able ingredients
		potentialBoils.addAll(r.getHopsList());
		potentialBoils.addAll(r.getMiscList());
		potentialBoils.addAll(r.getFermentablesList());
		
		for(Ingredient i : potentialBoils)
		{
			if (i.getUse().equals(Ingredient.USE_BOIL))
			{
				if (!ingredients.containsKey(i.getTime()))
				{
					// Create the list for this duration
					ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();
					ingList.add(i);
					ingredients.put(i.getTime(), ingList);
				}
				else
				{
					// Create the list for this duration
					ArrayList<Ingredient> ingList = ingredients.get(i.getTime());
					ingList.add(i);
					ingredients.put(i.getTime(), ingList);
				}
			}
		}
		
		if (ingredients.size() > 0)
		{
			// for each time=boil duration
			for (Integer time : ingredients.keySet())
			{
				inst = new Instruction();
				inst.setRelevantIngredients(ingredients.get(time));
				inst.setInstructionType(Instruction.TYPE_BOIL);
				inst.setDuration(time);
				inst.setDurationUnits(Units.MINUTES);
				inst.setOrder(r.getBoilTime()-time);
				inst.setInstructionTextFromIngredients();
				list.add(inst);
			}
		}
	}

	/**
	 * Generates dryHop instructions from the recipe
	 */
	private void dryHops()
	{
		HashMap<Integer,  ArrayList<Ingredient>> ingredients = new HashMap<Integer, ArrayList<Ingredient>>();
		for(Hop h : r.getHopsList())
		{
			if (h.getUse().equals(Hop.USE_DRY_HOP))
			{
				if (!ingredients.containsKey(h.getTime()))
				{
					// Create the list for this duration
					ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();
					ingList.add(h);
					ingredients.put(h.getTime(), ingList);
				}
				else
				{
					// Create the list for this duration
					ArrayList<Ingredient> ingList = ingredients.get(h.getTime());
					ingList.add(h);
					ingredients.put(h.getTime(), ingList);
				}
			}
		}
		
		if (ingredients.size() > 0)
		{
			// for each time=dry hop duration
			for (Integer time : ingredients.keySet())
			{
				inst = new Instruction();
				inst.setRelevantIngredients(ingredients.get(time));
				inst.setInstructionType(Instruction.TYPE_DRY_HOP);
				inst.setDuration(time);
				inst.setDurationUnits(Units.DAYS);
				inst.setOrder(r.getBoilTime()-time);
				inst.setInstructionTextFromIngredients();
				list.add(inst);
			}
		}
	}
	
	/**
	 * Generates dryHop instructions from the recipe
	 */
	private void bottling()
	{
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		for(Misc m : r.getMiscList())
		{
			if (m.getUse().equals(Misc.USE_BOTTLING))
			{	
				ingredients.add(m);
			}
		}
		
		if (ingredients.size() > 0)
		{
			inst = new Instruction();
			inst.setRelevantIngredients(ingredients);
			inst.setInstructionType(Instruction.TYPE_BOTTLING);
			inst.setDuration(1);
			inst.setDurationUnits(Units.HOURS);
			inst.setInstructionTextFromIngredients();
			inst.setOrder(0);
			list.add(inst);
		}
	}

	/**
	 * Generates yeast instructions from the recipe
	 */
	private void yeasts()
	{
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		for (Yeast y : r.getYeastsList())
		{
			ingredients.add(y);
		}
		
		if (ingredients.size() > 0)
		{
			inst = new Instruction();
			inst.setRelevantIngredients(ingredients);
			inst.setInstructionType(Instruction.TYPE_YEAST);
			inst.setDuration(1);
			inst.setInstructionTextFromIngredients();
			inst.setOrder(0);
			list.add(inst);
		}
	}
	
	/**
	 * Generates misc instructions from the recipe
	 */
	private void miscs()
	{
		if (list.size() > 0)
		{
			// Add a cool wort stage
			inst = new Instruction();
			inst.setInstructionType(Instruction.TYPE_COOL);
			inst.setInstructionText("Cool wort to " + r.getDisplayCoolToFermentationTemp() + "F");
			inst.setDuration(2);
			inst.setOrder(3);
			inst.setDurationUnits(Units.HOURS);
			list.add(inst);
		}

		if (list.size() > 0)
		{
			inst = new Instruction();
			inst.setInstructionType(Instruction.TYPE_PRIMARY);
			inst.setInstructionText("Ferment until FG= " + r.getFG());
			inst.setDuration(5);
			inst.setDurationUnits(Units.DAYS);
			inst.setOrder(5);
			list.add(inst);
		}	
	}
	
	/**
	 * Generates mash step instructions from the recipe
	 */
	private void mashSteps()
	{
		// Do nothing if this is an extract recipe
		if (!r.getType().equals(Recipe.EXTRACT))
		{
			ArrayList<MashStep> mashSteps = r.getMashProfile().getMashStepList();
			if (mashSteps.size() > 0)
			{
				// for each time=steep_duration
				for (MashStep s : mashSteps)
				{
					if (s.getRampTime() != 0)
					{
						inst = new Instruction();
						String temp = String.format("%2.0f", s.getDisplayStepTemp());
						String text = "Ramp temperature to " + temp + "F";
						inst.setInstructionText(text);
						inst.setInstructionType(Instruction.TYPE_MASH);
						inst.setDuration(s.getRampTime());
						inst.setOrder(0);
						list.add(inst);
					}
					
					inst = new Instruction();
					inst.setInstructionText(s.getName());
					inst.setInstructionType(Instruction.TYPE_MASH);
					inst.setDuration(s.getStepTime());
					inst.setOrder(0);
					list.add(inst);
				}
			}
		}
	}
	
	/**
	 * Generates mashed grain instructions from the recipe
	 */
	private void mashes()
	{
		// Do nothing if this is an extract recipe
		if (!r.getType().equals(Recipe.EXTRACT))
		{
			HashMap<Integer, String> mashes = new HashMap<Integer, String>();
			for(Fermentable f : r.getFermentablesList())
			{
				// We build up a map with K = steep duration
				// and V = string of steeped grains at duration K
				if (f.getFermentableType().equals(Fermentable.TYPE_GRAIN))
				{
					if (!mashes.containsKey(f.getTime()))
					{
						// Add a new entry for that duration
						mashes.put(f.getTime(), f.getName());	
					}
					else
					{
						// Append to existing duration
						String s = mashes.get(f.getTime());
						s += "\n";
						s += f.getName();
						mashes.put(f.getTime(), s);
					}
	
				}
			}
			
			if (mashes.size() > 0)
			{
				for (Integer time : mashes.keySet())
				{
					inst = new Instruction();
					inst.setInstructionText(mashes.get(time));
					inst.setInstructionType(Instruction.TYPE_MASH);
					inst.setDuration(0); // TODO
					inst.setOrder(0);
					list.add(inst);
				}
			}
		}
	}
}
