package com.biermacht.brews.frontend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

public class StyleNotesFragment extends Fragment implements BiermachtFragment {

  private int resource = R.layout.fragment_style_notes_view;

  private BeerStyle s;

  // Views.
  View pageView;
  TextView notesView;
  TextView examplesView;
  TextView ingredientsView;
  TextView categoryView;
  TextView categorySubtitle;

  public StyleNotesFragment() {
  }

  public static StyleNotesFragment instance(BeerStyle s) {
    // Create the fragment.
    StyleNotesFragment f = new StyleNotesFragment();

    // Store the recipe in the arguments bundle.
    Bundle b = new Bundle();
    b.putParcelable(Constants.KEY_STYLE, s);
    f.setArguments(b);

    // Return the newly created fragment.
    return f;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Get arguments from stored bundle.
    this.s = getArguments().getParcelable(Constants.KEY_STYLE);

    // No options menu.
    setHasOptionsMenu(false);

    // Inflate and find views.
    pageView = inflater.inflate(resource, container, false);
    notesView = (TextView) pageView.findViewById(R.id.style_notes);
    examplesView = (TextView) pageView.findViewById(R.id.examples_view);
    categoryView = (TextView) pageView.findViewById(R.id.category_view);
    categorySubtitle = (TextView) pageView.findViewById(R.id.category_subtitle);
    ingredientsView = (TextView) pageView.findViewById(R.id.ingredients_view);

    // Set text.
    categoryView.setText("Category " + s.getCatNum() + s.getStyleLetter());
    categorySubtitle.setText(s.getCategory() + " - " + s.getStyleGuide());
    notesView.setText(s.getNotes());
    examplesView.setText(s.getExamples().replaceAll(", ", "\n"));
    ingredientsView.setText(s.getIngredients().replaceAll("\\. ", ".\n\n"));

    return pageView;
  }

  @Override
  public void handleClick(View v) {

  }

  @Override
  public void update() {

  }

  @Override
  public String name() {
    return "Notes";
  }
}
