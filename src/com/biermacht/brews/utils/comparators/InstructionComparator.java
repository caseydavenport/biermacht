package com.biermacht.brews.utils.comparators;

import android.util.Log;

import com.biermacht.brews.recipe.Instruction;

import java.util.Comparator;

public class InstructionComparator<K> implements Comparator<K> {

  public int compare(K k1, K k2) {

    Instruction i1 = (Instruction) k1;
    Instruction i2 = (Instruction) k2;

    // Compare order number of instructions
    if (i1.getOrder() > i2.getOrder()) {
      return 1;
    }
    else if (i1.getOrder() < i2.getOrder()) {
      return - 1;
    }

    // If the instructions have the same order, wtf?
    // This should (hopefully) never be reached.
    Log.d("InstructionComparator", "Instruction orders are equal.  This should not happen");
    Log.d("InstructionComparator", "Instructions: " + i1.toString() + " " + i2.toString());
    return 1;
  }
}
