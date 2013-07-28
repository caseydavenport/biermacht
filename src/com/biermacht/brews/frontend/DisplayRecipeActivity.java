package com.biermacht.brews.frontend;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.frontend.fragments.*;
import android.support.v4.view.*;

public class DisplayRecipeActivity extends FragmentActivity implements OnClickListener {
	
	private Recipe mRecipe;
	private long id; // id of recipe we use
	private int currentItem; // For storing current page
	CollectionPagerAdapter cpAdapter;
    ViewPager mViewPager;
	
	private Context appContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);
              
        appContext = getApplicationContext();
        
        // Get recipe from calling activity
        id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, -1);
        mRecipe = Utils.getRecipeWithId(id);
        
        // Set title based on recipe name
        setTitle(mRecipe.getRecipeName());
		
		// ViewPager and pagerAdapter for slidy tabs!
        cpAdapter = new CollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(cpAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.activity_display_recipe, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
    	switch (item.getItemId()) {
            case android.R.id.home:
        		finish();
        		return true;

            case R.id.add_fermentable:
    	    	i = new Intent(this.appContext, AddFermentableActivity.class);
    	    	i.putExtra(Utils.INTENT_RECIPE_ID, mRecipe.getId());
    		    startActivity(i);
    		    return true;

            case R.id.add_hop:
    	    	i = new Intent(this.appContext, AddHopsActivity.class);
    	    	i.putExtra(Utils.INTENT_RECIPE_ID, mRecipe.getId());
    		    startActivity(i);
    		    return true;
    		    
            case R.id.add_yeast:
    	    	i = new Intent(this.appContext, AddYeastActivity.class);
    	    	i.putExtra(Utils.INTENT_RECIPE_ID, mRecipe.getId());
    		    startActivity(i);
    		    return true;
				
			case R.id.add_misc:
    	    	i = new Intent(this.appContext, AddMiscActivity.class);
    	    	i.putExtra(Utils.INTENT_RECIPE_ID, mRecipe.getId());
    		    startActivity(i);
    		    return true;
    		    
            case R.id.menu_timer:
            	cpAdapter.getInstructionViewFragment().toggleTimer();
            	return true;
    		    
            case R.id.menu_edit_recipe:
          		i = new Intent(this.appContext, EditRecipeActivity.class);
          		i.putExtra(Utils.INTENT_RECIPE_ID, mRecipe.getId());
          		startActivity(i);
				return true;
			
			case R.id.menu_edit_mash_profile:
				i = new Intent(this.appContext, EditMashProfileActivity.class);
				i.putExtra(Utils.INTENT_RECIPE_ID, mRecipe.getId());
				startActivity(i);
				return true;
			
			case R.id.menu_edit_fermentation_profile:
				i = new Intent(this.appContext, EditFermentationProfileActivity.class);
				i.putExtra(Utils.INTENT_RECIPE_ID, mRecipe.getId());
				startActivity(i);
				return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	// Update what we display
		mRecipe = Utils.getRecipeWithId(id);

        // Set title based on recipe name
        setTitle(mRecipe.getRecipeName());

		// ViewPager and pagerAdapter for slidy tabs!
        cpAdapter = new CollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
        mViewPager.setAdapter(cpAdapter);
        mViewPager.setCurrentItem(this.currentItem);
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	// Save the current page we're looking at
    	this.currentItem = mViewPager.getCurrentItem();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

	public void onClick(View v) 
	{
		
	}
}
