package com.biermacht.brews.frontend;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class DisplayRecipeActivity extends FragmentActivity implements OnClickListener {
	
	private Recipe mRecipe;
	private Fragment instructionFragment;
	private Fragment ingredientFragment;
	private Fragment detailsFragment;
	
	public static Context appContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        
        appContext = getApplicationContext();
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra("biermacht.brews.recipeID", 0);
        mRecipe = Utils.getRecipeWithId(id);
        
        // Set title based on recipe name
        setTitle(mRecipe.getRecipeName());
      
        // Set up ActionBar tabs
    	final ActionBar actionBar = getActionBar();
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Create Tabs
		ActionBar.Tab ingredientTab = actionBar.newTab();
		ActionBar.Tab instructionTab = actionBar.newTab();
		ActionBar.Tab detailsTab = actionBar.newTab();
		
		// Create fragments
		 ingredientFragment = new CustomFragment(R.layout.ingredient_view, mRecipe);
		 instructionFragment = new CustomFragment(R.layout.instruction_view, mRecipe);
		 detailsFragment = new CustomFragment(R.layout.details_view, mRecipe);
		
		// Set Tab text
		ingredientTab.setText("Ingredients");
		instructionTab.setText("Intructions");
		detailsTab.setText("Details");
        
		// Set Tab Listeners
    	ingredientTab.setTabListener(new MyTabsListener(ingredientFragment));
    	instructionTab.setTabListener(new MyTabsListener(instructionFragment));
    	detailsTab.setTabListener(new MyTabsListener(detailsFragment));
        
		// Add Tab to bar
		actionBar.addTab(ingredientTab);
		actionBar.addTab(instructionTab);
		actionBar.addTab(detailsTab);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_display_recipe, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }
    
    @Override
    public void onResume()
    {
    	Log.e("DisplayRecipe", "Display Recipe: onResume()");
    	super.onResume();
    	((CustomFragment) ingredientFragment).update();
    }

	public void onClick(View v) 
	{
		// if ADD GRAIN button pressed
		if(v.getId() == R.id.add_new_grain_button)
		{
	    	Intent intent = new Intent(DisplayRecipeActivity.appContext, AddGrainActivity.class);
		    intent.putExtra("com.biermacht.brews.recipeId", mRecipe.getId());
		    startActivity(intent);
		}
		// if ADD HOPS button pressed
		if(v.getId() == R.id.add_new_hops_button)
		{
	    	Intent intent = new Intent(DisplayRecipeActivity.appContext, AddHopsActivity.class);
		    intent.putExtra("com.biermacht.brews.recipeId", mRecipe.getId());
		    startActivity(intent);
		}
	}
}

class MyTabsListener implements ActionBar.TabListener {
    public Fragment fragment;
    
    public MyTabsListener(Fragment fragment) {
            this.fragment = fragment;
    }
    
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // Do nothing on tab reselect
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, fragment);
            ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
    }   
}


