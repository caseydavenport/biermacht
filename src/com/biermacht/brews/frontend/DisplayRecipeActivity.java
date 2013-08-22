package com.biermacht.brews.frontend;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddFermentableActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddHopsActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddMiscActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddYeastActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditRecipeActivity;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.utils.Database;

import android.support.v4.view.*;

public class DisplayRecipeActivity extends FragmentActivity {

	private Recipe mRecipe;
	private long id; // id of recipe we use
	private int currentItem; // For storing current page
	DisplayRecipeCollectionPagerAdapter cpAdapter;
    ViewPager mViewPager;
    ViewPager.OnPageChangeListener pageListener;
    Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

        // Set current item to be the first
        currentItem = 0;

        // Get recipe from intent
        mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);

        // Set on page change listener
        pageListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                currentItem = position;
                updateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };

        //Update user interface
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;
        menu.removeItem(R.id.menu_add_ing);
        menu.removeItem(R.id.menu_edit_recipe);
        menu.removeItem(R.id.menu_timer);
        menu.removeItem(R.id.menu_profile_dropdown);

        switch (currentItem)
        {
            case 0:
                getMenuInflater().inflate(R.menu.fragment_ingredient_menu, menu);
                break;
            case 1:
                getMenuInflater().inflate(R.menu.fragment_instruction_menu, menu);
                break;
            case 2:
                getMenuInflater().inflate(R.menu.fragment_details_menu, menu);
                break;
            case 3:
                getMenuInflater().inflate(R.menu.fragment_profile_menu, menu);
                break;
        }
        return true;
    }

    private void updateUI()
    {
        // Set icon as back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get recipe id from calling activity
        id = getIntent().getLongExtra(Constants.KEY_RECIPE_ID, Constants.INVALID_ID);

        // ViewPager and pagerAdapter for Slidy tabs!
        cpAdapter = new DisplayRecipeCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, getApplicationContext());

        // Set title based on recipe name
        setTitle(mRecipe.getRecipeName());

        // TODO: Temporary hack to fix dumb bug
        if (currentItem == 1)
            currentItem = 0;

        // Set Adapter
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(cpAdapter);

        // Set to the current item
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOnPageChangeListener(pageListener);

        updateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
    	switch (item.getItemId()) {
            case android.R.id.home:
        		finish();
        		return true;

            case R.id.add_fermentable:
    	    	i = new Intent(this.getApplicationContext(), AddFermentableActivity.class);
    	    	i.putExtra(Constants.KEY_RECIPE, mRecipe);
    		    startActivity(i);
    		    return true;

            case R.id.add_hop:
    	    	i = new Intent(getApplicationContext(), AddHopsActivity.class);
    	    	i.putExtra(Constants.KEY_RECIPE, mRecipe);
                startActivity(i);
    		    return true;

            case R.id.add_yeast:
    	    	i = new Intent(getApplicationContext(), AddYeastActivity.class);
    	    	i.putExtra(Constants.KEY_RECIPE, mRecipe);
                startActivity(i);
    		    return true;

			case R.id.add_misc:
    	    	i = new Intent(getApplicationContext(), AddMiscActivity.class);
    	    	i.putExtra(Constants.KEY_RECIPE, mRecipe);
                startActivity(i);
    		    return true;

            case R.id.menu_timer:
            	i = new Intent(getApplicationContext(), BrewTimerActivity.class);
                i.putExtra(Constants.KEY_RECIPE, mRecipe);
                startActivity(i);
            	return true;

            case R.id.menu_edit_recipe:
          		i = new Intent(getApplicationContext(), EditRecipeActivity.class);
          		i.putExtra(Constants.KEY_RECIPE, mRecipe);
                startActivity(i);
				return true;

			case R.id.menu_edit_mash_profile:
				i = new Intent(getApplicationContext(), EditMashProfileActivity.class);
                Log.d("DisplayRecipeActivity", "NAME: " + mRecipe.getMashProfile().getName());
				i.putExtra(Constants.KEY_RECIPE, mRecipe);
                i.putExtra(Constants.KEY_PROFILE_ID, mRecipe.getMashProfile().getId());
                i.putExtra(Constants.KEY_PROFILE, mRecipe.getMashProfile());
                startActivity(i);
				return true;

			case R.id.menu_edit_fermentation_profile:
				i = new Intent(getApplicationContext(), EditFermentationProfileActivity.class);
				i.putExtra(Constants.KEY_RECIPE, mRecipe);
                startActivity(i);
				return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onResume()
    {
        super.onResume();
        Log.d("DisplayRecipeActivity", "onResume: Getting recipe from database");
        try
        {
            mRecipe = Database.getRecipeWithId(mRecipe.getId());
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        updateUI();
    }

    public void updateOptionsMenu()
    {
        if (menu != null)
            onCreateOptionsMenu(menu);
        else
        {
            mViewPager.setCurrentItem(0);
        }
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
}
