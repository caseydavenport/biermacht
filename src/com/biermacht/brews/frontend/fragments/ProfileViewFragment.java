package com.biermacht.brews.frontend.fragments;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.DisplayRecipeActivity;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.recipe.RecipeReccomendedValues;
import com.biermacht.brews.utils.Utils;
import com.biermacht.brews.utils.ColorHandler;
import android.content.*;
import android.widget.*;
import java.util.*;
import com.biermacht.brews.frontend.*;
import com.biermacht.brews.frontend.adapters.*;
import com.biermacht.brews.utils.*;

public class ProfileViewFragment extends Fragment {

	private int resource;
	private Recipe r;
	private OnItemClickListener mClickListener;
	private RecipeReccomendedValues reccomendedValues;
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
	Detail abv;
	Detail color;
	Detail bitterness;

	public ProfileViewFragment(Context c, Recipe r)
	{
		this.resource = R.layout.details_view;
		this.r = r;
		this.c = c;
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		pageView = inflater.inflate(resource, container, false);
		listView = (ListView) pageView.findViewById(R.id.details_list);
		this.detailList = new ArrayList<Detail>();

		setHasOptionsMenu(true);

		// Configure details
		beerType = new Detail();
		beerType.setTitle("Profile Name: ");
		beerType.setType(Detail.TYPE_TEXT);
		beerType.setFormat("%s");
		beerType.setContent(r.getMashProfile().getName());
		detailList.add(beerType);
		
		beerType = new Detail();
		String t = String.format("%2.2f", r.getMashProfile().getDisplayGrainTemp());
		t += " " + Units.FARENHEIT;
		beerType.setTitle("Grain Temp: ");
		beerType.setType(Detail.TYPE_TEXT);
		beerType.setFormat("%s");
		beerType.setContent(t);
		detailList.add(beerType);
		
		beerType = new Detail();
		t = String.format("%2.2f", r.getMashProfile().getDisplayTunTemp());
		t += " " + Units.FARENHEIT;
		beerType.setTitle("Tun Temp: ");
		beerType.setType(Detail.TYPE_TEXT);
		beerType.setFormat("%s");
		beerType.setContent(t);
		detailList.add(beerType);
		
		// Adapter stuff
		mAdapter = new DetailArrayAdapter(c, detailList);
		listView.setAdapter(mAdapter);

		return pageView;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.mash_profile_menu, menu);
	}

}
