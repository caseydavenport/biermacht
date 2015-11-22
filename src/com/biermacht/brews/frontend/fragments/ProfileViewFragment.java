package com.biermacht.brews.frontend.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.Detail;
import com.biermacht.brews.frontend.adapters.DetailArrayAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

import java.util.ArrayList;

public class ProfileViewFragment extends Fragment implements BiermachtFragment {

  private int resource = R.layout.fragment_profile_view;
  ;
  private Recipe r;
  private OnItemClickListener mClickListener;
  View pageView;

  // List stuff
  private DetailArrayAdapter detailArrayAdapter;
  private ArrayList<Detail> mashDetailList;
  private ArrayList<Detail> fermDetailList;
  private ViewGroup mashProfileView;
  private ViewGroup fermentationProfileView;
  private ViewGroup styleProfileView;
  private TextView styleNameView;

  // Details to show
  Detail detail;

  public ProfileViewFragment() {
  }

  public static ProfileViewFragment instance(Recipe r) {
    // Create the fragment.
    ProfileViewFragment f = new ProfileViewFragment();

    // Store the recipe in the arguments bundle.
    Bundle b = new Bundle();
    b.putParcelable(Constants.KEY_RECIPE, r);
    f.setArguments(b);

    // Return the newly created fragment.
    return f;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Get arguments from stored bundle.
    this.r = getArguments().getParcelable(Constants.KEY_RECIPE);

    // No options menu.
    setHasOptionsMenu(false);

    // Inflate and find views.
    pageView = inflater.inflate(resource, container, false);
    mashProfileView = (ViewGroup) pageView.findViewById(R.id.mash_profile_view);
    fermentationProfileView = (ViewGroup) pageView.findViewById(R.id.fermentation_profile_view);
    styleProfileView = (ViewGroup) pageView.findViewById(R.id.style_profile_view);
    styleNameView = (TextView) styleProfileView.findViewById(R.id.style_profile_name);

    // Initialize lists.
    this.mashDetailList = new ArrayList<Detail>();
    this.fermDetailList = new ArrayList<Detail>();

    // Decide on which profiles to show
    if (r.getType().equals(Recipe.EXTRACT)) {
      mashProfileView.setVisibility(View.GONE);
    }

    this.configureMashView(inflater, container);
    this.configureFermentationView(inflater, container);
    this.configureBjcpView(inflater, container);

    return pageView;
  }

  private void configureMashView(LayoutInflater inflater, ViewGroup container) {
    // Configure details
    detail = new Detail();
    detail.setTitle("Profile Name: ");
    detail.setType(Detail.TYPE_TEXT);
    detail.setFormat("%s");
    detail.setContent(r.getMashProfile().getName());
    mashDetailList.add(detail);

    detail = new Detail();
    String t = String.format("%s", r.getMashProfile().getMashType());
    detail.setTitle("Mash Type: ");
    detail.setType(Detail.TYPE_TEXT);
    detail.setFormat("%s");
    detail.setContent(t);
    mashDetailList.add(detail);

    detail = new Detail();
    t = String.format("%s", r.getMashProfile().getSpargeType());
    detail.setTitle("Sparge type: ");
    detail.setType(Detail.TYPE_TEXT);
    detail.setFormat("%s");
    detail.setContent(t);
    mashDetailList.add(detail);

    detail = new Detail();
    t = String.format("%s", r.getMashProfile().getNumberOfSteps());
    detail.setTitle("Step count: ");
    detail.setType(Detail.TYPE_TEXT);
    detail.setFormat("%s");
    detail.setContent(t);
    mashDetailList.add(detail);

    detailArrayAdapter = new DetailArrayAdapter(getActivity(), mashDetailList);
    mashProfileView.addView(detailArrayAdapter.getView(0, null, container));
    mashProfileView.addView(inflater.inflate(R.layout.divider, container, false));
    mashProfileView.addView(detailArrayAdapter.getView(1, null, container));
    mashProfileView.addView(inflater.inflate(R.layout.divider, container, false));
    mashProfileView.addView(detailArrayAdapter.getView(2, null, container));
    mashProfileView.addView(inflater.inflate(R.layout.divider, container, false));
    mashProfileView.addView(detailArrayAdapter.getView(3, null, container));
  }

  private void configureFermentationView(LayoutInflater inflater, ViewGroup container) {
    for (int i = 0; i <= r.getFermentationStages(); i++) {
      String type = "";

      if (i == 0) {
        type = "Primary:";
      }
      if (i == 1) {
        type = "Secondary:";
      }
      if (i == 2) {
        type = "Tertiary:";
      }
      if (i > 2) {
        break;
      }

      detail = new Detail();
      String content = r.getFermentationAge(i + 1) + " " + Units.DAYS
              + " at "
              + String.format("%2.0f", r.getDisplayFermentationTemp(i + 1))
              + Units.getTemperatureUnits();

      detail.setTitle(type);
      detail.setType(Detail.TYPE_TEXT);
      detail.setFormat("%s");
      detail.setContent(content);
      fermDetailList.add(detail);
    }

    detailArrayAdapter = new DetailArrayAdapter(getActivity(), fermDetailList);

    for (int i = 0; i < r.getFermentationStages(); i++) {
      fermentationProfileView.addView(inflater.inflate(R.layout.divider, container, false));
      fermentationProfileView.addView(detailArrayAdapter.getView(i, null, container));
    }
  }

  private void configureBjcpView(LayoutInflater inflater, ViewGroup container) {
    TextView tv = new TextView(getActivity());
    tv.setPadding(20, 20, 20, 20);
    tv.setTextColor(Color.DKGRAY);
    tv.setText(r.getStyle().getNotes());
    styleNameView.setText(r.getStyle().getName());

    //styleProfileView.addView(tv);
  }

  @Override
  public void handleClick(View v) {

  }

  @Override
  public void update() {

  }

  @Override
  public String name() {
    return "Profiles";
  }
}
