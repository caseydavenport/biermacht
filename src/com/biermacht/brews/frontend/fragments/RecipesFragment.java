package com.biermacht.brews.frontend.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseInterface;
import com.biermacht.brews.frontend.BrewTimerActivity;
import com.biermacht.brews.frontend.DisplayRecipeActivity;
import com.biermacht.brews.frontend.EditFermentationProfileActivity;
import com.biermacht.brews.frontend.EditMashProfileActivity;
import com.biermacht.brews.frontend.EditRecipeNotesActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditRecipeActivity;
import com.biermacht.brews.frontend.MainActivity;
import com.biermacht.brews.frontend.adapters.RecipeArrayAdapter;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Database;
import com.biermacht.brews.utils.Utils;

import java.util.ArrayList;

public class RecipesFragment extends Fragment {

    // Layout resource
    private static int resource = R.layout.fragment_recipes;

    // Recipe List stuff
    private RecipeArrayAdapter mAdapter;
    private AdapterView.OnItemClickListener mClickListener;
    private ArrayList<Recipe> recipeList;

    // Database Interface
    private DatabaseInterface databaseInterface;

    // Context menu items
    private ArrayList<String> menuItems;

    // Holds the selected recipe
    private Recipe selectedRecipe;

    // Search view stuff
    private TextWatcher mTextWatcher;
    private int searchOptions;

    // Context
    private Context c;

    //Declare views here
    private ListView listView;
    private EditText searchView;
    private TextView noRecipesView;
    ViewGroup pageView;

    // Static menu items
    private static String EDIT_RECIPE = "Edit Recipe";
    private static String SCALE_RECIPE = "Scale Recipe";
    private static String COPY_RECIPE = "Copy Recipe";
    private static String DELETE_RECIPE = "Delete Recipe";
    private static String EDIT_FERM = "Edit Fermentation Profile";
    private static String EDIT_MASH = "Edit Mash Profile";
    private static String EXPORT_RECIPE = "Export as BeerXML";
    private static String BREW_TIMER = "Brew Timer";
    private static String RECIPE_NOTES = "Recipe Notes";

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        // Get views
		pageView = (RelativeLayout) inflater.inflate(resource, container, false);
        listView = (ListView) pageView.findViewById(R.id.recipe_list);
        searchView = (EditText) pageView.findViewById(R.id.search_bar);
        noRecipesView = (TextView) pageView.findViewById(R.id.no_recipes_view);

        // Get database Interface
        databaseInterface = MainActivity.databaseInterface;

        // Get Context
        c = getActivity();

        // Set up the onClickListener
        mClickListener = new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parentView, View childView, int pos,
                                    long id)
            {
                Intent intent = new Intent(c, DisplayRecipeActivity.class);
                intent.putExtra(Constants.KEY_RECIPE, recipeList.get(pos));
                startActivity(intent);
            }
        };

        // Set up the textWatcher for search bar
        mTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s)
            {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Recipe> fList = getFilteredList(s.toString());
                updateRecipeList(fList);
            }
        };

        // Set default search options to search by all criteria
        searchOptions = 1;

        // Add TextWatcher to search bar
        searchView.addTextChangedListener(mTextWatcher);

        // Set up my listView with title and ArrayAdapter
        new GetRecipeListFromDatabaseTask(getActivity()).execute("");
        listView.setOnItemClickListener(mClickListener);
        registerForContextMenu(listView);

        // Turn on options menu
		setHasOptionsMenu(true);
		
		return pageView;
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main, menu);
    }

    /**
     * Filters the list of all Recipes by the given string
     * @param s String to filter by
     * @return ArrayList of matching recipes
     */
    private ArrayList<Recipe> getFilteredList(String s)
    {
        recipeList = Database.getRecipeList(databaseInterface);
        ArrayList<Recipe> filteredList = new ArrayList<Recipe>();

        for (Recipe r : recipeList)
        {
            // Search options decoder
            // 1 --> Filter by all the following
            // 2 --> Filter by name only
            // 3 --> Filter by type only

            boolean searchByName = (searchOptions == 1 || searchOptions == 2);
            boolean searchByType = (searchOptions == 1 || searchOptions == 3);

            // If the recipe name matches
            if(r.getRecipeName().toLowerCase().contains(s.toLowerCase()) && searchByName)
            {
                filteredList.add(r);
            }

            // If the recipe type matches
            else if(r.getStyle().toString().toLowerCase().contains(s.toLowerCase()) && searchByType )
            {
                filteredList.add(r);
            }
        }
        return filteredList;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (v == listView)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            String title = recipeList.get(info.position).getRecipeName();

            selectedRecipe = recipeList.get(info.position);

            menu.setHeaderTitle(title);
            menuItems = new ArrayList<String>();

            // Build menu items
            menuItems.add(EDIT_RECIPE);
            if (!selectedRecipe.getType().equals(Recipe.EXTRACT))
                menuItems.add(EDIT_MASH);
            menuItems.add(EDIT_FERM);
            menuItems.add(BREW_TIMER);
            menuItems.add(RECIPE_NOTES);
            menuItems.add(SCALE_RECIPE);
            menuItems.add(COPY_RECIPE);
            menuItems.add(DELETE_RECIPE);

            for (int i = 0; i < menuItems.size(); i++)
            {
                menu.add(Menu.NONE, i, i, menuItems.get(i));
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String selected = menuItems.get(item.getItemId());

        // Edit recipe selected
        if (selected.equals(EDIT_RECIPE))
        {
            Intent i = new Intent(c, EditRecipeActivity.class);
            i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
            i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
            startActivity(i);
        }

        // Scale recipe selected
        else if (selected.equals(SCALE_RECIPE))
        {
            scaleAlert(selectedRecipe).show();
        }

        // Copy recipe selected
        else if (selected.equals(COPY_RECIPE))
        {
            selectedRecipe.setRecipeName(selectedRecipe.getRecipeName() + " - Copy");
            Database.createRecipeFromExisting(selectedRecipe);
            updateRecipeList(getFilteredList(searchView.getText().toString()));

        }

        // Delete recipe selected
        else if (selected.equals(DELETE_RECIPE))
        {
            deleteAlert(selectedRecipe).show();
        }

        // Edit fermentation selected
        else if (selected.equals(EDIT_FERM))
        {
            Intent i = new Intent(c, EditFermentationProfileActivity.class);
            i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
            i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
            startActivity(i);
        }

        //  Brew timer
        else if (selected.equals(BREW_TIMER))
        {
            Intent i = new Intent(c, BrewTimerActivity.class);
            i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
            i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
            startActivity(i);
        }

        // Edit Mash profile
        else if (selected.equals(EDIT_MASH))
        {
            Intent i = new Intent(c, EditMashProfileActivity.class);
            i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
            i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
            i.putExtra(Constants.KEY_PROFILE_ID, selectedRecipe.getMashProfile().getId());
            i.putExtra(Constants.KEY_PROFILE, selectedRecipe.getMashProfile());
            startActivity(i);
        }

        else if (selected.equals(RECIPE_NOTES))
        {
            Intent i = new Intent(c, EditRecipeNotesActivity.class);
            i.putExtra(Constants.KEY_RECIPE, selectedRecipe);
            i.putExtra(Constants.KEY_RECIPE_ID, selectedRecipe.getId());
            startActivity(i);
        }

        return true;
    }

    /**
     * Takes given list of recipes and displays them.  Also
     * sets recipelist field to the new list
     * @param l
     */
    public void updateRecipeList(ArrayList<Recipe> l)
    {
        // Set up my listView with title and ArrayAdapter
        mAdapter = new RecipeArrayAdapter(c, l);
        listView.setAdapter(mAdapter);
        recipeList = l;

        if (l.size() == 0)
        {
            noRecipesView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        else
        {
            noRecipesView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    private AlertDialog.Builder deleteAlert(final Recipe r)
    {
        return new AlertDialog.Builder(c)
                .setTitle("Confirm Delete")
                .setMessage("Do you really want to delete '" + r.getRecipeName() +"'")
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Database.deleteRecipe(r);
                        updateRecipeList(getFilteredList(searchView.getText().toString()));
                    }

                })

                .setNegativeButton(android.R.string.no, null);
    }

    private AlertDialog.Builder scaleAlert(final Recipe r)
    {
        LayoutInflater factory = LayoutInflater.from(c);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_scale, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.new_volume_edit_text);

        return new AlertDialog.Builder(c)
                .setTitle("Scale Recipe")
                .setView(alertView)
                .setPositiveButton(R.string.scale, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        double newVolume = Double.parseDouble(editText.getText().toString());
                        Utils.scaleRecipe(r, newVolume);
                        updateRecipeList(getFilteredList(searchView.getText().toString()));
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }

    private class GetRecipeListFromDatabaseTask extends AsyncTask<String, Void, String> {

        private Context context;
        private ProgressDialog progress;

        public GetRecipeListFromDatabaseTask(Context c)
        {
            this.context = c;
        }

        @Override
        protected String doInBackground(String... params)
        {
            // Get recipes to display
            recipeList = Database.getRecipeList(databaseInterface);

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            //progress.dismiss();
            updateRecipeList(recipeList);
            Log.d("readRecipesFromDatabase", "Finished reading recipes");
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Importing...");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(true);
            //progress.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
