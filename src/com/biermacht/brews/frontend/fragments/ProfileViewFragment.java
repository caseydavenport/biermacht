package com.biermacht.brews.frontend.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import android.content.*;

import java.util.*;
import com.biermacht.brews.frontend.*;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.utils.*;

public class ProfileViewFragment extends Fragment {

	private int resource;
	private Recipe r;
	private OnItemClickListener mClickListener;
	View pageView;
	Context c;

	// List stuff
	private DetailArrayAdapter detailArrayAdapter;
	private ArrayList<Detail> mashDetailList;
	private ArrayList<Detail> fermDetailList;
	private ViewGroup mashProfileView;
	private ViewGroup fermentationProfileView;
    private ViewGroup bjcpProfileView;
	private View view;

	// Details to show
	Detail detail;

	public ProfileViewFragment(Context c, Recipe r)
	{
		this.resource = R.layout.fragment_profile_view;
		this.r = r;
		this.c = c;
	}

	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        setHasOptionsMenu(false);
		pageView = inflater.inflate(resource, container, false);
		mashProfileView = (ViewGroup) pageView.findViewById(R.id.mash_profile_view);
		fermentationProfileView = (ViewGroup) pageView.findViewById(R.id.fermentation_profile_view);
        bjcpProfileView = (ViewGroup) pageView.findViewById(R.id.bjcp_profile_view);
		
		this.mashDetailList = new ArrayList<Detail>();
		this.fermDetailList = new ArrayList<Detail>();
		
		// Decide on which profiles to show
		if (r.getType().equals(Recipe.EXTRACT))
			mashProfileView.setVisibility(View.GONE);
		
		this.configureMashView(inflater, container);
		this.configureFermentationView(inflater, container);
        this.configureBjcpView(inflater, container);
		
		return pageView;
	}
	
	private void configureMashView(LayoutInflater inflater, ViewGroup container)
	{
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
		
		detailArrayAdapter = new DetailArrayAdapter(c, mashDetailList);
		mashProfileView.addView(detailArrayAdapter.getView(0, null, container));
		mashProfileView.addView(inflater.inflate(R.layout.divider, container, false));
		mashProfileView.addView(detailArrayAdapter.getView(1, null, container));
		mashProfileView.addView(inflater.inflate(R.layout.divider, container, false));
		mashProfileView.addView(detailArrayAdapter.getView(2, null, container));
	}
	
	private void configureFermentationView(LayoutInflater inflater, ViewGroup container)
	{
		for (int i = 0; i <= r.getFermentationStages(); i++)
		{
			String type = "";
			
			if (i == 0)
				type = "Primary:";
			if (i == 1)
				type = "Secondary:";
			if (i == 2)
				type = "Tertiary:";
			if (i > 2)
				break;
			
			detail = new Detail();
			String content = r.getFermentationAge(i+1) + " " + Units.DAYS
					         + " at "
					         + String.format("%2.0f", r.getDisplayFermentationTemp(i+1))
					         + Units.getTemperatureUnits();
			
			detail.setTitle(type);
			detail.setType(Detail.TYPE_TEXT);
			detail.setFormat("%s");
			detail.setContent(content);
			fermDetailList.add(detail);
		}
		
		detailArrayAdapter = new DetailArrayAdapter(c, fermDetailList);

		for (int i=0; i < r.getFermentationStages(); i++)
		{
			fermentationProfileView.addView(inflater.inflate(R.layout.divider, container, false));
			fermentationProfileView.addView(detailArrayAdapter.getView(i, null, container));
		}
	}

    private void configureBjcpView(LayoutInflater inflater, ViewGroup container)
    {
        TextView tv = new TextView(c);
        tv.setPadding(20,20,20,20);
        tv.setTextColor(Color.DKGRAY);
        tv.setText(r.getStyle().getNotes());
        bjcpProfileView.addView(tv);
    }
}
