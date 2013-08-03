package com.biermacht.brews.frontend;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.BrewTimerCollectionPagerAdapter;
import com.biermacht.brews.frontend.adapters.DisplayRecipeCollectionPagerAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class BrewTimerActivity extends FragmentActivity {
	
	private Recipe mRecipe;
	private long id; // id of recipe we use
	private int currentItem; // For storing current page
	BrewTimerCollectionPagerAdapter cpAdapter;

    // Views
    ViewGroup mainLayout;
    ViewPager mViewPager;
    TextView hoursView;
    TextView minutesView;
    TextView secondsView;
    View timerControls;

    // LayoutInflater
    LayoutInflater inflater;
	
	private Context appContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_timer);
        
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get inflater
        inflater = getLayoutInflater();

        // Get main layout
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        // Get context
        appContext = getApplicationContext();
        
        // Get recipe from calling activity
        id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, -1);
        mRecipe = Utils.getRecipeWithId(id);

        // Set title based on recipe name
        setTitle("Brew Timer");

        // Inflate timer controls
        timerControls = inflater.inflate(R.layout.view_timer_controls, mainLayout, false);

        // get countdown views
        hoursView = (TextView) timerControls.findViewById(R.id.hours);
        minutesView = (TextView) timerControls.findViewById(R.id.minutes);
        secondsView = (TextView) timerControls.findViewById(R.id.seconds);

		// ViewPager and pagerAdapter for slidy tabs!
        cpAdapter = new BrewTimerCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(cpAdapter);

        // Add views
        mainLayout.addView(timerControls);
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
        cpAdapter = new BrewTimerCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
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

    public void onClick(View v) {

    }
}
