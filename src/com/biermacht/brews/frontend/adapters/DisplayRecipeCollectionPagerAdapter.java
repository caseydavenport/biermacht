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

import java.util.ArrayList;

public class DisplayRecipeCollectionPagerAdapter extends FragmentStatePagerAdapter {
  Recipe r;
  Context c;
  ArrayList<Fragment> fragmentList;
  IngredientViewFragment ingredientFragment;
  InstructionViewFragment instructionFragment;
  DetailsViewFragment detailsFragment;
  ProfileViewFragment profileFragment;

  public DisplayRecipeCollectionPagerAdapter(FragmentManager fm, Recipe r, Context c) {
    super(fm);
    this.r = r;
    this.c = c;
    this.ingredientFragment = IngredientViewFragment.instance(r);
    this.instructionFragment = InstructionViewFragment.instance(r);
    this.detailsFragment = DetailsViewFragment.instance(r);
    this.profileFragment = ProfileViewFragment.instance(r);

    // Keep in list.
    this.fragmentList = new ArrayList<>();
    this.fragmentList.add(ingredientFragment);
    this.fragmentList.add(instructionFragment);
    this.fragmentList.add(detailsFragment);
    this.fragmentList.add(profileFragment);
  }

  @Override
  public Fragment getItem(int i) {
    return this.fragmentList.get(i);
  }

  @Override
  public int getCount() {
    return this.fragmentList.size();
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

  @Override
  public void restoreState(Parcelable p, ClassLoader cl) {
    // Override this to do nothing.  It is not needed, and throws exceptions for "reasons".
    return;
  }
}

