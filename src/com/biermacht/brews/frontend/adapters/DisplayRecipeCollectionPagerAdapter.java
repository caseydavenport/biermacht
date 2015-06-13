package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.biermacht.brews.frontend.fragments.DetailsViewFragment;
import com.biermacht.brews.frontend.fragments.IngredientViewFragment;
import com.biermacht.brews.frontend.fragments.InstructionViewFragment;
import com.biermacht.brews.frontend.fragments.ProfileViewFragment;
import com.biermacht.brews.recipe.Recipe;

public class DisplayRecipeCollectionPagerAdapter extends FragmentStatePagerAdapter {
  Recipe r;
  Context c;
  IngredientViewFragment ingVf;
  InstructionViewFragment insVf;
  DetailsViewFragment detVf;
  ProfileViewFragment proVf;

  public DisplayRecipeCollectionPagerAdapter(FragmentManager fm, Recipe r, Context c) {
    super(fm);
    this.r = r;
    this.c = c;
    this.ingVf = IngredientViewFragment.instance(r);
    this.insVf = InstructionViewFragment.instance(r);
    this.detVf = DetailsViewFragment.instance(r);
    this.proVf = ProfileViewFragment.instance(r);
  }

  @Override
  public Fragment getItem(int i) {

    if (i == 0) {
      return ingVf;
    }
    else if (i == 1) {
      return insVf;
    }
    else if (i == 2) {
      return detVf;
    }
    else {
      return proVf;
    }
  }

  @Override
  public int getCount() {
    return 4;
  }

  @Override
  public CharSequence getPageTitle(int position) {

    // Set title based on position in list
    if (position == 0) {
      return "Ingredients";
    }
    else if (position == 1) {
      return "Instructions";
    }
    else if (position == 2) {
      return "Details";
    }
    else if (position == 3) {
      return "Profiles";
    }

    return "Unknown Tab";
  }

  public InstructionViewFragment getInstructionViewFragment() {
    return this.insVf;
  }

  @Override
  public void restoreState(Parcelable p, ClassLoader cl) {
    // Override this to do nothing.  It is not needed, and throws exceptions for reasons.
    return;
  }
}

