package com.biermacht.brews.frontend;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.fragments.DetailsViewFragment;
import com.biermacht.brews.frontend.fragments.IngredientViewFragment;
import com.biermacht.brews.frontend.fragments.InstructionViewFragment;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class DisplayRecipeActivity extends FragmentActivity implements OnClickListener {
	
	private Recipe mRecipe;
	private InstructionViewFragment instructionFragment;
	private IngredientViewFragment ingredientFragment;
	private DetailsViewFragment detailsFragment;
	
	public static Context appContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);
        getActionBar().setDisplayHomeAsUpEnabled(true);
              
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
		 ingredientFragment = new IngredientViewFragment(mRecipe);
		 instructionFragment = new InstructionViewFragment(mRecipe);
		 detailsFragment = new DetailsViewFragment(mRecipe);
		
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
        // getMenuInflater().inflate(R.menu.activity_display_recipe, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
            case android.R.id.home:
        		finish();
        		return true;
            
            case R.id.add_fermentable:
    	    	Intent ferm_intent = new Intent(DisplayRecipeActivity.appContext, AddGrainActivity.class);
    	    	ferm_intent.putExtra("com.biermacht.brews.recipeId", mRecipe.getId());
    		    startActivity(ferm_intent);
    		    return true;

            case R.id.add_hop:
    	    	Intent hop_intent = new Intent(DisplayRecipeActivity.appContext, AddHopsActivity.class);
    	    	hop_intent.putExtra("com.biermacht.brews.recipeId", mRecipe.getId());
    		    startActivity(hop_intent);
    		    return true;
    		    
            case R.id.add_yeast:
    	    	Intent yeast_intent = new Intent(DisplayRecipeActivity.appContext, AddYeastActivity.class);
    	    	yeast_intent.putExtra("com.biermacht.brews.recipeId", mRecipe.getId());
    		    startActivity(yeast_intent);
    		    return true;
    		    
            case R.id.menu_edit_recipe:
          		Intent i = new Intent(DisplayRecipeActivity.appContext, EditRecipeActivity.class);
          		i.putExtra("biermacht.brews.recipeID", mRecipe.getId());
          		startActivity(i); 
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
    	super.onResume();
    	
    	// Call the update methods for each fragment
    	detailsFragment.update();
    	ingredientFragment.update();
    	instructionFragment.update();
    }

	public void onClick(View v) 
	{
		
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
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
    }   
}


