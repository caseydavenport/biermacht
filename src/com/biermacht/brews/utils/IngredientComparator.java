package com.biermacht.brews.utils;

import java.util.Comparator;

public class IngredientComparator<Ingredient> implements Comparator<Ingredient>
{

	public int compare(Ingredient s1, Ingredient s2) {
		return s1.toString().compareToIgnoreCase(s2.toString());
	}
	
}
