package com.biermacht.brews.frontend.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.utils.ColorHandler;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

public class StyleParametersFragment extends Fragment implements BiermachtFragment {

  private int resource = R.layout.fragment_style_parameters_view;

  private BeerStyle s;

  // Views.
  View pageView;
  TextView ogText;
  TextView fgText;
  TextView abvText;
  TextView ibuText;
  TextView carbonationText;
  ImageView minColor;
  ImageView thirtyThreePercentColor;
  ImageView sixtySixPercentColor;
  ImageView maxColor;

  public StyleParametersFragment() {
  }

  public static StyleParametersFragment instance(BeerStyle s) {
    // Create the fragment.
    StyleParametersFragment f = new StyleParametersFragment();

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
    ogText = (TextView) pageView.findViewById(R.id.style_og_view);
    fgText = (TextView) pageView.findViewById(R.id.style_fg_view);
    abvText = (TextView) pageView.findViewById(R.id.style_abv_view);
    ibuText = (TextView) pageView.findViewById(R.id.style_ibu_view);
    carbonationText = (TextView) pageView.findViewById(R.id.style_carbonation_view);
    minColor = (ImageView) pageView.findViewById(R.id.min_color_icon);
    thirtyThreePercentColor = (ImageView) pageView.findViewById(R.id.thirtythree_percent_color_icon);
    sixtySixPercentColor = (ImageView) pageView.findViewById(R.id.sixtysix_percent_color_icon);
    maxColor = (ImageView) pageView.findViewById(R.id.max_color_icon);

    ogText.setText(s.getMinOg() + " - " + s.getMaxOg());
    fgText.setText(s.getMinFg() + " - " + s.getMaxFg());
    abvText.setText(s.getMinAbv() + " - " + s.getMaxAbv());
    ibuText.setText(s.getMinIbu() + " - " + s.getMaxIbu());

    // For some reason we don't have carbonation data yet...
    // carbonationText.setText(s.getMinCarb() + " - " + s.getMaxCarb());

    // Set colors.
    String color = ColorHandler.getSrmColor(s.getMinColor());
    minColor.setColorFilter(Color.parseColor(color));

    color = ColorHandler.getSrmColor((s.getMinColor() * 2 + s.getMaxColor()) / 3);
    thirtyThreePercentColor.setColorFilter(Color.parseColor(color));

    color = ColorHandler.getSrmColor((s.getMinColor() + s.getMaxColor() * 2) / 3);
    sixtySixPercentColor.setColorFilter(Color.parseColor(color));

    color = ColorHandler.getSrmColor(s.getMaxColor());
    maxColor.setColorFilter(Color.parseColor(color));

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
    return "Parameters";
  }
}
