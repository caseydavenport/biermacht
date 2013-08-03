package com.biermacht.brews.frontend;

import android.app.*;
import android.content.Context;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.biermacht.brews.*;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.ingredient.*;
import com.biermacht.brews.recipe.*;
import com.biermacht.brews.utils.*;
import java.util.*;

import com.biermacht.brews.frontend.adapters.SpinnerAdapter;

public class AddMiscActivity extends Activity {

    // Main view - holds all the rows
    private ViewGroup mainView;

    // Alert builder
    private AlertBuilder alertBuilder;

    // Important things
    private View.OnClickListener onClickListener;

    // LayoutInflater
    LayoutInflater inflater;

    // Recipe we are editing
    private Recipe mRecipe;

    // IngredientHandler to get ingredient arrays
    IngredientHandler ingredientHandler;

    // Holds the currently selected misc, and misc being edited
    Misc misc;

    // Editable rows to display
    private Spinner miscSpinner;
    private Spinner typeSpinner;
    private Spinner useSpinner;
    private View nameView;
    private View amountView;
    private View timeView;

    // Titles from rows
    private TextView nameViewTitle;
    private TextView amountViewTitle;
    private TextView timeViewTitle;

    // Content from rows
    private TextView nameViewText;
    private TextView amountViewText;
    private TextView timeViewText;

    // Spinner array declarations
    private ArrayList<Ingredient> miscArray;
    private ArrayList<String> typeArray;
    private ArrayList<String> useArray;

    // Data storage for spinners
	private String type;
	private String use;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // Set icon as back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Ingredient Handler
        ingredientHandler = MainActivity.ingredientHandler;

        // Get the list of ingredients to show
        miscArray = new ArrayList<Ingredient>();
        miscArray.addAll(ingredientHandler.getMiscsList());

        // Get the inflater
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create alert builder
        alertBuilder = new AlertBuilder(this);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.GONE);

        // Get recipe and misc from calling activity
        long id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, Utils.INVALID_ID);
        mRecipe = Utils.getRecipeWithId(id);

        // On click listener
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /************************************************************
                 * Options for clicking on each of the editable views
                 ************************************************************/

                AlertDialog alert;
                if (v.equals(nameView))
                    alert = alertBuilder.editTextStringAlert(nameViewText, nameViewTitle).create();
                else if (v.equals(amountView))
                    alert = alertBuilder.editTextFloatAlert(amountViewText, amountViewTitle).create();
                else if (v.equals(timeView))
                    alert = alertBuilder.editTextFloatAlert(timeViewText, timeViewTitle).create();
                else
                    return; // In case its none of those views...

                // Force keyboard open and show popup
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
            }
        };

        // Initialize views and such here
        mainView = (ViewGroup) findViewById(R.id.main_layout);
        nameView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        amountView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        timeView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        miscSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        typeSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);
        useSpinner = (Spinner) inflater.inflate(R.layout.row_layout_spinner, mainView, false);

        // Set the onClickListener for each row
        nameView.setOnClickListener(onClickListener);
        amountView.setOnClickListener(onClickListener);
        timeView.setOnClickListener(onClickListener);

        /************************************************************************
         ************* Add views to main view  **********************************
         *************************************************************************/
        mainView.addView(miscSpinner);
        mainView.addView(nameView);
        mainView.addView(useSpinner);
        mainView.addView(typeSpinner);
        mainView.addView(timeView);
        mainView.addView(amountView);

        /************************************************************************
         ************* Get titles, set values   **********************************
         *************************************************************************/
        nameViewTitle = (TextView) nameView.findViewById(R.id.title);
        nameViewTitle.setText("Name");

        amountViewTitle = (TextView) amountView.findViewById(R.id.title);
        amountViewTitle.setText("Amount (L)");

        timeViewTitle = (TextView) timeView.findViewById(R.id.title);
        timeViewTitle.setText("Time (mins)");

        /************************************************************************
         ************* Get content views, set values   **************************
         *************************************************************************/
        nameViewText = (TextView) nameView.findViewById(R.id.text);
        amountViewText = (TextView) amountView.findViewById(R.id.text);
        timeViewText = (TextView) timeView.findViewById(R.id.text);

        // Set up spinner
        IngredientSpinnerAdapter adapter = new IngredientSpinnerAdapter(this, miscArray, "Misc Selector");
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        miscSpinner.setAdapter(adapter);
        miscSpinner.setSelection(0);    

		// Set up type spinner
        typeArray = new ArrayList<String>();
        typeArray.add(Misc.TYPE_FINING);
        typeArray.add(Misc.TYPE_FLAVOR);
        typeArray.add(Misc.TYPE_HERB);
        typeArray.add(Misc.TYPE_SPICE);
        typeArray.add(Misc.TYPE_WATER_AGENT);
        typeArray.add(Misc.TYPE_OTHER);
		
        SpinnerAdapter miscTypeAdapter = new SpinnerAdapter(this, typeArray, "Misc Type");
        miscTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typeSpinner.setAdapter(miscTypeAdapter);
        typeSpinner.setSelection(0);

		// Set up use spinner
		useArray = new ArrayList<String>();
        useArray.add(Misc.USE_BOIL);
        useArray.add(Misc.USE_BOTTLING);
        useArray.add(Misc.USE_MASH);
        useArray.add(Misc.USE_PRIMARY);
        useArray.add(Misc.USE_SECONDARY);
		
		SpinnerAdapter miscUseAdapter = new SpinnerAdapter(this, useArray, "Misc Use");
		miscUseAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        useSpinner.setAdapter(miscUseAdapter);
		useSpinner.setSelection(0);

        // Handle misc selector here
        miscSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					misc = (Misc) miscArray.get(position);

                    nameViewText.setText(misc.getName());

					timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));
					amountViewText.setText(String.format("%2.2f", misc.getDisplayAmount()));
					
					typeSpinner.setSelection(typeArray.indexOf(misc.getMiscType()));
					useSpinner.setSelection(useArray.indexOf(misc.getUse()));
					
					amountViewTitle.setText("Amount (" + misc.getDisplayUnits() + ")");
					timeViewTitle.setText(misc.getUse() + " time");
				}

				public void onNothingSelected(AdapterView<?> parentView)
                {
				}

			});   

		// Handle type selector here
        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
					type = typeArray.get(position);
				}

				public void onNothingSelected(AdapterView<?> parentView)
                {
				}

			});

		// Handle use selector here
        useSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
					use = useArray.get(position);

                    if (use.equals(Misc.USE_BOTTLING))
                    {
                        timeView.setVisibility(View.GONE);
                    }
                    else
                    {
                        timeView.setVisibility(View.VISIBLE);
                        String units = "";
                        if (use.equals(Misc.USE_PRIMARY)||use.equals(Misc.USE_SECONDARY))
                            units = Units.DAYS;
                        else if (use.equals(Misc.USE_BOIL)||use.equals(Misc.USE_MASH))
                            units = Units.MINUTES;
                        else
                            units = Units.MINUTES;

                        timeViewTitle.setText(use + " Time" + " (" + units + ")");
                    }
				}

				public void onNothingSelected(AdapterView<?> parentView)
                {
				}

			});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_add_miscs, menu);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId())
		{
            case android.R.id.home:
        		finish();
        		return true; 
        }
        return super.onOptionsItemSelected(item);
    }

	public void onClick(View v) {
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.submit_button)
		{
            boolean readyToGo = true;

            try {
                String name = nameViewText.getText().toString();
                int time = Integer.parseInt(timeViewText.getText().toString());
                int endTime = mRecipe.getBoilTime();
                int startTime = endTime - time;
                double amount = Double.parseDouble(amountViewText.getText().toString());

                if (time > mRecipe.getBoilTime())
                    time = mRecipe.getBoilTime();

                misc.setName(name);
                misc.setTime(time);
                misc.setStartTime(startTime);
                misc.setEndTime(endTime);
                misc.setDisplayAmount(amount, misc.getDisplayUnits());
                misc.setMiscType(type);
                misc.setUse(use);
            } catch (Exception e)
            {
                Log.d("AddMiscActivity", "Exception on submit: " + e.toString());
                readyToGo = false;
            }

            if (readyToGo)
            {
                mRecipe.addIngredient(misc);
                mRecipe.update();
                Utils.updateRecipe(mRecipe);
                finish();
            }
		}

		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
	}
}
