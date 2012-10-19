package com.biermacht.brews.utils.comparators;

import java.util.Comparator;

public class BeerStyleComparator<BeerStyle> implements Comparator<BeerStyle>
{

	public int compare(BeerStyle s1, BeerStyle s2) {
		return s1.toString().compareToIgnoreCase(s2.toString());
	}
	
}