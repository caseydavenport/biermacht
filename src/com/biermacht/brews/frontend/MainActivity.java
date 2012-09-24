package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.biermacht.brews.EditRecipeActivity;
import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseInterface;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class MainActivity extends ListActivity {
	
	// Create our stuff
	private RecipeArrayAdapter mAdapter;
	private OnItemClickListener mClickListener;
	private OnItemLongClickListener mLongClickListener;
	private TextWatcher mTextWatcher;
	private ArrayList<Recipe> recipeList;
	private Recipe selectedRecipe;
	public static DatabaseInterface databaseInterface;
	
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
        
        // Declare my database interface 
        databaseInterface = new DatabaseInterface(getApplicationContext());
        databaseInterface.open();

        // Get recipes to display
        recipeList = Utils.getRecipeList(databaseInterface);
        
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
        registerForContextMenu(listView);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	updateRecipeList(getFilteredList(searchView.getText().toString()));
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
    	recipeList = Utils.getRecipeList(databaseInterface);
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
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
      if (v == listView) 
      {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        String title = recipeList.get(info.position).getRecipeName();
        
        selectedRecipe = recipeList.get(info.position);
        
        menu.setHeaderTitle(title);
        String[] menuItems = {"Edit Recipe", "Delete Recipe"};
        
        for (int i = 0; i < menuItems.length; i++) 
        {
          menu.add(Menu.NONE, i, i, menuItems[i]);
        }
      }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
      int menuItemIndex = item.getItemId();
      
      // Edit recipe selected
      if (menuItemIndex == 0)
      {
  		Intent i = new Intent(getApplicationContext(), EditRecipeActivity.class);
  		startActivity(i); 
      }
      // Delete recipe selected
      else if (menuItemIndex == 1)
      {
    	  Utils.deleteRecipe(selectedRecipe);
    	  updateRecipeList(getFilteredList(searchView.getText().toString()));
      }

      return true;
    }
    
    private void updateRecipeList(ArrayList<Recipe> l)
    {
        // Set up my listView with title and ArrayAdapter
        mAdapter = new RecipeArrayAdapter(getApplicationContext(), l);
        listView.setAdapter(mAdapter);
        recipeList = l;
    }
}
