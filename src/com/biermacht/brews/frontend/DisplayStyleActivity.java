package com.biermacht.brews.frontend;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.DisplayStyleCollectionPagerAdapter;
import com.biermacht.brews.recipe.BeerStyle;
import com.biermacht.brews.utils.Constants;

public class DisplayStyleActivity extends AppCompatActivity {

  private BeerStyle style;
  private int currentItem;                           // For storing current page
  DisplayStyleCollectionPagerAdapter cpAdapter;
  ViewPager mViewPager;
  ViewPager.OnPageChangeListener pageListener;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_recipe);

    // Set icon as back button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    // Get recipe from intent
    style = getIntent().getParcelableExtra(Constants.KEY_STYLE);

    // Set on page change listener
    pageListener = new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float offset, int offsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    };

    // Set to the current item.
    currentItem = 0;

    // Update user interface
    updateUI();
  }

  /**
   * TODO: This creates an entire new pager adapter adapter in order to update the UI.  It would be
   * nice if we could just update things in place, without having to create / destroy so many
   * objects.
   */
  public void updatePagerAdater() {
    cpAdapter = new DisplayStyleCollectionPagerAdapter(getSupportFragmentManager(), style, getApplicationContext());

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
    setTitle(style.getName());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onPause() {
    super.onPause();
    // Save the current page we're looking at
    this.currentItem = mViewPager.getCurrentItem();
  }
}
