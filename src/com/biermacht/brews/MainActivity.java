package com.biermacht.brews;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	// Create our stuff
	private RecipeArrayAdapter mAdapter;
	private OnItemClickListener mClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Make views here
        ListView listView = (ListView) findViewById(R.id.recipe_list);
        
        // Create a bunch of test brews here
        Recipe brew1 = new Recipe("Arizona Pale Ale");
        Recipe brew2 = new Recipe("Panther Stout");
        Recipe brew3 = new Recipe("Chattanooga Cherry Weissbier");
        Recipe brew4 = new Recipe("Sherman Light");
        
        brew1.setBeerType(Recipe.BEERTYPE_IPA);
        brew2.setBeerType(Recipe.BEERTYPE_STOUT);
        brew3.setBeerType(Recipe.BEERTYPE_HEFEWEIZEN);
        
        // Add the test brews to the list
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        recipeList.add(brew1);
        recipeList.add(brew2);
        recipeList.add(brew3);
        recipeList.add(brew4);
        
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
        
        // Set up my listView with title and ArrayAdapter
        mAdapter = new RecipeArrayAdapter(this, recipeList);
        listView.setAdapter(mAdapter);
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
}
