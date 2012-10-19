package com.biermacht.brews.frontend;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Yeast;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Utils;

public class EditYeastActivity extends Activity implements OnClickListener {
	
	private EditText yeastNameEditText;
	private EditText amountEditText;
	private EditText attenuationEditText;
	private TextView bestForTextView;
	private Recipe mRecipe;
	Yeast yeast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_yeast);

        // Get recipe from calling activity
        long id = getIntent().getLongExtra("com.biermacht.brews.recipeID", 0);
        long yeastId = getIntent().getLongExtra("com.biermacht.brews.grainID", 0);
        mRecipe = MainActivity.databaseInterface.getRecipeWithId(id);
        yeast = (Yeast) Utils.getIngredientWithId(yeastId);
        
        // Initialize views and such here
        yeastNameEditText = (EditText) findViewById(R.id.name_edit_text);
        amountEditText = (EditText) findViewById(R.id.amount_edit_text);
        attenuationEditText = (EditText) findViewById(R.id.attenuation_edit_text);
        bestForTextView = (TextView) findViewById(R.id.best_for_text);
        
        // Set some values
        bestForTextView.setText(yeast.getBestFor());
        yeastNameEditText.setText(yeast.getName());
        amountEditText.setText(yeast.getAmount() + "");
        attenuationEditText.setText(yeast.getAttenuation() + "");
 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
    }

	public void onClick(View v) {
		// if "SUBMIT" button pressed
		if (v.getId() == R.id.submit_button)
		{
			String name = yeastNameEditText.getText().toString();
			double attenuation = Double.parseDouble(attenuationEditText.getText().toString());
			double amount = Double.parseDouble(amountEditText.getText().toString());
			
			Yeast y = yeast;
			y.setName(name);
			y.setAttenuation(attenuation);
			y.setAmount(amount);
			
			Utils.updateIngredient(y);
			mRecipe = Utils.getRecipeWithId(mRecipe.getId());
			mRecipe.update();
			Utils.updateRecipe(mRecipe);
			
			
			finish();
		}
		
		// If "DELETE" button pressed
		if (v.getId() == R.id.delete_button)
		{
			Utils.deleteIngredient(yeast);
			
			finish();
		}
		
		// if "CANCEL" button pressed
		if (v.getId() == R.id.cancel_button)
		{
			finish();
		}
	}
}
