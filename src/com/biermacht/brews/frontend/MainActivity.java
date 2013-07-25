package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseInterface;
import com.biermacht.brews.frontend.adapters.RecipeArrayAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.IngredientHandler;
import com.biermacht.brews.utils.Utils;
import java.io.*;

public class MainActivity extends Activity implements OnClickListener {
	
	// Create our stuff
	private RecipeArrayAdapter mAdapter;
	private OnItemClickListener mClickListener;
	private OnItemLongClickListener mLongClickListener;
	private TextWatcher mTextWatcher;
	private ArrayList<Recipe> recipeList;
	private Recipe selectedRecipe;
	private int searchOptions;
	private ArrayList<String> menuItems;
	
	// Poorly done globally used shit
	public static DatabaseInterface databaseInterface;
	public static IngredientHandler ingredientHandler;
	public static Boolean isFirstUse;
	
    //Declare views here
    private ListView listView; 
    private EditText searchView;
    private TextView noRecipesView;
    
    // Static menu items
    private static String EDIT_RECIPE = "Edit Recipe";
    private static String SCALE_RECIPE = "Scale Recipe";
    private static String COPY_RECIPE = "Copy Recipe";
    private static String DELETE_RECIPE = "Delete Recipe";
    private static String EDIT_FERM = "Edit Fermentation Profile";
    private static String EDIT_MASH = "Edit Mash Profile";
    private static String EXPORT_RECIPE = "Export as BeerXML";
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // SHARED PREFERENCES JUNK ALL GOES HERE!
        SharedPreferences preferences = this.getSharedPreferences("com.biermacht.brews", Context.MODE_PRIVATE);
        isFirstUse = preferences.getBoolean("com.biermacht.brews.firstUse", false);
        if (!isFirstUse)
        {
        	preferences.edit().putBoolean("com.biermacht.brews.firstUse", true).commit();
        }
                
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
			    intent.putExtra(Utils.INTENT_RECIPE_ID, recipeList.get(pos).getId());
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
    		
    	case R.id.menu_settings:
    		Intent i2 = new Intent(getApplicationContext(), SettingsActivity.class);
    		startActivity(i2);
    		break;
			
		case R.id.menu_import_recipe:
			importRecipeAlert().show();
    	}
    	return true;
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{

  	  if (requestCode == 1) 
	  {
     	if(resultCode == RESULT_OK){      
         	String path = data.getData().getPath().toString();
			
			if (path != null)
			{
				try
				{
					for (Recipe r : ingredientHandler.getRecipesFromXml(path))
					{
						r.update();
						Utils.createRecipeFromExisting(r);
					}
					
				}
				catch (IOException e)
				{
					Log.e("MainActivity", e.toString());
				}
			}	
     	}
     	if (resultCode == RESULT_CANCELED) {    
         	//Write your code on no result return 
     		}
  		}
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
        menuItems = new ArrayList<String>();
        
        // Build menu items
        menuItems.add(EDIT_RECIPE);
        if (!selectedRecipe.getType().equals(Recipe.EXTRACT))
        	menuItems.add(EDIT_MASH);
        menuItems.add(EDIT_FERM);
        menuItems.add(SCALE_RECIPE);
        menuItems.add(COPY_RECIPE);
        menuItems.add(DELETE_RECIPE);
        
        for (int i = 0; i < menuItems.size(); i++) 
        {
          menu.add(Menu.NONE, i, i, menuItems.get(i));
        }
      }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
      String selected = menuItems.get(item.getItemId());
      
      
      // Edit recipe selected
      if (selected.equals(EDIT_RECIPE))
      {
  		Intent i = new Intent(getApplicationContext(), EditRecipeActivity.class);
  		i.putExtra(Utils.INTENT_RECIPE_ID, selectedRecipe.getId());
  		startActivity(i); 
      }
      
      // Scale recipe selected
      else if (selected.equals(SCALE_RECIPE))
      {
    	  scaleAlert(selectedRecipe).show();
      }
      // Copy recipe selected
      else if (selected.equals(COPY_RECIPE))
      {
    	  selectedRecipe.setRecipeName(selectedRecipe.getRecipeName() + " - Copy");
    	  Utils.createRecipeFromExisting(selectedRecipe);
    	  updateRecipeList(getFilteredList(searchView.getText().toString()));
    	  
      }
      // Delete recipe selected
      else if (selected.equals(DELETE_RECIPE))
      {
    	  deleteAlert(selectedRecipe).show();
      }
      
      // Edit fermentation selected
      else if (selected.equals(EDIT_FERM))
      {
    		Intent i = new Intent(getApplicationContext(), EditFermentationProfileActivity.class);
      		i.putExtra(Utils.INTENT_RECIPE_ID, selectedRecipe.getId());
      		startActivity(i); 
      }
      
      // Edit fermentation selected
      else if (selected.equals(EDIT_MASH))
      {
    		Intent i = new Intent(getApplicationContext(), EditMashProfileActivity.class);
      		i.putExtra(Utils.INTENT_RECIPE_ID, selectedRecipe.getId());
      		startActivity(i); 
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
        {
        	noRecipesView.setVisibility(View.VISIBLE);
        	listView.setVisibility(View.GONE);
        }
        else
        {
        	noRecipesView.setVisibility(View.GONE);
        	listView.setVisibility(View.VISIBLE);
        }
    }

	public void onClick(View v) {
		
	}
	
	private Builder deleteAlert(final Recipe r)
	{
		return new AlertDialog.Builder(this)
			.setTitle("Confirm Delete")
			.setMessage("Do you really want to delete '" + r.getRecipeName() +"'")
			.setIcon(android.R.drawable.ic_delete)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					Utils.deleteRecipe(r);
			    	updateRecipeList(getFilteredList(searchView.getText().toString()));
				}
				
		    })
		    
		    .setNegativeButton(android.R.string.no, null);
	}
	
	private Builder scaleAlert(final Recipe r)
	{
		LayoutInflater factory = LayoutInflater.from(this);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_scale, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.new_volume_edit_text);
		
		return new AlertDialog.Builder(this)
			.setTitle("Scale Recipe")
			.setView(alertView)
			.setPositiveButton(R.string.scale, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {	
					double newVolume = Double.parseDouble(editText.getText().toString());
					Utils.scaleRecipe(r, newVolume);
			    	updateRecipeList(getFilteredList(searchView.getText().toString()));
				}
				
		    })
		    
		    .setNegativeButton(R.string.cancel, null);
	}
	
	private Builder importRecipeAlert()
	{
		return new AlertDialog.Builder(this)
			.setTitle("Import BeerXML Recipe")
			.setMessage("Press OPEN to search your device for a BeerXML recipe file to import.")
			//.setView(alertView)
			.setPositiveButton("OPEN", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					Intent i3 = new Intent(Intent.ACTION_GET_CONTENT);
					i3.setType("file/*");
					startActivityForResult(i3, 1);
				}

		    })

		    .setNegativeButton(R.string.cancel, null);
	}
}
