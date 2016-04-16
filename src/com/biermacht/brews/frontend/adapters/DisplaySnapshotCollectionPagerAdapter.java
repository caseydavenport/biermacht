package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.biermacht.brews.frontend.fragments.AboutSnapshotFragment;
import com.biermacht.brews.frontend.fragments.DetailsViewFragment;
import com.biermacht.brews.frontend.fragments.IngredientViewFragment;
import com.biermacht.brews.frontend.fragments.InstructionViewFragment;
import com.biermacht.brews.frontend.fragments.ProfileViewFragment;
import com.biermacht.brews.recipe.RecipeSnapshot;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

import java.util.ArrayList;

public class DisplaySnapshotCollectionPagerAdapter extends FragmentStatePagerAdapter {
  private RecipeSnapshot snapshot;
  private Context c;
  ArrayList<Fragment> fragmentList;
  public AboutSnapshotFragment aboutFragment;
  public IngredientViewFragment ingredientFragment;
  public DetailsViewFragment detailsFragment;
  public ProfileViewFragment profileFragment;
  public InstructionViewFragment instructionFragment;

  public DisplaySnapshotCollectionPagerAdapter(FragmentManager fm, RecipeSnapshot snap, Context c) {
    super(fm);
    this.snapshot = snap;
    this.c = c;
    this.aboutFragment = AboutSnapshotFragment.instance(snap);
    this.ingredientFragment = IngredientViewFragment.instance(snap);
    this.instructionFragment = InstructionViewFragment.instance(snap);
    this.detailsFragment = DetailsViewFragment.instance(snap);
    this.profileFragment = ProfileViewFragment.instance(snap);

    this.fragmentList = new ArrayList<Fragment>();
    this.fragmentList.add(aboutFragment);
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
    BiermachtFragment f = (BiermachtFragment) this.fragmentList.get(position);
    return f.name();
  }

  @Override
  public void restoreState(Parcelable p, ClassLoader cl) {
    // Override this to do nothing.  It is not needed, and throws exceptions for "reasons".
    return;
  }
}

