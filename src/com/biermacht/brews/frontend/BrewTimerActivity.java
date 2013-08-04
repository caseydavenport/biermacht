package com.biermacht.brews.frontend;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.BrewTimerCollectionPagerAdapter;
import com.biermacht.brews.frontend.adapters.DisplayRecipeCollectionPagerAdapter;
import com.biermacht.brews.frontend.fragments.BrewTimerStepFragment;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Units;
import com.biermacht.brews.utils.Utils;

public class BrewTimerActivity extends FragmentActivity {
	
	private Recipe mRecipe;
	private long id; // id of recipe we use
	private int currentItem; // For storing current page being viewed
    private int pausedItem; // Stores the item we paused on.
	BrewTimerCollectionPagerAdapter cpAdapter;
    private Handler timerHandler;

    // Formatter for timer values
    java.text.DecimalFormat nft;

    // True if timer is running, paused, or stopped
    private int timerState;

    // Possible timer states
    private static int PAUSED = 0;
    private static int RUNNING = 1;
    private static int STOPPED = 2;

    // Views
    ViewGroup mainLayout;
    ViewPager mViewPager;
    TextView hoursView;
    TextView minutesView;
    TextView secondsView;
    View timerControls;

    // LayoutInflater
    LayoutInflater inflater;

    // Currently seletected fragment and instruction
    BrewTimerStepFragment f;
    Instruction i;

    // Application context
	private Context appContext;

    // Runnable for updating the time
    private Runnable mUpdateTimeTask = new Runnable() {

        int seconds;

        public void run()
        {
            seconds = getTimerSeconds() - 1;
            setTimerFromSeconds(seconds);
            timerHandler.postDelayed(mUpdateTimeTask, 1000);

            // When we get to the next step, raise alarm, pause timer,
            // and switch views
            if (seconds == Units.toSeconds(i.getNextDuration(), i.getDurationUnits()))
            {
                pause();
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }

            // Stop if we've reached the end of a sections
            if (seconds == 0)
                stop();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_timer);

        // Timer is not yet running
        timerState = STOPPED;

        // Configure formatter for timer values
        nft = new java.text.DecimalFormat("#00.###");
        nft.setDecimalSeparatorAlwaysShown(false);

        // Timer handler
        timerHandler = new Handler();
        
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get inflater
        inflater = getLayoutInflater();

        // Get main layout
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        // Get context
        appContext = getApplicationContext();
        
        // Get recipe from calling activity
        id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, -1);
        mRecipe = Utils.getRecipeWithId(id);

        // Set title based on recipe name
        setTitle("Brew Timer");

        // Inflate timer controls
        timerControls = inflater.inflate(R.layout.view_timer_controls, mainLayout, false);

        // get countdown views
        hoursView = (TextView) timerControls.findViewById(R.id.hours);
        minutesView = (TextView) timerControls.findViewById(R.id.minutes);
        secondsView = (TextView) timerControls.findViewById(R.id.seconds);

		// ViewPager and pagerAdapter for slidy tabs!
        cpAdapter = new BrewTimerCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(cpAdapter);

        // Set on page change listener
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                f = (BrewTimerStepFragment) cpAdapter.getItem(position);
                i = f.getInstruction();

                if (timerState == STOPPED)
                {
                    hoursView.setText(nft.format(Utils.getHours(i.getDuration(), i.getDurationUnits())) + "");
                    minutesView.setText(nft.format(Utils.getMinutes(i.getDuration(), i.getDurationUnits())) + "");
                    secondsView.setText(nft.format(0) + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Set to the first item
        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);

        // Add views
        mainLayout.addView(timerControls);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.activity_display_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
    	switch (item.getItemId()) {
            case android.R.id.home:
        		finish();
        		return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	// Update what we display
		mRecipe = Utils.getRecipeWithId(id);

        // Set title based on recipe name
        setTitle("Brew Timer");

		// ViewPager and pagerAdapter for slidy tabs!
        cpAdapter = new BrewTimerCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
        mViewPager.setAdapter(cpAdapter);
        mViewPager.setCurrentItem(this.currentItem);
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	// Save the current page we're looking at
    	this.currentItem = mViewPager.getCurrentItem();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void togglePlayPause()
    {
        if (timerState == RUNNING)
            pause();
        else
            play();
    }

    public void play()
    {
        timerState = RUNNING;
        timerHandler.removeCallbacks(mUpdateTimeTask);
        timerHandler.postDelayed(mUpdateTimeTask, 1000);
    }

    public void pause()
    {
        timerState = PAUSED;
        timerHandler.removeCallbacks(mUpdateTimeTask);
    }

    public void stop()
    {
        timerState = STOPPED;
        timerHandler.removeCallbacks(mUpdateTimeTask);
        hoursView.setText(nft.format(Utils.getHours(i.getDuration(), i.getDurationUnits())) + "");
        minutesView.setText(nft.format(Utils.getMinutes(i.getDuration(), i.getDurationUnits())) + "");
        secondsView.setText(nft.format(0) + "");
    }

    public int getTimerSeconds()
    {
        int seconds = 0;

        seconds += Integer.parseInt(hoursView.getText().toString()) * 3600;
        seconds += Integer.parseInt(minutesView.getText().toString()) * 60;
        seconds += Integer.parseInt(secondsView.getText().toString());

        return seconds;
    }

    public void setTimerFromSeconds(int time)
    {
        int hours = 0, minutes = 0, seconds = 0;

        hours = (int) (time / 3600);
        minutes = (int) ((time - 3600 * hours) / 60);
        seconds = time - (hours * 3600) - (minutes * 60);

        java.text.DecimalFormat nft = new java.text.DecimalFormat("#00.###");
        nft.setDecimalSeparatorAlwaysShown(false);

        hoursView.setText(nft.format(hours) + "");
        minutesView.setText(nft.format(minutes) + "");
        secondsView.setText(nft.format(seconds) + "");
    }

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.play_pause_button:
            {
                togglePlayPause();
                break;
            }
            case R.id.next_button:
            {
                stop();
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                break;
            }
            case R.id.previous_button:
            {
                stop();
                break;
            }
        }

    }
}
