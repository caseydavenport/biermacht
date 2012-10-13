package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseInterface;
import com.biermacht.brews.frontend.adapters.RecipeArrayAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;

public class MainActivity extends Activity implements OnClickListener {
	
	// Create our stuff
	private RecipeArrayAdapter mAdapter;
	private OnItemClickListener mClickListener;
	private OnItemLongClickListener mLongClickListener;
	private TextWatcher mTextWatcher;
	private ArrayList<Recipe> recipeList;
	private Recipe selectedRecipe;
	private int searchOptions;
	
	// Poorly done globally used shit
	public static DatabaseInterface databaseInterface;
	public static IngredientHandler ingredientHandler;
	
    //Declare views here
    private ListView listView; 
    private EditText searchView;
    private TextView noRecipesView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                
        // Initialize views
        listView = (ListView) findViewById(R.id.recipe_list);
        searchView = (EditText) findViewById(R.id.search_bar);
        noRecipesView = (TextView) findViewById(R.id.no_recipes_view);
        
        // Declare my database interface 
        databaseInterface = new DatabaseInterface(getApplicationContext());
        databaseInterface.open();
        
        // Declare my ingredient handler
        ingredientHandler = new IngredientHandler(getApplicationContext());

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
        
        // Set default search options to search by all criteria
        searchOptions = 1;
        
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
    		
    		// Search options decoder
    		// 1 --> Filter by all the following
    		// 2 --> Filter by name only
    		// 3 --> Filter by type only
    		
    		boolean searchByName = (searchOptions == 1 || searchOptions == 2);
    		boolean searchByType = (searchOptions == 1 || searchOptions == 3);
    		
    		// If the recipe name matches
    		if(r.getRecipeName().toLowerCase().contains(s.toLowerCase()) && searchByName)
    		{
    			filteredList.add(r);
    		}
    		
    		// If the recipe type matches
    		else if(r.getStyle().toString().toLowerCase().contains(s.toLowerCase()) && searchByType )
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
        String[] menuItems = {"Edit Recipe", "Copy Recipe", "Delete Recipe"};
        
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
  		i.putExtra("biermacht.brews.recipeID", selectedRecipe.getId());
  		startActivity(i); 
      }
      // Copy recipe selected
      else if (menuItemIndex == 1)
      {
    	  selectedRecipe.setRecipeName(selectedRecipe.getRecipeName() + " - Copy");
    	  Utils.createRecipeFromExisting(selectedRecipe);
    	  updateRecipeList(getFilteredList(searchView.getText().toString()));
    	  
      }
      // Delete recipe selected
      else if (menuItemIndex == 2)
      {
    	  deleteAlert(selectedRecipe).show();
      }

      return true;
    }
    
    private void updateRecipeList(ArrayList<Recipe> l)
    {
        // Set up my listView with title and ArrayAdapter
        mAdapter = new RecipeArrayAdapter(getApplicationContext(), l);
        listView.setAdapter(mAdapter);
        recipeList = l;
        
        if (l.size() == 0)
        	noRecipesView.setVisibility(View.VISIBLE);
        else
        	noRecipesView.setVisibility(View.GONE);
    }

	public void onClick(View v) {
		Log.e("MAINACTIVITY", "VIEW CLICKED");
		if (v.getId() == R.id.no_recipes_view)
		{
    		Intent i = new Intent(getApplicationContext(), AddNewRecipeActivity.class);
    		startActivity(i);
		}
	}
	
	private Builder deleteAlert(final Recipe r)
	{
		return new AlertDialog.Builder(this)
			.setTitle("Confirm Delete")
			.setMessage("Do you really want to delete '" + r.getRecipeName() +"'")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					Utils.deleteRecipe(r);
			    	updateRecipeList(getFilteredList(searchView.getText().toString()));
				}
				
		    })
		    
		    .setNegativeButton(android.R.string.no, null);
	}
}
