package com.biermacht.brews.frontend;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.biermacht.brews.DragDropList.DragSortController;
import com.biermacht.brews.DragDropList.DragSortListView;
import com.biermacht.brews.R;
import com.biermacht.brews.exceptions.RecipeNotFoundException;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.frontend.adapters.*;

public class EditMashProfileActivity extends Activity implements OnClickListener {

	// Data entry view declarations
	private Spinner mashProfileSpinner;
	private EditText nameEditText;
	private EditText effEditText;
	
	// Data storage declarations
	private double efficiency;
	private String name;

	// Recipe
	private Recipe mRecipe;
	private MashProfile mashProfile;
	
	// Arrays
	private ArrayList<MashProfile> mashProfileArray;

    // DragDrop view suff
    private DragSortListView listView;
    ArrayAdapter<String> adapter;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener()
    {
        @Override
        public void drop(int from, int to)
        {
            if (from != to)
            {
                String item = adapter.getItem(from);
                adapter.remove(item);
                adapter.insert(item, to);
            }
        }
    };

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener()
    {
        @Override
        public void remove(int which)
        {
            adapter.remove(adapter.getItem(which));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mash_profile);

        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Constants.INTENT_RECIPE_ID, Constants.INVALID_ID);

        // Set icon as back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Acquire recipe
        try
        {
            mRecipe = Database.getRecipeWithId(id);
        }
        catch (RecipeNotFoundException e)
        {
            e.printStackTrace();
            finish();
        }

        // Acquire profile
        mashProfile = mRecipe.getMashProfile();
		
		// Initialize data containers
		name = mashProfile.getName();
		efficiency = mRecipe.getEfficiency();

        // Initialize views and stuff
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        effEditText = (EditText) findViewById(R.id.efficiency_edit_text);
        listView = (DragSortListView) findViewById(R.id.listview);

        // Drag list view shit
        String[] names = {"Name", "This", "an", "is", "awful"};
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(names));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setDropListener(onDrop);
        listView.setRemoveListener(onRemove);

        DragSortController controller = new DragSortController(listView);
        controller.setDragHandleId(R.id.row_icon);
        //controller.setClickRemoveId(R.id.);
        controller.setRemoveEnabled(true);
        controller.setSortEnabled(true);
        controller.setDragInitMode(1);
        //controller.setRemoveMode(removeMode);

        listView.setFloatViewManager(controller);
        listView.setOnTouchListener(controller);
        listView.setDragEnabled(true);
     
        // Default values
        effEditText.setText(String.format("%2.2f", mRecipe.getEfficiency()));
     	nameEditText.setText(mashProfile.getName());
		
        //Arraylist of Profiles
        mashProfileArray = MainActivity.ingredientHandler.getMashProfileList();

		// If it doesn't contain the current recipes profile,
		// then it is custom and we add it to the list.
		if(!mashProfileArray.contains(mRecipe.getMashProfile()))
			mashProfileArray.add(mRecipe.getMashProfile());

		// Set up mash profile spinner
		mashProfileSpinner = (Spinner) findViewById(R.id.mash_profile_spinner);
		MashProfileSpinnerAdapter profAdapter = new MashProfileSpinnerAdapter(this, mashProfileArray);
        profAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		mashProfileSpinner.setAdapter(profAdapter);

		// Determine the correct selection for the mash profile spinner
        int pos = 0;
        for (MashProfile p : mashProfileArray)
        {
        	if (p.equals(mRecipe.getMashProfile()))
        		break;
        	pos++;
        }
		mashProfileSpinner.setSelection(pos);

		// Handle mash profile selector here
        mashProfileSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					mashProfile = mashProfileArray.get(position);
					nameEditText.setText(mashProfile.getName());
				}

				public void onNothingSelected(AdapterView<?> parentView) {
					// Blag
				}

			});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	public void onClick(View v) {

		// Cancel Button Pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}

		// Submit Button pressed
		if (v.getId() == R.id.submit_button)
		{
			boolean readyToGo = true;	
			try {
				efficiency = Float.parseFloat(effEditText.getText().toString());
			    name = nameEditText.getText().toString();
			}
			catch (Exception e)
			{
				readyToGo = false;
			}

			if (name.isEmpty())
				readyToGo = false;
			if (efficiency > 100)
				readyToGo = false;

			if (readyToGo)
			{
				mashProfile.setName(name);
				
				mRecipe.setEfficiency(efficiency);
				mRecipe.setMashProfile(mashProfile);
				
				mRecipe.update();
                Database.updateRecipe(mRecipe);
				finish();
			}
		}
	}
}
