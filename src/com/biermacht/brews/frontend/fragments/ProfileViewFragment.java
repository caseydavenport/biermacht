package com.biermacht.brews.frontend.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.recipe.Recipe;
import android.content.*;
import android.widget.*;
import java.util.*;
import com.biermacht.brews.frontend.*;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.utils.*;
import com.biermacht.brews.recipe.*;

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
	private ViewGroup profileView;
	private ViewGroup mashProfileView;
	private ViewGroup fermentationProfileView;
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
		setHasOptionsMenu(true);
		pageView = inflater.inflate(resource, container, false);
		profileView = (ViewGroup) pageView.findViewById(R.id.profile_view);
		mashProfileView = (ViewGroup) pageView.findViewById(R.id.mash_profile_view);
		fermentationProfileView = (ViewGroup) pageView.findViewById(R.id.fermentation_profile_view);
		
		this.mashDetailList = new ArrayList<Detail>();
		this.fermDetailList = new ArrayList<Detail>();
		
		// Decide on which profiles to show
		if (r.getType().equals(Recipe.EXTRACT))
			mashProfileView.setVisibility(View.GONE);
		
		this.configureMashView(inflater, container);
		this.configureFermentationView(inflater, container);
		
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
		String t = String.format("%2.0f", r.getMashProfile().getDisplayGrainTemp());
		t += " " + Units.FARENHEIT;
		detail.setTitle("Grain Temp: ");
		detail.setType(Detail.TYPE_TEXT);
		detail.setFormat("%s");
		detail.setContent(t);
		mashDetailList.add(detail);
		
		detail = new Detail();
		t = String.format("%d", r.getMashProfile().getNumberOfSteps());
		detail.setTitle("Mash Steps: ");
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
		// Configure details
		detail = new Detail();
		detail.setTitle("Num. Stages: ");
		detail.setType(Detail.TYPE_TEXT);
		detail.setFormat("%s");
		detail.setContent(r.getFermentationStages()+"");
		fermDetailList.add(detail);
		
		for (int i = 1; i <= r.getFermentationStages(); i++)
		{
			String type = "";
			
			if (i == 1)
				type = "Primary:";
			if (i == 2)
				type = "Secondary:";
			if (i == 3)
				type = "Tertiary:";
			if (i > 3)
				break;
			
			detail = new Detail();
			String content = r.getFermentationAge(i) + " " + Units.DAYS
					         + " at "
					         + String.format("%2.0f", r.getDisplayFermentationTemp(i))
					         + " F";
			
			detail.setTitle(type);
			detail.setType(Detail.TYPE_TEXT);
			detail.setFormat("%s");
			detail.setContent(content);
			fermDetailList.add(detail);
		}
		
		detailArrayAdapter = new DetailArrayAdapter(c, fermDetailList);
		
		fermentationProfileView.addView(detailArrayAdapter.getView(0, null, container));
		
		for (int i=1; i <= r.getFermentationStages(); i++)
		{
			fermentationProfileView.addView(inflater.inflate(R.layout.divider, container, false));
			fermentationProfileView.addView(detailArrayAdapter.getView(i, null, container));
		}
		
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.edit_profile_menu, menu);
	}

}
