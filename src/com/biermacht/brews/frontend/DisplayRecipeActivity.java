package com.biermacht.brews.frontend;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.frontend.IngredientActivities.AddFermentableActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddHopsActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddMiscActivity;
import com.biermacht.brews.frontend.IngredientActivities.AddYeastActivity;
import com.biermacht.brews.frontend.IngredientActivities.EditRecipeActivity;
import com.biermacht.brews.frontend.adapters.DisplayRecipeCollectionPagerAdapter;
import com.biermacht.brews.frontend.fragments.DetailsViewFragment;
import com.biermacht.brews.frontend.fragments.IngredientViewFragment;
import com.biermacht.brews.frontend.fragments.InstructionViewFragment;
import com.biermacht.brews.frontend.fragments.ProfileViewFragment;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.AlertBuilder;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

public class DisplayRecipeActivity extends AppCompatActivity {

  private Recipe mRecipe;
  private int currentItem; // For storing current page
  DisplayRecipeCollectionPagerAdapter cpAdapter;
  ViewPager mViewPager;
  ViewPager.OnPageChangeListener pageListener;
  Menu menu;

  // Alert builder
  public AlertBuilder alertBuilder;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_recipe);

    // Set icon as back button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // Get recipe from intent
    mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);

    // Create alert builder with no callback.
    alertBuilder = new AlertBuilder(this, null);

    // Set on page change listener
    pageListener = new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float offset, int offsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        updateOptionsMenu();
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    };

    // Set to the current item (default to IngredientView)
    currentItem = 0;

    // Update user interface
    updateUI();
  }

  /**
   * TODO: This creates an entire new pager adapter adapter in order to update the UI.  It would be nice
   * if we could just update things in place, without having to create / destroy so many
   * objects.
   */
  public void updatePagerAdater() {
    cpAdapter = new DisplayRecipeCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, getApplicationContext());

    // Set Adapter and onPageChangeListener.
    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(cpAdapter);
    mViewPager.addOnPageChangeListener(pageListener);

    // Set the current item
    mViewPager.setCurrentItem(currentItem);
  }

  /**
   * Updates the UI after (potentially) changes have been made to the Recipe being viewed.
   */
  private void updateUI() {
    // Update the PagerAdapter.
    updatePagerAdater();

    // Set title based on recipe name
    setTitle(mRecipe.getRecipeName());

    // Update which options menu is displayed.
    updateOptionsMenu();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
    menu.removeItem(R.id.menu_add_ing);
    menu.removeItem(R.id.menu_edit_recipe);
    menu.removeItem(R.id.menu_timer);
    menu.removeItem(R.id.menu_profile_dropdown);

    BiermachtFragment f = (BiermachtFragment) cpAdapter.getItem(mViewPager.getCurrentItem());

    if (f instanceof IngredientViewFragment) {
      Log.d("DisplayRecipeActivity", "Displaying fragment_ingredient_menu");
      getMenuInflater().inflate(R.menu.fragment_ingredient_menu, menu);
    }
    else if (f instanceof InstructionViewFragment) {
      Log.d("DisplayRecipeActivity", "Displaying fragment_instruction_menu");
      getMenuInflater().inflate(R.menu.fragment_instruction_menu, menu);
    }
    else if (f instanceof DetailsViewFragment) {
      Log.d("DisplayRecipeActivity", "Displaying fragment_details_menu");
      getMenuInflater().inflate(R.menu.fragment_details_menu, menu);
    }
    else if (f instanceof ProfileViewFragment) {
      Log.d("DisplayRecipeActivity", "Displaying fragment_profile_menu");
      getMenuInflater().inflate(R.menu.fragment_profile_menu, menu);
      if (mRecipe.getType().equals(Recipe.EXTRACT)) {
        Log.d("DisplayRecipeActivity", "Including menu_edit_mash_profile");
        menu.findItem(R.id.menu_edit_mash_profile).setVisible(false);
      }
    }
    else {
      // No cases were matched - return false.
      Log.e("DisplayRecipeActivity", "Unable to select menu.");
      return false;
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent i;

    // Pass the given event to the currently selected Fragment to handle.  If handled, return.
    if (cpAdapter.getItem(mViewPager.getCurrentItem()).onOptionsItemSelected(item)) {
      return true;
    }

    // Otherwise, switch on the item ID.
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;

      case R.id.add_fermentable:
        i = new Intent(this.getApplicationContext(), AddFermentableActivity.class);
        i.putExtra(Constants.KEY_RECIPE, mRecipe);
        startActivity(i);
        return true;

      case R.id.add_hop:
        i = new Intent(getApplicationContext(), AddHopsActivity.class);
        i.putExtra(Constants.KEY_RECIPE, mRecipe);
        startActivity(i);
        return true;

      case R.id.add_yeast:
        i = new Intent(getApplicationContext(), AddYeastActivity.class);
        i.putExtra(Constants.KEY_RECIPE, mRecipe);
        startActivity(i);
        return true;

      case R.id.add_misc:
        i = new Intent(getApplicationContext(), AddMiscActivity.class);
        i.putExtra(Constants.KEY_RECIPE, mRecipe);
        startActivity(i);
        return true;

      case R.id.menu_timer:
        // If we have no instructions, don't go to timer.
        if (mRecipe.getInstructionList().size() == 0) {
          String msg = "Brew timer requires some instructions.  Add to your recipe to generate instructions!";
          alertBuilder.notification("No Instructions", msg).show();
          return false;
        }

        // We have instructions, can go to timer.
        i = new Intent(getApplicationContext(), BrewTimerActivity.class);
        i.putExtra(Constants.KEY_RECIPE, mRecipe);
        startActivity(i);
        return true;

      case R.id.menu_edit_recipe:
        i = new Intent(getApplicationContext(), EditRecipeActivity.class);
        i.putExtra(Constants.KEY_RECIPE, mRecipe);
        startActivity(i);
        return true;

      case R.id.menu_edit_mash_profile:
        i = new Intent(getApplicationContext(), EditMashProfileActivity.class);
        i.putExtra(Constants.KEY_RECIPE, mRecipe);
        i.putExtra(Constants.KEY_PROFILE_ID, mRecipe.getMashProfile().getId());
        i.putExtra(Constants.KEY_PROFILE, mRecipe.getMashProfile());
        startActivity(i);
        return true;

      case R.id.menu_edit_fermentation_profile:
        i = new Intent(getApplicationContext(), EditFermentationProfileActivity.class);
        i.putExtra(Constants.KEY_RECIPE, mRecipe);
        startActivity(i);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  public void onResume() {
    super.onResume();
    Log.d("DisplayRecipeActivity", "onResume: Getting recipe from database");
    // Changes may have been made to this Recipe in another activity - get the Recipe
    // from the database and update the UI.
    try {
      mRecipe = DatabaseAPI.getRecipeWithId(mRecipe.getId());
    } catch (Exception e) {
      e.printStackTrace();
    }

    updateUI();
  }

  public void updateOptionsMenu() {
    if (menu != null) {
      onCreateOptionsMenu(menu);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    // Save the current page we're looking at
    this.currentItem = mViewPager.getCurrentItem();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }
}
