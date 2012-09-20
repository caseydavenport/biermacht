package com.biermacht.brews.frontend;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;

public class DisplayRecipeActivity extends FragmentActivity {
	
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
        mRecipe = MainActivity.recipeDataSource.getRecipeWithId(id);
        
        // Set title based on recipe name
        setTitle(mRecipe.getRecipeName());

        // Fake description
        mRecipe.setDescription("This is just a sample description.  Hopefully this will show up in the description section of the details page.  That would be really nice.  I'm also going to extend this on for a bit to make sure it is lengthy enough to take up some space, and hopefully even require scrolling of some sort to occur on the description / details page");
        
        
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


