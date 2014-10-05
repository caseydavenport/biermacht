package com.biermacht.brews.frontend;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.BrewTimerCollectionPagerAdapter;
import com.biermacht.brews.frontend.fragments.BrewTimerStepFragment;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.services.BrewTimerService;
import com.biermacht.brews.services.CountdownReceiver;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Utils;

public class BrewTimerActivity extends FragmentActivity {
	
	private Recipe mRecipe;
	private long id; // id of recipe we use
	private int currentItem; // For storing current page being viewed
	BrewTimerCollectionPagerAdapter cpAdapter;

    // Formatter for timer values
    java.text.DecimalFormat nft;

    // True if timer is running, paused, or stopped
    private int timerState;

    // Views
    ViewGroup mainLayout;
    ViewPager mViewPager;
    TextView hoursView;
    TextView minutesView;
    TextView secondsView;
    View timerControls;

    // Buttons
    ImageButton stopButton;
    ImageButton playPauseButton;
    ImageButton goToCurrentButton;

    // LayoutInflater
    LayoutInflater inflater;

    // Ringtone
    private Ringtone ringtone;

    // Currently SELECTED (not counting down) fragment and instruction
    BrewTimerStepFragment f;
    Instruction inst;

    // Application context
	private Context appContext;

    // Receive timer updates using a CountdownReceiver
    private CountdownReceiver countdownReceiver = new CountdownReceiver() {
        @Override
        public void onNewTime(int seconds) {
            // Set the display
            setTimerFromSeconds(seconds);

            // Time for this step is up
            if (seconds == 0){
                onStepComplete();
            }
            else {
                // We're receiving ticks, set the button to be "pause"
                playPauseButton.setImageResource(R.drawable.av_pause);
                timerState = Constants.RUNNING;
            }
        }
    };

    private BroadcastReceiver queryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BrewTimerActivity", "Received query response");
            Recipe qRecipe = intent.getParcelableExtra(Constants.KEY_RECIPE);
            int qCurrentStep = intent.getIntExtra(Constants.KEY_STEP_NUMBER, 0);
            int qSeconds = intent.getIntExtra(Constants.KEY_SECONDS, 0);
            int qTimerState = intent.getIntExtra(Constants.KEY_TIMER_STATE, Constants.STOPPED);

            // Set timer state
            timerState = qTimerState;

            if (!qRecipe.equals(mRecipe))
            {
                Log.e("BrewTimerActivity", "Current running timer is not for this recipe, aborting");
                finish();
            }

            if (qCurrentStep != 0)
            {
                Log.d("BrewTimerActivity", "Setting active step panel to be #" + qCurrentStep);
                mViewPager.setCurrentItem(qCurrentStep);
            }

            if (qSeconds != 0)
            {
                Log.d("BrewTimerActivity", "Setting timer based on queried seconds: " + qSeconds);
                setTimerFromSeconds(qSeconds);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_timer);

        // Timer is not yet running
        timerState = Constants.STOPPED;
        currentItem = 0;

        // Configure formatter used for timer values
        nft = new java.text.DecimalFormat("#00.###");
        nft.setDecimalSeparatorAlwaysShown(false);

        // Set up ringtone alarm for alerts
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
        
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get inflater
        inflater = getLayoutInflater();

        // Get main layout
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        // Get context
        appContext = getApplicationContext();
        
        // Get recipe from calling activity
        id = getIntent().getLongExtra(Constants.KEY_RECIPE_ID, Constants.INVALID_ID);
        mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);

        // Set title
        setTitle("Timer");

        // Inflate timer controls
        timerControls = inflater.inflate(R.layout.view_timer_controls, mainLayout, false);

        // Get countdown views
        hoursView = (TextView) timerControls.findViewById(R.id.hours);
        minutesView = (TextView) timerControls.findViewById(R.id.minutes);
        secondsView = (TextView) timerControls.findViewById(R.id.seconds);

        // Get buttons
        stopButton = (ImageButton) timerControls.findViewById(R.id.stop_button);
        playPauseButton = (ImageButton) timerControls.findViewById(R.id.play_pause_button);
        goToCurrentButton = (ImageButton) timerControls.findViewById(R.id.go_to_current_button);

        // Set default button backgrounds
        stopButton.setImageResource(R.drawable.av_stop);
        playPauseButton.setImageResource(R.drawable.av_play);

		// ViewPager and pagerAdapter for slidy tabs!
        cpAdapter = new BrewTimerCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(cpAdapter);

        // Set on page change listener
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                f = (BrewTimerStepFragment) cpAdapter.getItem(position);
                inst = f.getInstruction();

                if (timerState == Constants.STOPPED)
                {
                    currentItem = mViewPager.getCurrentItem();
                    hoursView.setText(nft.format(Utils.getHours(inst.getDuration(), inst.getDurationUnits())) + "");
                    minutesView.setText(nft.format(Utils.getMinutes(inst.getDuration(), inst.getDurationUnits())) + "");
                    secondsView.setText(nft.format(0) + "");

                    double nextTime = inst.getDuration() - inst.getNextDuration();

                    hoursView.setText(nft.format(Utils.getHours(nextTime, inst.getDurationUnits())) + "");
                    minutesView.setText(nft.format(Utils.getMinutes(nextTime, inst.getDurationUnits())) + "");
                    secondsView.setText(nft.format(0) + "");

                    goToCurrentButton.setImageResource(R.drawable.navigation_accept);
                }

                if (position == currentItem)
                    goToCurrentButton.setImageResource(R.drawable.navigation_accept);
                else if (position < currentItem)
                    goToCurrentButton.setImageResource(R.drawable.navigation_next_item);
                else if (position > currentItem)
                    goToCurrentButton.setImageResource(R.drawable.navigation_previous_item);
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

        // Register listener for timer updates
        registerReceiver(countdownReceiver, new IntentFilter(Constants.BROADCAST_REMAINING_TIME));

        // Register listener for query responses
        registerReceiver(queryReceiver, new IntentFilter(Constants.BROADCAST_QUERY_RESP));

        // If the service is running, query it for current data
        if (BrewTimerService.isRunning)
        {
            Intent i = new Intent();
            i.setAction(Constants.BROADCAST_TIMER_CONTROLS);
            i.putExtra(Constants.KEY_COMMAND, Constants.COMMAND_QUERY);
            appContext.sendBroadcast(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public void onResume()
    {
    	super.onResume();
        mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);

        // Set title
        setTitle("Brew Timer");

		// ViewPager and pagerAdapter for slidy tabs!
        cpAdapter = new BrewTimerCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
        mViewPager.setAdapter(cpAdapter);
        mViewPager.setCurrentItem(this.currentItem);
    }

    public void togglePlayPause()
    {
        if (timerState == Constants.RUNNING)
            pause();
        else
            play();
    }

    public void play()
    {
        mViewPager.setCurrentItem(currentItem);

        // If the timer is 0, don't start!
        if (getTimerSeconds() != 0)
        {
            // Set timer state
            timerState = Constants.RUNNING;
            playPauseButton.setImageResource(R.drawable.av_pause);

            Intent i = new Intent(this, BrewTimerService.class);
            i.putExtra(Constants.KEY_TITLE, inst.getBrewTimerTitle());
            i.putExtra(Constants.KEY_SECONDS, getTimerSeconds());
            i.putExtra(Constants.KEY_RECIPE, mRecipe);
            i.putExtra(Constants.KEY_STEP_NUMBER, currentItem);
            startService(i);
        }
        stopAlarm();
    }

    public void pause()
    {
        // Tell the timer service that we're paused
        Intent i = new Intent();
        i.setAction(Constants.BROADCAST_TIMER_CONTROLS);
        i.putExtra(Constants.KEY_COMMAND, Constants.COMMAND_PAUSE);
        appContext.sendBroadcast(i);

        // Set the timer state
        timerState = Constants.PAUSED;

        // Jump to the current step
        mViewPager.setCurrentItem(currentItem);

        // Set the play/pause button to display the "Play" image
        playPauseButton.setImageResource(R.drawable.av_play);
        stopAlarm();
    }

    public void stop()
    {
        // Tell the timer serviced that we've stopped
        Intent i = new Intent();
        i.setAction(Constants.BROADCAST_TIMER_CONTROLS);
        i.putExtra(Constants.KEY_COMMAND, Constants.COMMAND_STOP);
        appContext.sendBroadcast(i);

        // Set the timer state
        timerState = Constants.STOPPED;

        // Set timer to initial duration
        hoursView.setText(nft.format(Utils.getHours(inst.getDuration(), inst.getDurationUnits())) + "");
        minutesView.setText(nft.format(Utils.getMinutes(inst.getDuration(), inst.getDurationUnits())) + "");
        secondsView.setText(nft.format(0) + "");

        // Set play/pause button to display the "Play" image
        playPauseButton.setImageResource(R.drawable.av_play);

        // Stop the timer service
        stopService(new Intent(this, BrewTimerService.class));
    }

    public void onStepComplete()
    {
        // When we get to the next step, raise alarm, pause timer,
        // and switch views
         pause();
         startAlarm();

        currentItem = currentItem + 1;
        mViewPager.setCurrentItem(currentItem);
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

        hoursView.setText(nft.format(hours) + "");
        minutesView.setText(nft.format(minutes) + "");
        secondsView.setText(nft.format(seconds) + "");
    }

    public void startAlarm()
    {
        // Turn screen on
        Window wind;
        wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // Play the alarm
        ringtone.play();
    }

    // Turns off the ringing alarm
    public void stopAlarm()
    {
        ringtone.stop();
    }

    @Override
    protected void onDestroy()
    {
        // Unregister from our receivers
        unregisterReceiver(queryReceiver);
        unregisterReceiver(countdownReceiver);

        stopAlarm();
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.play_pause_button:
            {
                togglePlayPause();
                break;
            }
            case R.id.go_to_current_button:
            {
                mViewPager.setCurrentItem(currentItem);
                stopAlarm();
                break;
            }
            case R.id.stop_button:
            {
                mViewPager.setCurrentItem(0);
                stopAlarm();
                stop();
                break;
            }
        }

    }
}

/**
 * // Set the timer
 seconds = getTimerSeconds() - 1;
 nextStepSeconds = getNextStepTimerSeconds() - 1;
 setTimerFromSeconds(seconds);
 setNextStepTimerFromSeconds(nextStepSeconds);

 // Fragment and instruction for the step we are currently counting down
 BrewTimerStepFragment frag = (BrewTimerStepFragment) cpAdapter.getItem(currentItem);
 BrewTimerStepFragment nextFrag = (BrewTimerStepFragment) cpAdapter.getItem(currentItem + 1);
 Instruction ci = frag.getInstruction();
 Instruction ni = nextFrag.getInstruction();
 nextTime = ni.getDuration() - ni.getNextDuration();

 // When we get to the next step, raise alarm, pause timer,
 // and switch views
 if (seconds == Units.toSeconds(ci.getNextDuration(), ci.getDurationUnits()))
 {
 pause();
 startAlarm();

 nextStepHoursView.setText(nft.format(Utils.getHours(nextTime, ci.getDurationUnits())) + "");
 nextStepMinutesView.setText(nft.format(Utils.getMinutes(nextTime, ci.getDurationUnits())) + "");
 nextStepSecondsView.setText(nft.format(0) + "");
 currentItem = currentItem + 1;
 mViewPager.setCurrentItem(currentItem);
 }
 **/