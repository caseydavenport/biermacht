package com.biermacht.brews.frontend;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Grain;
import com.biermacht.brews.recipe.Ingredient;
import com.biermacht.brews.utils.Utils;

public class AddGrainActivity extends Activity implements OnClickListener {
	
	private Spinner grainTypeSpinner;
	private EditText grainNameEditText;
	private EditText grainColorEditText;
	private EditText grainGravEditText;
	private EditText grainWeightEditText;
	private Button submitButton;
	private ArrayList<String> grainTypeArray = Utils.getFermentablesStringList();
	private String grainType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grain);
        
        // Initialize views and such here
        grainNameEditText = (EditText) findViewById(R.id.grain_name_edit_text);
        grainColorEditText = (EditText) findViewById(R.id.grain_color_edit_text);
        grainGravEditText = (EditText) findViewById(R.id.grain_grav_edit_text);
        grainWeightEditText = (EditText) findViewById(R.id.grain_weight_edit_text);
        submitButton = (Button) findViewById(R.id.new_grain_submit_button);
        
        // Set up grain type spinner
        grainTypeSpinner = (Spinner) findViewById(R.id.grain_type_spinner);
        SpinnerAdapter<String> adapter = new SpinnerAdapter<String>(this, grainTypeArray);  
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        grainTypeSpinner.setAdapter(adapter);
        grainTypeSpinner.setSelection(0);    
        
        // Handle beer type selector here
        grainTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Grain grainObj = new Grain("");
            	grainType = grainTypeArray.get(position);
                
                for (Ingredient i : Utils.getFermentablesList())
                {
                	if (grainType.equals(i.toString()))
                		grainObj = (Grain) i;
                }
            	
                grainNameEditText.setText(grainType);
                grainColorEditText.setText(grainObj.getLovibondColor() +"");
                grainGravEditText.setText(grainObj.getGravity() +"");
                grainWeightEditText.setText(5 +"");
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });   
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
    }

	public void onClick(View v) {
		if (v.getId() == R.id.new_grain_submit_button)
		{
			String grainName = grainNameEditText.getText().toString();
			double color = Double.parseDouble(grainColorEditText.getText().toString());
			double grav = Double.parseDouble(grainGravEditText.getText().toString());
			double weight = Double.parseDouble(grainWeightEditText.getText().toString());
			
			Grain g = new Grain(grainName);
			g.setLovibondColor(color);
			g.setGravity(grav);
			g.setWeight(weight);
			
			finish();
		}
	}
}
