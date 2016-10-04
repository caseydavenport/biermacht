package com.biermacht.brews.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.AddEditActivity;
import com.biermacht.brews.utils.Constants;

public class EditRecipeNotesActivity extends AddEditActivity {

  // Notes views
  private View notesView;
  private EditText notesViewEditText;
  private View tasteNotesView;
  private EditText tasteNotesViewEditText;

  // Title bars
  public View notesTitleView;
  public TextView notesTitleViewText;
  public ImageButton notesTitleViewButton;

  public View tasteNotesTitleView;
  public TextView tasteNotesTitleViewText;
  public ImageButton tasteNotesTitleViewButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Disable delete button for this view
    findViewById(R.id.delete_button).setVisibility(View.GONE);

    // Change submit button to say "save"
    submitButton.setText("Save");

    // Create notes edit text
    notesView = inflater.inflate(R.layout.view_recipe_notes, mainView, false);
    notesViewEditText = (EditText) notesView.findViewById(R.id.edit_text);

    tasteNotesView = inflater.inflate(R.layout.view_recipe_notes, mainView, false);
    tasteNotesViewEditText = (EditText) tasteNotesView.findViewById(R.id.edit_text);

    // Set values
    notesViewEditText.setText(mRecipe.getNotes());
    tasteNotesViewEditText.setText(mRecipe.getTasteNotes());

    // Title bars
    notesTitleView = inflater.inflate(R.layout.view_title, mainView, false);
    notesTitleViewText = (TextView) notesTitleView.findViewById(R.id.title);
    notesTitleViewButton = (ImageButton) notesTitleView.findViewById(R.id.button);

    tasteNotesTitleView = inflater.inflate(R.layout.view_title, mainView, false);
    tasteNotesTitleViewText = (TextView) tasteNotesTitleView.findViewById(R.id.title);
    tasteNotesTitleViewButton = (ImageButton) tasteNotesTitleView.findViewById(R.id.button);

    // Set button to be invisible
    notesTitleViewButton.setVisibility(View.INVISIBLE);
    tasteNotesTitleViewButton.setVisibility(View.INVISIBLE);

    // Set title text
    notesTitleViewText.setText("Recipe notes");
    tasteNotesTitleViewText.setText("Taste notes");

    // Remove all views
    mainView.removeAllViews();

    // Add views
    mainView.addView(notesTitleView);
    mainView.addView(notesView);
    mainView.addView(tasteNotesTitleView);
    mainView.addView(tasteNotesView);
  }

  @Override
  public void getValuesFromIntent() {
    super.getValuesFromIntent();
  }

  @Override
  public void setInitialSpinnerSelection() {

  }

  @Override
  public void getList() {

  }

  @Override
  public void createSpinner() {

  }

  @Override
  public void onMissedClick(View v) {

  }

  @Override
  public void configureSpinnerListener() {

  }

  @Override
  public void acquireValues() throws Exception {
    super.acquireValues();
    String notes = notesViewEditText.getText().toString();
    String tasteNotes = tasteNotesViewEditText.getText().toString();

    mRecipe.setNotes(notes);
    mRecipe.setTasteNotes(tasteNotes);
  }

  @Override
  public void onFinished() {
    mRecipe.save(getApplicationContext());
    finish();
  }

  @Override
  public void onCancelPressed() {
    setResult(Constants.RESULT_CANCELED, new Intent());
    finish();
  }

  @Override
  public void onDeletePressed() {

  }
}
