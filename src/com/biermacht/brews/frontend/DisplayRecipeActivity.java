package com.biermacht.brews.frontend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.frontend.adapters.*;

import android.support.v4.view.*;

import java.io.IOException;

public class DisplayRecipeActivity extends FragmentActivity {
	
	private Recipe mRecipe;
	private long id; // id of recipe we use
	private int currentItem; // For storing current page
	DisplayRecipeCollectionPagerAdapter cpAdapter;
    ViewPager mViewPager;

    private class UpdateTask extends AsyncTask<String, Void, String> {

        private Context context;
        private ProgressDialog progress;

        public UpdateTask(Context c)
        {
            this.context = c;
        }

        @Override
        protected String doInBackground(String... params)
        {
            appContext = getApplicationContext();

            // Get recipe from calling activity
            id = getIntent().getLongExtra(Constants.INTENT_RECIPE_ID, Constants.INVALID_ID);
            mRecipe = Utils.getRecipeWithId(id);

            // ViewPager and pagerAdapter for Slidy tabs!
            cpAdapter = new DisplayRecipeCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
            mViewPager = (ViewPager) findViewById(R.id.pager);

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            progress.dismiss();

            // Set icon as back button
            getActionBar().setDisplayHomeAsUpEnabled(true);

            // Set title based on recipe name
            setTitle(mRecipe.getRecipeName());

            // Set Adapter
            mViewPager.setAdapter(cpAdapter);

            // Set to the current item
            mViewPager.setCurrentItem(currentItem);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Loading Recipe");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
	
	private Context appContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

        // Set current item to be the first
        currentItem = 0;

        // Update Asynchronously
        new UpdateTask(this).execute("");
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
    	    	i.putExtra(Constants.INTENT_RECIPE_ID, mRecipe.getId());
    		    startActivity(i);
    		    return true;

            case R.id.add_hop:
    	    	i = new Intent(this.appContext, AddHopsActivity.class);
    	    	i.putExtra(Constants.INTENT_RECIPE_ID, mRecipe.getId());
    		    startActivity(i);
    		    return true;
    		    
            case R.id.add_yeast:
    	    	i = new Intent(this.appContext, AddYeastActivity.class);
    	    	i.putExtra(Constants.INTENT_RECIPE_ID, mRecipe.getId());
    		    startActivity(i);
    		    return true;
				
			case R.id.add_misc:
    	    	i = new Intent(this.appContext, AddMiscActivity.class);
    	    	i.putExtra(Constants.INTENT_RECIPE_ID, mRecipe.getId());
    		    startActivity(i);
    		    return true;
    		    
            case R.id.menu_timer:
            	i = new Intent(this.appContext, BrewTimerActivity.class);
                i.putExtra(Constants.INTENT_RECIPE_ID, mRecipe.getId());
                startActivity(i);
            	return true;
    		    
            case R.id.menu_edit_recipe:
          		i = new Intent(this.appContext, EditRecipeActivity.class);
          		i.putExtra(Constants.INTENT_RECIPE_ID, mRecipe.getId());
          		startActivity(i);
				return true;
			
			case R.id.menu_edit_mash_profile:
				i = new Intent(this.appContext, EditMashProfileActivity.class);
				i.putExtra(Constants.INTENT_RECIPE_ID, mRecipe.getId());
				startActivity(i);
				return true;
			
			case R.id.menu_edit_fermentation_profile:
				i = new Intent(this.appContext, EditFermentationProfileActivity.class);
				i.putExtra(Constants.INTENT_RECIPE_ID, mRecipe.getId());
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

        // Update Asynchronously
        new UpdateTask(this).execute("");
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
