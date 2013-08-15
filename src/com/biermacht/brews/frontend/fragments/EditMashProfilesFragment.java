package com.biermacht.brews.frontend.fragments;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.IngredientActivities.EditCustomFermentableActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditHopActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditMiscActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditYeastActivity;
import com.biermacht.brews.frontend.adapters.CustomIngredientArrayAdapter;
import com.biermacht.brews.frontend.adapters.MashProfileArrayAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

import android.content.*;

import com.biermacht.brews.utils.comparators.IngredientComparator;
import com.biermacht.brews.utils.comparators.ToStringComparator;

public class EditMashProfilesFragment extends Fragment {

    private static int resource = R.layout.fragment_view;
    private OnItemClickListener mClickListener;
    private ListView listView;
    private ArrayList<MashProfile> list;
    View pageView;
    Context c;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        pageView = inflater.inflate(resource, container, false);

        setHasOptionsMenu(true);

        // Context
        c = getActivity();

        // Get ingredient list
        list = Database.getMashProfilesFromVirtualDatabase(Constants.DATABASE_CUSTOM);
        Collections.sort(list, new ToStringComparator());

        // Initialize important junk
        listView = (ListView) pageView.findViewById(R.id.listview);

        // Set up the onClickListener
        mClickListener = new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id)
            {
                MashProfile profile = list.get(pos);

            }
        };

        // Set whether or not we show the list view
        if (list.size() > 0)
        {
            MashProfileArrayAdapter arrayAdapter = new MashProfileArrayAdapter(c, list);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(arrayAdapter);
            registerForContextMenu(listView);
            listView.setOnItemClickListener(mClickListener);
        }
        else
        {
            TextView noListView = (TextView) pageView.findViewById(R.id.nothing_to_show_view);
            noListView.setText("No mash profiles to display");
            noListView.setVisibility(View.VISIBLE);
        }

        return pageView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_mash_profiles_menu, menu);
    }

}
