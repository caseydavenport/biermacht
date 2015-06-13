package com.biermacht.brews.frontend.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.Detail;
import com.biermacht.brews.frontend.adapters.DetailArrayAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;

import java.util.ArrayList;

public class DetailsViewFragment extends Fragment {

  private int resource = R.layout.fragment_details_view;
  private Recipe r;
  private View pageView;

  // ListView, List, and Adapter.
  private DetailArrayAdapter mAdapter;
  private ArrayList<Detail> detailList;
  private ListView listView;

  // Detail views to show.  These are statically defined.
  Detail beerType;
  Detail originalGravity;
  Detail finalGravity;
  Detail eff;
  Detail abv;
  Detail color;
  Detail bitterness;

  public DetailsViewFragment() {
    // TODO: What does this do? No wi-fi right now.
    setRetainInstance(true);

    // No options menu.
    setHasOptionsMenu(false);
  }

  public static DetailsViewFragment instance(Recipe r) {
    // Create the fragment.
    DetailsViewFragment f = new DetailsViewFragment();

    // Store the recipe in the arguments bundle.
    Bundle b = new Bundle();
    b.putParcelable(Constants.KEY_RECIPE, r);
    f.setArguments(b);

    // Return the newly created fragment.
    return f;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Get stored arguments from bundle.
    this.r = getArguments().getParcelable(Constants.KEY_RECIPE);

    // Inflate the resource for this fragment and find the component views.
    pageView = inflater.inflate(resource, container, false);
    listView = (ListView) pageView.findViewById(R.id.details_list);

    // Create a new details list.
    this.detailList = new ArrayList<Detail>();

    // First, create the Beer Style detail.  This Detail is a text type 
    // which displays the beer's BJCP style name.
    this.beerType = new Detail();
    this.beerType.setTitle("Beer Style: ");
    this.beerType.setType(Detail.TYPE_TEXT);
    this.beerType.setFormat("%s");
    this.beerType.setContent(this.r.getStyle().getName());
    this.detailList.add(this.beerType);

    // Create and add the Original Gravity detail.  This Detail is a range
    // type which displays the estimated original gravity.
    this.originalGravity = new Detail();
    this.originalGravity.setTitle("Original Gravity: ");
    this.originalGravity.setValue(this.r.getOG());
    this.originalGravity.setFormat("%2.3f");
    this.originalGravity.setMin(this.r.getStyle().getMinOg());
    this.originalGravity.setMax(this.r.getStyle().getMaxOg());
    this.originalGravity.setVariance(.002);
    this.detailList.add(this.originalGravity);

    // If the recipe has a measured original gravity, include it in the details list.
    if (this.r.getMeasuredOG() > 0) {
      // The Measured OG Detail is a range type which displays the measured 
      // original gravity, as input by the user.
      this.originalGravity = new Detail();
      this.originalGravity.setTitle("Measured OG: ");
      this.originalGravity.setValue(this.r.getMeasuredOG());
      this.originalGravity.setFormat("%2.3f");
      this.originalGravity.setMin(this.r.getStyle().getMinOg());
      this.originalGravity.setMax(this.r.getStyle().getMaxOg());
      this.originalGravity.setVariance(.002);
      this.detailList.add(this.originalGravity);
    }

    // Add the Final Gravity Detail.  This is a range detail which displays
    // the estimated final gravity.
    this.finalGravity = new Detail();
    this.finalGravity.setTitle("Final Gravity: ");
    this.finalGravity.setValue(this.r.getFG());
    this.finalGravity.setFormat("%2.3f");
    this.finalGravity.setVariance(.002);
    this.finalGravity.setMin(this.r.getStyle().getMinFg());
    this.finalGravity.setMax(this.r.getStyle().getMaxFg());
    this.detailList.add(this.finalGravity);

    // If a measured final gravity exists, add a detail to display it.
    if (this.r.getMeasuredFG() > 0) {
      // Add the measured final gravity Detail.  This is a range type which
      // displays the user-provided final gravity.
      this.finalGravity = new Detail();
      this.finalGravity.setTitle("Measured FG: ");
      this.finalGravity.setValue(this.r.getMeasuredFG());
      this.finalGravity.setFormat("%2.3f");
      this.finalGravity.setVariance(.002);
      this.finalGravity.setMin(this.r.getStyle().getMinFg());
      this.finalGravity.setMax(this.r.getStyle().getMaxFg());
      this.detailList.add(this.finalGravity);
    }

    // Add the bitterness Detail.  This is a range type which displays 
    // estimated bitterness in IBUs.
    this.bitterness = new Detail();
    this.bitterness.setTitle("Bitterness, IBU: ");
    this.bitterness.setValue(this.r.getBitterness());
    this.bitterness.setFormat("%2.1f");
    this.bitterness.setVariance(.2);
    this.bitterness.setMin(this.r.getStyle().getMinIbu());
    this.bitterness.setMax(this.r.getStyle().getMaxIbu());
    this.detailList.add(this.bitterness);

    // Add the color Detail.  This is a range type which displays
    // the estimated color for this recipe.
    this.color = new Detail();
    this.color.setTitle("Color, SRM: ");
    this.color.setValue(this.r.getColor());
    this.color.setFormat("%2.1f");
    this.color.setVariance(.1);
    this.color.setMin(this.r.getStyle().getMinColor());
    this.color.setMax(this.r.getStyle().getMaxColor());
    this.detailList.add(this.color);

    // Add the estimated ABV Detail.  This is a range type which displays the 
    // estimated alcohol-by-volume for this recipe.
    this.abv = new Detail();
    this.abv.setTitle("Estimated ABV: ");
    this.abv.setValue(this.r.getABV());
    this.abv.setFormat("%2.1f");
    this.abv.setVariance(.06);
    this.abv.setMin(this.r.getStyle().getMinAbv());
    this.abv.setMax(this.r.getStyle().getMaxAbv());
    this.detailList.add(this.abv);

    // If a measured ABV exists, add a detail to display it.
    if (this.r.getMeasuredABV() > 0) {
      // Add the measured ABV Detail.  This displays the ABV calculated using
      // the provided measured OG and measured FG.
      this.abv = new Detail();
      this.abv.setTitle("Measured ABV: ");
      this.abv.setValue(this.r.getMeasuredABV());
      this.abv.setFormat("%2.1f");
      this.abv.setVariance(.06);
      this.abv.setMin(this.r.getStyle().getMinAbv());
      this.abv.setMax(this.r.getStyle().getMaxAbv());
      this.detailList.add(this.abv);
    }

    // If a measured efficiency is present, display it as a Detail.  This
    // detail is not valid for Extract recipes.
    if ((this.r.getMeasuredEfficiency() > 0) &&
            (! this.r.getType().equals(Recipe.EXTRACT))) {
      // Add the Measured Efficiency Detail.  This is a range type which displays
      // mash efficiency as calculated using the user-input OG and volume.
      this.eff = new Detail();
      this.eff.setTitle("Efficiency: ");
      this.eff.setValue(this.r.getMeasuredEfficiency());
      this.eff.setFormat("%2.0f");
      this.eff.setVariance(.1);
      this.eff.setMin(65.0);
      this.eff.setMax(100.0);
      this.detailList.add(this.eff);
    }

    // Create a new DetailArrayAdapter and set it on the listView.
    this.mAdapter = new DetailArrayAdapter(getActivity(), this.detailList);
    this.listView.setAdapter(this.mAdapter);

    return this.pageView;
  }
}
