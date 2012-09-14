package com.biermacht.brews;

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
        
        // Make random ingredients and instructions and add them...
        Ingredient hops = new Ingredient(Ingredient.TYPE_HOPS, "Vanguard", 1.0, "oz");
        Ingredient malt = new Ingredient(Ingredient.TYPE_MALT, "Amber", 6.3, "lbs");
        Ingredient hops2 = new Ingredient(Ingredient.TYPE_HOPS, "Challenger", 1.0, "oz");
        Ingredient yeast = new Ingredient(Ingredient.TYPE_YEAST, "Random", 1, "vial");
        
        mRecipe.addIngredient(hops);
        mRecipe.addIngredient(malt);
        mRecipe.addIngredient(hops2);
        mRecipe.addIngredient(yeast);
        
        Instruction timedInst = new Instruction("This should have a time limit!");
        timedInst.setDuration(15, "mins");
        
        mRecipe.addInstruction(new Instruction("Do one thing!"));
        mRecipe.addInstruction(timedInst);
        mRecipe.addInstruction(new Instruction("Then, you should probably do another thing!"));
        mRecipe.addInstruction(new Instruction("And, you should do more..."));
        mRecipe.addInstruction(new Instruction("This is an important step.  Please make sure to you know, do this one.  Because it's important and all."));
        mRecipe.addInstruction(new Instruction("And, you should do more..."));
        mRecipe.addInstruction(new Instruction("After you have performed both of those things, it might be best to do one more thing"));
        mRecipe.addInstruction(new Instruction("Shit man, don't forget THAT thing... if you don't do that it will NEVER WORK.. christ what do you think you're doing?"));
        mRecipe.addInstruction(new Instruction("After that, you're done!"));
        
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


