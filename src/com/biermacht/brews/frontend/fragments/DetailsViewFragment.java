package com.biermacht.brews.frontend.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;

import android.content.*;
import android.widget.*;
import java.util.*;
import com.biermacht.brews.frontend.*;
import com.biermacht.brews.frontend.adapters.*;

public class DetailsViewFragment extends Fragment {

	private int resource;
	private Recipe r;
	private OnItemClickListener mClickListener;
	View pageView;
	Context c;
	
	// List stuff
	private DetailArrayAdapter mAdapter;
	private ArrayList<Detail> detailList;
	private ListView listView;
	
	// Details to show
	Detail beerType;
	Detail originalGravity;
	Detail finalGravity;
    Detail eff;
	Detail abv;
	Detail color;
	Detail bitterness;
	
	public DetailsViewFragment(Context c, Recipe r)
	{
		this.resource = R.layout.fragment_details_view;
		this.r = r;
		this.c = c;

        setRetainInstance(true);
	}
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		pageView = inflater.inflate(resource, container, false);
		listView = (ListView) pageView.findViewById(R.id.details_list);
		this.detailList = new ArrayList<Detail>();

        setHasOptionsMenu(false);
			
		// Configure details
		beerType = new Detail();
		beerType.setTitle("Beer Style: ");
		beerType.setType(Detail.TYPE_TEXT);
		beerType.setFormat("%s");
		beerType.setContent(r.getStyle().getName());
		detailList.add(beerType);
			
		originalGravity = new Detail();
		originalGravity.setTitle("Original Gravity: ");
		originalGravity.setValue(r.getOG());
		originalGravity.setFormat("%2.3f");
		originalGravity.setMin(r.getStyle().getMinOg());
		originalGravity.setMax(r.getStyle().getMaxOg());
		originalGravity.setVariance(.002);
		detailList.add(originalGravity);

        originalGravity = new Detail();
        originalGravity.setTitle("Measured OG: ");
        originalGravity.setValue(r.getMeasuredOG());
        originalGravity.setFormat("%2.3f");
        originalGravity.setMin(r.getStyle().getMinOg());
        originalGravity.setMax(r.getStyle().getMaxOg());
        originalGravity.setVariance(.002);
        if (r.getMeasuredOG() > 0)
            detailList.add(originalGravity);
			
		finalGravity = new Detail();
		finalGravity.setTitle("Final Gravity: ");
		finalGravity.setValue(r.getFG());
		finalGravity.setFormat("%2.3f");
		finalGravity.setVariance(.002);
		finalGravity.setMin(r.getStyle().getMinFg());
		finalGravity.setMax(r.getStyle().getMaxFg());
		detailList.add(finalGravity);

        finalGravity = new Detail();
        finalGravity.setTitle("Measured FG: ");
        finalGravity.setValue(r.getMeasuredFG());
        finalGravity.setFormat("%2.3f");
        finalGravity.setVariance(.002);
        finalGravity.setMin(r.getStyle().getMinFg());
        finalGravity.setMax(r.getStyle().getMaxFg());
        if (r.getMeasuredFG() > 0)
            detailList.add(finalGravity);

		bitterness = new Detail();
		bitterness.setTitle("Bitterness, IBU: ");
		bitterness.setValue(r.getBitterness());
		bitterness.setFormat("%2.1f");
		bitterness.setVariance(.2);
		bitterness.setMin(r.getStyle().getMinIbu());
		bitterness.setMax(r.getStyle().getMaxIbu());
		detailList.add(bitterness);
		  
		color = new Detail();
		color.setTitle("Color, SRM: ");
		color.setValue(r.getColor());
		color.setFormat("%2.1f");
		color.setVariance(.1);
		color.setMin(r.getStyle().getMinColor());
		color.setMax(r.getStyle().getMaxColor());
		detailList.add(color);
			
		abv = new Detail();
		abv.setTitle("Estimated ABV: ");
		abv.setValue(r.getABV());
		abv.setFormat("%2.1f");
		abv.setVariance(.06);
		abv.setMin(r.getStyle().getMinAbv());
		abv.setMax(r.getStyle().getMaxAbv());
		detailList.add(abv);

        abv = new Detail();
        abv.setTitle("Measured ABV: ");
        abv.setValue(r.getMeasuredABV());
        abv.setFormat("%2.1f");
        abv.setVariance(.06);
        abv.setMin(r.getStyle().getMinAbv());
        abv.setMax(r.getStyle().getMaxAbv());
        if (r.getMeasuredABV() > 0)
            detailList.add(abv);

        eff = new Detail();
        eff.setTitle("Efficiency: ");
        eff.setValue(r.getMeasuredEfficiency());
        eff.setFormat("%2.0f");
        eff.setVariance(.1);
        eff.setMin(65.0);
        eff.setMax(100.0);
        if (r.getMeasuredEfficiency() > 0 && !r.getType().equals(Recipe.EXTRACT))
            detailList.add(eff);
			
		// Adapter stuff
		mAdapter = new DetailArrayAdapter(c, detailList);
		listView.setAdapter(mAdapter);
		  
		return pageView;
	}
}
