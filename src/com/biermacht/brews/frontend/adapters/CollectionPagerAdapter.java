package com.biermacht.brews.frontend.adapters;
import android.support.v4.app.*;
import com.biermacht.brews.recipe.*;
import android.content.*;
import com.biermacht.brews.frontend.fragments.*;


// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
	Recipe r;
	Context c;
	IngredientViewFragment ingVf;
	InstructionViewFragment insVf;
	DetailsViewFragment detVf;

    public CollectionPagerAdapter(FragmentManager fm, Recipe r, Context c) {
        super(fm);
		this.r = r;
		this.c = c;
		this.ingVf = new IngredientViewFragment(c,r);
		this.insVf = new InstructionViewFragment(c,r);
		this.detVf = new DetailsViewFragment(c,r);
    }

    @Override
    public Fragment getItem(int i) {
        
		if (i ==0)
			return ingVf;
		else if (i == 1)
			return insVf;
        else 
			return detVf;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

		// Set title based on position in list
		if (position == 0)
			return "Ingredients";
		else if (position == 1)
			return "Instructions";
		else if (position == 2)
			return "Details";

        return "Unknown Tab";
    }
}

