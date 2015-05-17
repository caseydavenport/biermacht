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
import com.biermacht.brews.frontend.EditCustomMashProfileActivity;
import com.biermacht.brews.frontend.adapters.MashProfileArrayAdapter;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;

import android.content.*;

import com.biermacht.brews.utils.comparators.ToStringComparator;
import com.biermacht.brews.utils.interfaces.ClickableFragment;

public class EditMashProfilesFragment extends Fragment implements ClickableFragment {

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
                Intent i = new Intent(c, EditCustomMashProfileActivity.class);
                i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
                i.putExtra(Constants.KEY_PROFILE_ID, list.get(pos).getId());
                i.putExtra(Constants.KEY_PROFILE, list.get(pos));
                startActivity(i);

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

    @Override
    public void handleClick(View v) {

    }
}
