package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.biermacht.brews.frontend.fragments.StyleNotesFragment;
import com.biermacht.brews.frontend.fragments.StyleParametersFragment;
import com.biermacht.brews.frontend.fragments.StyleProfileFragment;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

import java.util.ArrayList;

public class DisplayStyleCollectionPagerAdapter extends FragmentStatePagerAdapter {
  Context c;
  BeerStyle s;
  ArrayList<Fragment> fragmentList;
  StyleNotesFragment notesFragment;
  StyleProfileFragment profileFragment;
  StyleParametersFragment parametersFragment;

  public DisplayStyleCollectionPagerAdapter(FragmentManager fm, BeerStyle s, Context c) {
    super(fm);
    this.s = s;
    this.c = c;
    this.notesFragment = StyleNotesFragment.instance(s);
    this.profileFragment = StyleProfileFragment.instance(s);
    this.parametersFragment = StyleParametersFragment.instance(s);

    // Keep in list.
    this.fragmentList = new ArrayList<>();
    this.fragmentList.add(notesFragment);
    this.fragmentList.add(profileFragment);
    this.fragmentList.add(parametersFragment);
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

