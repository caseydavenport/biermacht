package com.biermacht.brews.frontend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyleProfileFragment extends Fragment implements BiermachtFragment {

  private int resource = R.layout.fragment_style_profile_view;

  private BeerStyle s;

  // Views.
  View pageView;
  TextView aromaText;
  TextView appearanceText;
  TextView flavorText;
  TextView mouthfeelText;

  public StyleProfileFragment() {
  }

  public static StyleProfileFragment instance(BeerStyle s) {
    // Create the fragment.
    StyleProfileFragment f = new StyleProfileFragment();

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
    aromaText = (TextView) pageView.findViewById(R.id.aroma_text);
    appearanceText = (TextView) pageView.findViewById(R.id.appearance_text);
    flavorText = (TextView) pageView.findViewById(R.id.flavor_text);
    mouthfeelText = (TextView) pageView.findViewById(R.id.mouthfeel_text);

    // Parse out the various pieces of the profile - Aroma, Appearance, Flavor, Mouthfeel.
    String pattern = "Aroma:(.*)Appearance:(.*)Flavor:(.*)Mouthfeel:(.*)?";
    Pattern r = Pattern.compile(pattern);
    Matcher m = r.matcher(s.getProfile());

    String flavor = "No flavor profile";
    String appearance = "No appearance profile";
    String aroma = "No aroma profile";
    String mouthfeel = "No mouthfeel profile";

    if (m.find()) {
      aroma = m.group(1);
      appearance = m.group(2);
      flavor = m.group(3);
      mouthfeel = m.group(4);
    }
    else {
      Log.d("StyleProfileFragment", "Failed to parse profile: " + s.getProfile());
    }

    aromaText.setText(aroma.trim());
    appearanceText.setText(appearance.trim());
    flavorText.setText(flavor.trim());
    mouthfeelText.setText(mouthfeel.trim());

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
    return "Profile";
  }
}
