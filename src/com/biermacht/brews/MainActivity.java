package com.biermacht.brews;

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

public class MainActivity extends ListActivity {
	
	// Create our stuff
	private RecipeArrayAdapter mAdapter;
	private OnItemClickListener mClickListener;
	private TextWatcher mTextWatcher;
	private ArrayList<Recipe> recipeList;
	
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
        
        // Get recipes to display
        recipeList = Utils.getRecipeList();
        
        // Set up the onClickListener
        mClickListener = new OnItemClickListener() 
        {
			public void onItemClick(AdapterView<?> parentView, View childView, int pos,
						long id)
			{	
				Recipe recipe = (Recipe) getListView().getItemAtPosition(pos);
			    Intent intent = new Intent(getApplicationContext(), DisplayRecipeActivity.class);
			    intent.putExtra("com.biermacht.brews.RECIPE", recipe);
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
    	recipeList = Utils.getRecipeList();
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
