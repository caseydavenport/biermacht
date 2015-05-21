package com.biermacht.brews.frontend.IngredientActivities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AddEditIngredientActivity extends AddEditActivity {

  // Editable rows to display
  public View searchableListView;

  // Titles from rows
  public TextView searchableListViewTitle;

  // Content from rows
  public TextView searchableListViewText;

  // Listener for searchable list
  AdapterView.OnItemClickListener searchableListListener;

  // Method called in subclass to configure the searchable list listener.
  public abstract void configureSearchableListListener();

  // Store the current dialog
  public AlertDialog dialog;

  // Textwatcher for searchable list view
  TextWatcher textWatcher;

  // Filtered lsit from which the ingredient was chosen.
  ArrayList<Ingredient> filteredList;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize views and such here
    searchableListView = inflater.inflate(R.layout.row_layout_edit_text, mainView, false);

    // Initialize dialog to null
    dialog = null;

    /************************************************************************
     ************* Add views *************************************************
     *************************************************************************/
    this.registerViews(Arrays.asList(searchableListView));
    this.setViews(Arrays.asList(searchableListView, nameView, timeView, amountView));

    /************************************************************************
     ************* Get titles, set values   **********************************
     *************************************************************************/
    searchableListViewTitle = (TextView) searchableListView.findViewById(R.id.title);
    searchableListViewTitle.setText("Select");

    // Text views
    searchableListViewText = (TextView) searchableListView.findViewById(R.id.text);

    // Configure the searchable list listener
    configureSearchableListListener();

    textWatcher = new TextWatcher() {
      public void afterTextChanged(Editable s) {
      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Clear previous list
        filteredList = new ArrayList<Ingredient>();
        getList();

        // Fix up the search string
        s = s.toString().trim();

        // If the name or description matches, add it to the list.
        for (Ingredient i : ingredientList) {
          if (i.getType().equals(Ingredient.PLACEHOLDER)) {
            filteredList.add(0, i);
          }
          else if (i.getName().toLowerCase().contains(s.toString().toLowerCase())) {
            filteredList.add(i);
          }
          else if (i.getShortDescription().toLowerCase().contains(s.toString().toLowerCase())) {
            filteredList.add(i);
          }
        }

        adapter.clear();
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();
      }
    };

    // Initialize filtered list
    filteredList = new ArrayList<Ingredient>();
    filteredList.addAll(ingredientList);
  }

  public void setInitialSearchableListSelection() {
    // Set to second item (first is the "create new" placeholder)
    searchableListListener.onItemClick(null, null, 1, 1);
  }

  @Override
  public void onMissedClick(View v) {
    if (v.equals(searchableListView)) {
      dialog = alertBuilder.searchableListAlert(searchableListViewTitle, searchableListViewText, adapter, ingredientList, searchableListListener, textWatcher).create();
    }
    else {
      return;
    }
    dialog.show();
  }

  public void cancelDialog() {
    if (dialog != null) {
      dialog.cancel();
      dialog = null;
    }
  }

  // Helper which returns True if we're adding this ingredient to a recipe,
  // False if we're adding it to the custom DB.
  public boolean haveRecipe() {
    return mRecipe.getId() != Constants.MASTER_RECIPE_ID;
  }
}
