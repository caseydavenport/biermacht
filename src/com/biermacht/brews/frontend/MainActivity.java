package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.RecipeDataSource;
import com.biermacht.brews.recipe.Grain;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.BrewCalculator;
import com.biermacht.brews.utils.Utils;

public class MainActivity extends ListActivity {
	
	// Create our stuff
	private RecipeArrayAdapter mAdapter;
	private OnItemClickListener mClickListener;
	private TextWatcher mTextWatcher;
	private ArrayList<Recipe> recipeList;
	public static RecipeDataSource recipeDataSource;
	
    //Declare views here
    private ListView listView; 
    private EditText searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize views
        listView = getListView();
        searchView = (EditText) findViewById(R.id.search_bar);
        
        // Put recipes into database
        
        recipeDataSource = new RecipeDataSource(getApplicationContext());
        recipeDataSource.open();
        
        
        // Create a bunch of test brews here
        Recipe brew1 = new Recipe("Arizona Pale Ale");
        Recipe brew2 = new Recipe("Panther Stout");
        Recipe brew3 = new Recipe("Chattanooga Cherry Weissbier");
        Recipe brew4 = new Recipe("Sherman Light");
        
        brew1.setBeerType(Utils.BEERTYPE_IPA);
        brew2.setBeerType(Utils.BEERTYPE_STOUT);
        brew3.setBeerType(Utils.BEERTYPE_HEFEWEIZEN);
        
        Grain testGrain = (Grain) Utils.FERMENTABLE_MUNICH_MALT;
        Grain testGrain2 = (Grain) Utils.FERMENTABLE_VIENNA_MALT;
        testGrain.setWeight(6);
        testGrain2.setWeight(6);
        
        brew4.addIngredient(testGrain);
        brew4.addIngredient(testGrain2);
        brew3.addIngredient(testGrain2);
        brew2.addIngredient(testGrain);
        brew4.update();
        brew3.update();
        brew2.update();
        brew1.update();
        
        recipeDataSource.deleteRecipeIfExists(1);
        recipeDataSource.deleteRecipeIfExists(2);
        recipeDataSource.deleteRecipeIfExists(3);
        recipeDataSource.deleteRecipeIfExists(4);
        
        // Add the recipes to the database
        recipeDataSource.addRecipeToDatabase(brew1);
        recipeDataSource.addRecipeToDatabase(brew2);
        recipeDataSource.addRecipeToDatabase(brew3);
        recipeDataSource.addRecipeToDatabase(brew4);
        
        
        
        // Get recipes to display
        recipeList = Utils.getRecipeList(recipeDataSource);
        
        // Set up the onClickListener
        mClickListener = new OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> parentView, View childView, int pos,
						long id)
			{	
			    Intent intent = new Intent(MainActivity.this, DisplayRecipeActivity.class);
			    intent.putExtra("biermacht.brews.recipeID", recipeList.get(pos).getId());
			    startActivity(intent);				
			}
        };
        
        // Set up the textWatcher for search bar
        mTextWatcher = new TextWatcher() {
			public void afterTextChanged(Editable s) 
			{
			} 
            public void beforeTextChanged(CharSequence s, int start, int count, int after) 
            {
            }
            
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	ArrayList<Recipe> fList = getFilteredList(s.toString());
            	updateRecipeList(fList);
            }
        };
        
        // Add TextWatcher to search bar
        searchView.addTextChangedListener(mTextWatcher);
        
        // Set up my listView with title and ArrayAdapter
        updateRecipeList(recipeList);
        listView.setOnItemClickListener(mClickListener);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    	case R.id.menu_new_recipe:
    		Intent i = new Intent(getApplicationContext(), AddNewRecipeActivity.class);
    		startActivity(i);
    		break;
    	}
    	return true;
    }
    
    /**
     * Filters the list of all Recipes by the given string
     * @param s String to filter by
     * @return ArrayList of matching recipes
     */
    private ArrayList<Recipe> getFilteredList(String s)
    {
    	recipeList = Utils.getRecipeList(recipeDataSource);
    	ArrayList<Recipe> filteredList = new ArrayList<Recipe>();
    	
    	for (Recipe r : recipeList)
    	{
    		if(r.getRecipeName().toLowerCase().contains(s.toLowerCase()))
    		{
    			filteredList.add(r);
    		}
    	}  
    	return filteredList;
    }
    
    private void updateRecipeList(ArrayList<Recipe> l)
    {
        // Set up my listView with title and ArrayAdapter
        mAdapter = new RecipeArrayAdapter(getApplicationContext(), l);
        listView.setAdapter(mAdapter);
    }
}
