package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.recipe.*;

import android.widget.*;

public class EditRecipeActivity extends Activity implements OnClickListener {

	// Data entry view declarations
	//private Spinner beerStyleSpinner;
	//private Spinner beerTypeSpinner;
    private ViewGroup mainView;
	//private Spinner mashProfileSpinner;

    // Important things
    private OnClickListener onClickListener;
    private WindowManager windowManager;

    // Rows to be displayed
	private View recipeNameView;
	private View timeView;
	private View efficiencyView;
	private View batchSizeView;
	private View boilSizeView;
    private View measuredOGView;
    private View measuredFGView;

    // Titles
    private TextView recipeNameViewTitle;
    private TextView timeViewTitle;
    private TextView efficiencyViewTitle;
    private TextView batchSizeViewTitle;
    private TextView boilSizeViewTitle;
    private TextView measuredOGViewTitle;
    private TextView measuredFGViewTitle;

    // Contents
    private TextView recipeNameViewText;
    private TextView timeViewText;
    private TextView efficiencyViewText;
    private TextView batchSizeViewText;
    private TextView boilSizeViewText;
    private TextView measuredOGViewText;
    private TextView measuredFGViewText;

    // LayoutInflater
    LayoutInflater inflater;
	
	// Data storage declarations
	private BeerStyle style;
	private MashProfile profile;
	private String type;
	private double efficiency;
	
	// Recipe we are editing
	private Recipe mRecipe;
	
	// Spinner array declarations
	private ArrayList<BeerStyle> beerStyleArray;
	private ArrayList<String> beerTypeArray;
	private ArrayList<MashProfile> mashProfileArray;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // On click listener
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /************************************************************
                 * Options for clicking on each of the editable views
                 ************************************************************/

                AlertDialog alert;
                if (v.equals(recipeNameView))
                    alert = editTextStringAlert(recipeNameViewText, recipeNameViewTitle).create();
                else if (v.equals(timeView))
                    alert = editTextIntegerAlert(timeViewText, timeViewTitle).create();
                else if (v.equals(efficiencyView))
                    alert = editTextFloatAlert(efficiencyViewText, efficiencyViewTitle).create();
                else if (v.equals(batchSizeView))
                    alert = editTextFloatAlert(batchSizeViewText, batchSizeViewTitle).create();
                else if (v.equals(boilSizeView))
                    alert = editTextFloatAlert(boilSizeViewText, boilSizeViewTitle).create();
                else if (v.equals(measuredFGView))
                    alert = editTextFloatAlert(measuredFGViewText, measuredFGViewTitle).create();
                else if (v.equals(measuredOGView))
                    alert = editTextFloatAlert(measuredOGViewText, measuredOGViewTitle).create();
                else
                    return; // In case its none of those views...

                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                alert.show();
            }
        };

        // Window manager
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        // Get the inflater
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        // Get recipe from calling activity
        long id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, 0);
        mRecipe = Utils.getRecipeWithId(id);
        style = mRecipe.getStyle();
        profile = mRecipe.getMashProfile();
        type = mRecipe.getType();
        efficiency = mRecipe.getEfficiency();
        
        // Initialize views and stuff
        mainView = (ViewGroup) findViewById(R.id.main_layout);
        recipeNameView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        timeView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        efficiencyView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        batchSizeView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);;
        boilSizeView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        measuredFGView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);
        measuredOGView = (View) inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

        // Set onClickListeners
        recipeNameView.setOnClickListener(onClickListener);
        timeView.setOnClickListener(onClickListener);
        efficiencyView.setOnClickListener(onClickListener);
        batchSizeView.setOnClickListener(onClickListener);
        boilSizeView.setOnClickListener(onClickListener);
        measuredFGView.setOnClickListener(onClickListener);
        measuredOGView.setOnClickListener(onClickListener);

        // Add views to main view
        mainView.addView(recipeNameView);
        mainView.addView(inflater.inflate(R.layout.divider, mainView, false));
        mainView.addView(timeView);
        mainView.addView(inflater.inflate(R.layout.divider, mainView, false));
        mainView.addView(efficiencyView);
        mainView.addView(inflater.inflate(R.layout.divider, mainView, false));
        mainView.addView(batchSizeView);
        mainView.addView(inflater.inflate(R.layout.divider, mainView, false));
        mainView.addView(boilSizeView);
        mainView.addView(inflater.inflate(R.layout.divider, mainView, false));
        mainView.addView(measuredOGView);
        mainView.addView(inflater.inflate(R.layout.divider, mainView, false));
        mainView.addView(measuredFGView);
        mainView.addView(inflater.inflate(R.layout.divider, mainView, false));


        /************************************************************************
        ************* Get titles, set values   **********************************
        *************************************************************************/
        recipeNameViewTitle = (TextView) recipeNameView.findViewById(R.id.title);
        recipeNameViewTitle.setText("Recipe Name");

        timeViewTitle = (TextView) timeView.findViewById(R.id.title);
        timeViewTitle.setText("Time (mins)");

        efficiencyViewTitle = (TextView) efficiencyView.findViewById(R.id.title);
        efficiencyViewTitle.setText("Predicted Efficiency");

        batchSizeViewTitle = (TextView) batchSizeView.findViewById(R.id.title);
        batchSizeViewTitle.setText("Batch Size (gal)");

        boilSizeViewTitle = (TextView) boilSizeView.findViewById(R.id.title);
        boilSizeViewTitle.setText("Boil Size (gal)");

        measuredFGViewTitle = (TextView) measuredFGView.findViewById(R.id.title);
        measuredFGViewTitle.setText("Measured Final Gravity");

        measuredOGViewTitle = (TextView) measuredOGView.findViewById(R.id.title);
        measuredOGViewTitle.setText("Measured Original Gravity");

        /************************************************************************
         ************* Get content views, set values   **************************
         *************************************************************************/
        recipeNameViewText = (TextView) recipeNameView.findViewById(R.id.text);
        recipeNameViewText.setText(mRecipe.getRecipeName());

        timeViewText = (TextView) timeView.findViewById(R.id.text);
        timeViewText.setText(String.format("%d", mRecipe.getBoilTime()));

        efficiencyViewText = (TextView) efficiencyView.findViewById(R.id.text);
        efficiencyViewText.setText(String.format("%2.2f", mRecipe.getEfficiency()));

        batchSizeViewText = (TextView) batchSizeView.findViewById(R.id.text);
        batchSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBatchSize()));

        boilSizeViewText = (TextView) boilSizeView.findViewById(R.id.text);
        boilSizeViewText.setText(String.format("%2.2f", mRecipe.getDisplayBoilSize()));

        measuredFGViewText = (TextView) measuredFGView.findViewById(R.id.text);
        measuredFGViewText.setText(String.format("%2.3f", mRecipe.getMeasuredFG()));

        measuredOGViewText = (TextView) measuredOGView.findViewById(R.id.text);
        measuredOGViewText.setText(String.format("%2.3f", mRecipe.getMeasuredOG()));

        /**
        //Arraylist of beer types
        beerStyleArray = MainActivity.ingredientHandler.getStylesList();
		mashProfileArray = MainActivity.ingredientHandler.getMashProfileList();
		
		// If it doesn't contain the current recipes style / profile,
		// then it is custom and we add it to the list.
		if(!beerStyleArray.contains(mRecipe.getStyle()))
			beerStyleArray.add(mRecipe.getStyle());
		if(!mashProfileArray.contains(mRecipe.getMashProfile()))
			mashProfileArray.add(mRecipe.getMashProfile());
        
        // Set up beer type spinner
        beerStyleSpinner = (Spinner) findViewById(R.id.beer_type_spinner);
        BeerStyleSpinnerAdapter adapter = new BeerStyleSpinnerAdapter(this, beerStyleArray);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        beerStyleSpinner.setAdapter(adapter);
		
		// Set up mash profile spinner
		mashProfileSpinner = (Spinner) findViewById(R.id.mash_profile_spinner);
		MashProfileSpinnerAdapter profAdapter = new MashProfileSpinnerAdapter(this, mashProfileArray);
        profAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		mashProfileSpinner.setAdapter(profAdapter);
        
        // Determine the correct selection for the style spinner
        int pos = 0;
        for (BeerStyle b : beerStyleArray)
        {
        	if (b.equals(mRecipe.getStyle()))
        		break;
        	pos++;
			
			if (pos > beerStyleArray.size())
				pos = 0;
        }
        
        beerStyleSpinner.setSelection(pos);
        
        // Set up brew type spinner
        beerTypeSpinner = (Spinner) findViewById(R.id.brew_type_spinner);
        beerTypeArray = new ArrayList<String>();
        beerTypeArray.add("Extract");
        beerTypeArray.add("Partial Mash");
        beerTypeArray.add("All Grain");
        SpinnerAdapter beerTypeAdapter = new SpinnerAdapter(this, beerTypeArray);
        beerTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        beerTypeSpinner.setAdapter(beerTypeAdapter);

        // Determine the correct selection for the brew type spinner
        pos = 0;
        for (String s : beerTypeArray)
        {
        	if (s.equals(mRecipe.getType()))
        		break;
        	pos++;
        }
        beerTypeSpinner.setSelection(pos);
		
		// Determine the correct selection for the mash profile spinner
        pos = 0;
        for (MashProfile p : mashProfileArray)
        {
        	if (p.equals(mRecipe.getMashProfile()))
        		break;
        	pos++;
        }
		mashProfileSpinner.setSelection(pos);
        
        // Handle beer style selector here
        beerStyleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                style = beerStyleArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // Blag
            }

        });
		
		// Handle mash profile selector here
        mashProfileSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				profile = mashProfileArray.get(position);
			}

			public void onNothingSelected(AdapterView<?> parentView) {
				// Blag
			}

		});
        
        // Handle beer type selector here
        beerTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                type = beerTypeArray.get(position);
                
                if(type.equals(Recipe.EXTRACT))
                {
                	mashProfileSpinner.setVisibility(View.GONE);
					mashProfileTitle.setVisibility(View.GONE);
                }
                else
                {
					mashProfileSpinner.setVisibility(View.VISIBLE);
					mashProfileTitle.setVisibility(View.VISIBLE);
				}

            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // Blag
            }

        });
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_new_recipe, menu);
        return true;
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
			String recipeName = "Unnamed Brew";
			Integer boilTime = 1;
			float batchSize = 5;
			float boilSize = 5;
            float measuredOg = 1;
            float measuredFg = 1;
			
			try {
			    recipeName = recipeNameViewText.getText().toString();
				boilTime = Integer.parseInt(timeViewText.getText().toString());
				efficiency = Float.parseFloat(efficiencyViewText.getText().toString());
				batchSize = Float.parseFloat(batchSizeViewText.getText().toString());
				boilSize = Float.parseFloat(boilSizeViewText.getText().toString());
                measuredOg = Float.parseFloat(measuredOGViewText.getText().toString());
                measuredFg = Float.parseFloat(measuredFGViewText.getText().toString());
			}
			catch (Exception e)
			{
				readyToGo = false;
			}
			
			if (recipeName.isEmpty())
				readyToGo = false;
			if (efficiency > 100)
				readyToGo = false;
			
			if (readyToGo)
			{
				mRecipe.setRecipeName(recipeName);
				mRecipe.setVersion(Utils.getXmlVersion());
				mRecipe.setType(type);
				mRecipe.setStyle(style);
				mRecipe.setMashProfile(profile);
				mRecipe.setDisplayBatchSize(batchSize);
				mRecipe.setDisplayBoilSize(boilSize);
				mRecipe.setBoilTime(boilTime);
				mRecipe.setEfficiency(efficiency);
				mRecipe.setBatchTime(1);
                mRecipe.setMeasuredFG(measuredFg);
                mRecipe.setMeasuredOG(measuredOg);
				
				mRecipe.update();
				Utils.updateRecipe(mRecipe);
				finish();
			}
		}
	}

    // Builders for all of the alerts
    private AlertDialog.Builder editTextStringAlert(final TextView text, final TextView title)
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_edit_text_string, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.edit_text);
        editText.setText(text.getText().toString());

        return new AlertDialog.Builder(this)
                .setTitle(title.getText().toString())
                .setView(alertView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        text.setText(editText.getText().toString());
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }

    private AlertDialog.Builder editTextIntegerAlert(final TextView text, final TextView title)
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_edit_text_integer, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.edit_text);
        editText.setText(text.getText().toString());

        return new AlertDialog.Builder(this)
                .setTitle(title.getText().toString())
                .setView(alertView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        text.setText(editText.getText().toString());
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }

    private AlertDialog.Builder editTextFloatAlert(final TextView text, final TextView title)
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_edit_text_float_2_4, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.edit_text);
        editText.setText(text.getText().toString());

        return new AlertDialog.Builder(this)
                .setTitle(title.getText().toString())
                .setView(alertView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        text.setText(editText.getText().toString());
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }
}
