package com.biermacht.brews.utils.comparators;

import java.util.Comparator;
import com.biermacht.brews.recipe.Instruction;

public class InstructionComparator<K> implements Comparator<K>
{

	public int compare(K k1, K k2) {
		
		Instruction i1 = (Instruction) k1;
		Instruction i2 = (Instruction) k2;
		
		if (i1.getOrder() > i2.getOrder())
			return 1;
		else if (i1.getOrder() < i2.getOrder())
			return -1;
		else if(i1.getOrder() == i2.getOrder())
		{
			if (i1.getStartTime() > i2.getStartTime())
				return 1;
			else if (i1.getStartTime() < i2.getStartTime())
				return -1;
			else
				return 0;
		}
		
		return 1; // This should (hopefully) never be reached.
	}
}
