package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.biermacht.brews.frontend.fragments.AboutSnapshotFragment;
import com.biermacht.brews.frontend.fragments.DetailsViewFragment;
import com.biermacht.brews.frontend.fragments.IngredientViewFragment;
import com.biermacht.brews.frontend.fragments.ProfileViewFragment;
import com.biermacht.brews.recipe.RecipeSnapshot;

public class DisplaySnapshotCollectionPagerAdapter extends FragmentStatePagerAdapter {
  private RecipeSnapshot snapshot;
  private Context c;
  public AboutSnapshotFragment aboutFragment;
  public IngredientViewFragment ingredientFragment;
  public DetailsViewFragment detailsFragment;
  public ProfileViewFragment profileFragment;

  public DisplaySnapshotCollectionPagerAdapter(FragmentManager fm, RecipeSnapshot snap, Context c) {
    super(fm);
    this.snapshot = snap;
    this.c = c;
    this.aboutFragment = AboutSnapshotFragment.instance(snap);
    this.ingredientFragment = IngredientViewFragment.instance(snap);
    this.detailsFragment = DetailsViewFragment.instance(snap);
    this.profileFragment = ProfileViewFragment.instance(snap);
  }

  @Override
  public Fragment getItem(int i) {

    if (i == 0) {
      return this.aboutFragment;
    }
    else if (i == 1) {
      return this.ingredientFragment;
    }
    else if (i == 2) {
      return this.detailsFragment;
    }
    else {
      return this.profileFragment;
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
      return "About";
    }
    else if (position == 1) {
      return "Ingredients";
    }
    else if (position == 2) {
      return "Details";
    }
    else if (position == 3) {
      return "Notes";
    }

    return "Unknown Tab";
  }

  @Override
  public void restoreState(Parcelable p, ClassLoader cl) {
    // Override this to do nothing.  It is not needed, and throws exceptions for "reasons".
    return;
  }
}

