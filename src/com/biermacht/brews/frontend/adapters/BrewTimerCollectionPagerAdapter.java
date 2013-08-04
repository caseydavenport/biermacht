package com.biermacht.brews.frontend.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;

import com.biermacht.brews.frontend.fragments.BrewTimerStepFragment;
import com.biermacht.brews.frontend.fragments.DetailsViewFragment;
import com.biermacht.brews.frontend.fragments.IngredientViewFragment;
import com.biermacht.brews.frontend.fragments.InstructionViewFragment;
import com.biermacht.brews.frontend.fragments.ProfileViewFragment;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.Recipe;

import java.util.ArrayList;


// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class BrewTimerCollectionPagerAdapter extends FragmentStatePagerAdapter {
	private Recipe r;
    private Context c;
    private ArrayList<Fragment> instructionFragmentList;

    public BrewTimerCollectionPagerAdapter(FragmentManager fm, Recipe r, Context c) {
        super(fm);
		this.r = r;
		this.c = c;
        instructionFragmentList = new ArrayList<Fragment>();

        for (Instruction i : r.getInstructionList())
            if (i.showInBrewTimer())
            {
                BrewTimerStepFragment f = new BrewTimerStepFragment(c, r, i);
                instructionFragmentList.add(f);
            }
    }

    @Override
    public Fragment getItem(int i) {
        return instructionFragmentList.get(i);
    }

    @Override
    public int getCount() {
			return instructionFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
            return instructionFragmentList.get(position).toString();
    }
}

