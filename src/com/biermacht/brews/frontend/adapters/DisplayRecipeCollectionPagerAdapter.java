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
import com.biermacht.brews.frontend.fragments.SnapshotsViewFragment;
import com.biermacht.brews.recipe.Recipe;

public class DisplayRecipeCollectionPagerAdapter extends FragmentStatePagerAdapter {
  Recipe r;
  Context c;
  IngredientViewFragment ingredientFragment;
  InstructionViewFragment instructionFragment;
  DetailsViewFragment detailsFragment;
  ProfileViewFragment profileFragment;
  SnapshotsViewFragment snapshotFragment;

  public DisplayRecipeCollectionPagerAdapter(FragmentManager fm, Recipe r, Context c) {
    super(fm);
    this.r = r;
    this.c = c;
    this.ingredientFragment = IngredientViewFragment.instance(r);
    this.instructionFragment = InstructionViewFragment.instance(r);
    this.detailsFragment = DetailsViewFragment.instance(r);
    this.profileFragment = ProfileViewFragment.instance(r);
    this.snapshotFragment = SnapshotsViewFragment.instance(r);
  }

  @Override
  public Fragment getItem(int i) {

    if (i == 0) {
      return snapshotFragment;
    }
    else if (i == 1) {
      return ingredientFragment;
    }
    else if (i == 2) {
      return instructionFragment;
    }
    else if (i == 3) {
      return detailsFragment;
    }
    else {
      return profileFragment;
    }
  }

  @Override
  public int getCount() {
    return 5;
  }

  @Override
  public CharSequence getPageTitle(int position) {

    // Set title based on position in list
    if (position == 0) {
      return "Snapshots";
    }
    else if (position == 1) {
      return "Ingredients";
    }
    else if (position == 2) {
      return "Instructions";
    }
    else if (position == 3) {
      return "Details";
    }
    else if (position == 4) {
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

