package com.biermacht.brews.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;

public class EditMashStepActivity extends AddMashStepActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Disable delete button for this view
        findViewById(R.id.delete_button).setVisibility(View.VISIBLE);

        // Change submit button to say "save"
        submitButton.setText("Save");
    }

    @Override
    public void onRecipeNotFound()
    {
        Log.d("EditMashStepActivity", "Recipe not needed - using blank recipe");
        mRecipe = new Recipe();
    }

    @Override
    public void getValuesFromIntent()
    {
        super.getValuesFromIntent();
        stepId = getIntent().getLongExtra(Constants.KEY_MASH_STEP_ID, Constants.INVALID_ID);

        // Create mash step
        step = getIntent().getParcelableExtra(Constants.KEY_MASH_STEP);
        step.setRecipe(mRecipe);
    }

    @Override
    public void setInitialSpinnerSelection()
    {
        spinnerView.setSelection(stepTypeArray.indexOf(step.getType()));
    }

    @Override
    public void onFinished()
    {
        Intent result = new Intent();
        result.putExtra(Constants.KEY_MASH_STEP, step);
        result.putExtra(Constants.KEY_MASH_STEP_ID, stepId);
        setResult(Constants.RESULT_OK, result);
        finish();
    }

    @Override
    public void onCancelPressed()
    {
        setResult(Constants.RESULT_CANCELED, new Intent());
        finish();
    }

    @Override
    public void onDeletePressed()
    {
        Intent result = new Intent();
        result.putExtra(Constants.KEY_MASH_STEP, step);
        result.putExtra(Constants.KEY_MASH_STEP_ID, stepId);
        setResult(Constants.RESULT_DELETED, result);
        finish();
    }
}
