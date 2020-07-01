package com.biermacht.brews.frontend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.biermacht.brews.utils.ColorHandler;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.Utils;

public class BrewTimerActivity extends AppCompatActivity {

  // Stores the recipe which is being brewed - provided by the caller via the Intent which
  // started this Activity.
  private Recipe mRecipe;

  // Holds the index of the current active brew step.  E.g, the step which is currently
  // being performed.
  private int currentActivePosition;

  // Holds the index of the brew step currently being viewed, independent of the step which is
  // being performed (if there is one).
  private int currentSelectedPosition;

  // Holds the currently SELECTED (not actively counting down) fragment and instruction.
  BrewTimerStepFragment f;
  Instruction inst;

  // Adapter
  BrewTimerCollectionPagerAdapter cpAdapter;

  // Formatter for countdown timer views.
  java.text.DecimalFormat nft;

  // Indicates if timer is running, paused, or stopped.  Can be one of Constants.RUNNING,
  // Constants.STOPPED, or Constants.PAUSED.
  private int timerState;

  // Views
  ViewGroup mainLayout;
  ViewPager mViewPager;
  TextView hoursView;
  TextView minutesView;
  TextView secondsView;
  View timerControls;
  View countdownTimerView;
  TextView stepStatusView;

  // Buttons to control the timer.
  ImageButton stopButton;
  ImageButton playPauseButton;
  ImageButton goToCurrentButton;

  // LayoutInflater to inflate views.
  LayoutInflater inflater;

  // Application context
  private Context appContext;

  // Receive timer updates using a CountdownReceiver.  The timer is run in the BrewTimerService,
  // which advertises the timer ticks received by this CountDownReceiver.  The receiver then
  // updates the UI to indicate the new time.
  private CountdownReceiver countdownReceiver = new CountdownReceiver() {
    @Override
    public void onNewTime(int seconds) {

      // Set the display
      setTimerFromSeconds(seconds);

      // Time for this step is up
      if (seconds == 0) {
        onStepComplete();
      }
      else {
        // We're receiving ticks, set the button to be "pause"
        playPauseButton.setImageResource(R.drawable.ic_pause_black_48dp);
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

      // If this is not the recipe used by the current timer, alert the user and cancel the
      // activity.  We can't have two brew timers running at the same time.
      if (! qRecipe.equals(mRecipe)) {
        Log.e("BrewTimerActivity", "Currently running timer is not for this recipe, aborting");

        new AlertDialog.Builder(BrewTimerActivity.this)
                .setTitle("Cannot open timer")
                .setMessage("The brew timer is already running for another recipe.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                    finishWithoutStoppingService();
                  }
                }).show();
        return;
      }

      if (qCurrentStep != 0) {
        Log.d("BrewTimerActivity", "Setting active step panel to be #" + qCurrentStep);
        mViewPager.setCurrentItem(qCurrentStep);
      }

      if (qSeconds != 0) {
        Log.d("BrewTimerActivity", "Setting timer based on queried seconds: " + qSeconds);
        setTimerFromSeconds(qSeconds);
      }

      // Update status bar text.
      setStatusText();
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d("BrewTimerActivity", "Begin onCreate() for BrewTimerActivity");

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_brew_timer);

    // Set title for this activity.
    setTitle("Timer");

    // Timer is not yet running
    timerState = Constants.STOPPED;
    currentActivePosition = 0;

    // Configure formatter used for timer values
    nft = new java.text.DecimalFormat("#00.###");
    nft.setDecimalSeparatorAlwaysShown(false);

    // Set icon as back button
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // Get inflater
    inflater = getLayoutInflater();

    // Get main layout
    mainLayout = (LinearLayout) findViewById(R.id.main_layout);

    // Get context
    appContext = getApplicationContext();

    // Get recipe from the caller.
    mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);

    // Get the current timer state from the caller.  If no timer state is provided, default
    // to STOPPED.  A timer state will be provided when this activity is being created after
    // the user pressed the timer notification.
    timerState = getIntent().getIntExtra(Constants.KEY_TIMER_STATE, Constants.STOPPED);
    Log.d("BrewTimerActivity", "Initial timer state: " + timerState);

    // Get current step - if the timer is already running, this may be non-zero, in which case
    // we must set the pager adapter to the correct step after we create it.
    currentActivePosition = getIntent().getIntExtra(Constants.KEY_STEP_NUMBER, 0);
    currentSelectedPosition = currentActivePosition;

    // Inflate timer controls
    timerControls = inflater.inflate(R.layout.view_timer_controls, mainLayout, false);
    countdownTimerView = timerControls.findViewById(R.id.countdown_timer);

    // Get countdown views
    hoursView = (TextView) timerControls.findViewById(R.id.hours);
    minutesView = (TextView) timerControls.findViewById(R.id.minutes);
    secondsView = (TextView) timerControls.findViewById(R.id.seconds);

    // Set font face to be fixed width
    hoursView.setTypeface(Typeface.MONOSPACE);
    minutesView.setTypeface(Typeface.MONOSPACE);
    secondsView.setTypeface(Typeface.MONOSPACE);

    // Get buttons
    stopButton = (ImageButton) timerControls.findViewById(R.id.stop_button);
    playPauseButton = (ImageButton) timerControls.findViewById(R.id.play_pause_button);
    goToCurrentButton = (ImageButton) timerControls.findViewById(R.id.go_to_current_button);

    // Get view to display step status
    stepStatusView = (TextView) timerControls.findViewById(R.id.step_status);

    // Set default button backgrounds
    stopButton.setImageResource(R.drawable.ic_stop_black_48dp);
    playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);

    // ViewPager and pagerAdapter for slidy tabs!
    cpAdapter = new BrewTimerCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(cpAdapter);

    // Set on page change listener
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float offset, int offsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        Log.d("BrewTimerActivity", "Step #" + position + " selected.");

        // Acquire objects corresponding to the currently selected BrewStep page.
        f = (BrewTimerStepFragment) cpAdapter.getItem(position);
        inst = f.getInstruction();
        currentSelectedPosition = position;

        // If we're stopped, update some of our state whenever we switch instructions.  If we're
        // stopped, it means we can change the current active step and update the timerView
        // based on the remaining time for that step.
        if (timerState == Constants.STOPPED) {
          Log.d("BrewTimerActivity", "Timer is stopped - update.");
          currentActivePosition = mViewPager.getCurrentItem();
          setTimerFromCurrentStep();

          // If this instruction doesn't use a timer, hide the timer view.  Instructions might
          // not use timers if they are not time based. For example, sparge or cool steps.
          if (! inst.showTimer()) {
            Log.d("BrewTimerActivity", "Hiding timer for instruction: " + inst.getInstructionType());
            setTimerToNull();
          }
          else {
            Log.d("BrewTimerActivity", "Update timer view with new remaining time.");
            hoursView.setText(nft.format(Utils.getHours(inst.getTimeToNextStep(), inst.getDurationUnits())) + "");
            minutesView.setText(nft.format(Utils.getMinutes(inst.getTimeToNextStep(), inst.getDurationUnits())) + "");
            secondsView.setText(nft.format(0) + "");
          }
          goToCurrentButton.setImageResource(R.drawable.ic_check_black_48dp);
        }

        // Adjust fields based on the item we're looking at
        if (position == currentActivePosition) {
          goToCurrentButton.setImageResource(R.drawable.ic_check_black_48dp);
        }
        else if (position < currentActivePosition) {
          goToCurrentButton.setImageResource(R.drawable.ic_navigate_next_black_48dp);
        }
        else if (position > currentActivePosition) {
          goToCurrentButton.setImageResource(R.drawable.ic_navigate_before_black_48dp);
        }

        // Set text on the status bar
        setStatusText();
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    };
    mViewPager.addOnPageChangeListener(onPageChangeListener);

    // Set the view pager to the current active step, if there is one.  We need to trigger the
    // OnPageChangeListener to be called, so first setCurrentItem to a different page, and
    // then set it to the desired one.  We store the desired active step so that we can change
    // it back.
    Log.d("BrewTimerActivity", "Set the current brew step to be step #" + currentActivePosition);
    mViewPager.setCurrentItem(currentActivePosition);
    onPageChangeListener.onPageSelected(currentActivePosition);

    // Add views
    mainLayout.addView(timerControls);

    // Register listener for timer updates
    registerReceiver(countdownReceiver, new IntentFilter(Constants.BROADCAST_REMAINING_TIME));

    // Register listener for query responses
    registerReceiver(queryReceiver, new IntentFilter(Constants.BROADCAST_QUERY_RESP));

    // If the service is running, query it for current data
    if (BrewTimerService.isRunning) {
      Log.d("BrewTimerActivity", "BrewTimerService is running, query it for current info.");
      Intent i = new Intent();
      i.setAction(Constants.BROADCAST_TIMER_CONTROLS);
      i.putExtra(Constants.KEY_COMMAND, Constants.COMMAND_QUERY);
      appContext.sendBroadcast(i);

      // If the brew timer service is running, stop any alarms that might be active.  This might
      // occur if the BrewTimerActivity was destroyed while the service was still running, and
      // is being re-opened after a step has completed.
      stopAlarm();
    }

    Log.d("BrewTimerActivity", "End onCreate() for BrewTimerActivity");
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // Behave in the same way as if back was pressed.
        this.onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    // If the back button is pressed and the brew timer is running, ask
    // the user if they really want to exit the activity.  Otherwise, just
    // go back.
    if (this.timerState == Constants.STOPPED) {
      // Timer is not running - we can just exit the activity as normal.
      Log.d("BrewTimerService", "Back button pressed, finish().");
      finish();
    }
    else {
      // The timer is not in stopped state.  Ask the user if they really
      // want to leave!  If they pick yes, the timer will be canceled.
      Log.d("BrewTimerService", "Back button pressed, display alert.");
      new AlertDialog.Builder(BrewTimerActivity.this)
              .setTitle("Cancel Brew Timer?")
              .setMessage("Leaving this screen will cancel the current brew timer. Continue?")
              .setNegativeButton("Cancel", null)
              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  finish();
                }
              }).show();
    }
  }

  /**
   * The default finish() method.  This ends the activity, bringing the BrewTimerService with it.
   */
  @Override
  public void finish() {
    // Override finish to also stop the timer.
    if (timerState != Constants.STOPPED) {
      this.stop();
    }
    super.finish();
  }

  /**
   * Use this to finish the BrewTimerActivity without stopping the service.
   */
  public void finishWithoutStoppingService() {
    super.finish();
  }

  @Override
  public void onResume() {
    super.onResume();
    mRecipe = getIntent().getParcelableExtra(Constants.KEY_RECIPE);

    // Set title
    setTitle("Brew Timer");

    // ViewPager and pagerAdapter for slidy tabs!
    cpAdapter = new BrewTimerCollectionPagerAdapter(getSupportFragmentManager(), mRecipe, appContext);
    mViewPager.setAdapter(cpAdapter);
    mViewPager.setCurrentItem(this.currentActivePosition);
  }

  public void togglePlayPause() {
    if (timerState == Constants.RUNNING) {
      pause();
    }
    else {
      play();
    }
  }

  public void play() {
    mViewPager.setCurrentItem(currentActivePosition);
    stopAlarm();

    // If the timer is 0, don't start!
    if (getTimerSeconds() != 0) {
      // Set timer state
      timerState = Constants.RUNNING;
      playPauseButton.setImageResource(R.drawable.ic_pause_black_48dp);

      Intent i = new Intent(this, BrewTimerService.class);
      i.putExtra(Constants.KEY_TITLE, inst.getBrewTimerTitle());
      i.putExtra(Constants.KEY_SECONDS, getTimerSeconds());
      i.putExtra(Constants.KEY_RECIPE, mRecipe);
      i.putExtra(Constants.KEY_STEP_NUMBER, currentActivePosition);
      startService(i);
    }
    else {
      Log.d("BrewTimerService", "Play pressed when time remaining == 0.  Moving to next step.");
      currentActivePosition = currentActivePosition + 1;
      mViewPager.setCurrentItem(currentActivePosition);
      setTimerFromCurrentStep();
    }
    setStatusText();
  }

  public void pause() {
    // Tell the timer service that we're paused
    Intent i = new Intent();
    i.setAction(Constants.BROADCAST_TIMER_CONTROLS);
    i.putExtra(Constants.KEY_COMMAND, Constants.COMMAND_PAUSE);
    appContext.sendBroadcast(i);

    // Set the timer state
    timerState = Constants.PAUSED;

    // Jump to the current step
    mViewPager.setCurrentItem(currentActivePosition);

    // Set the play/pause button to display the "Play" image
    playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);

    // Set appropriate text on status bar
    setStatusText();
  }

  public void stop() {
    // Set the currently displayed page to be the first instruction.
    mViewPager.setCurrentItem(0);
    currentActivePosition = 0;

    // Tell the timer serviced that we've stopped
    Intent i = new Intent();
    i.setAction(Constants.BROADCAST_TIMER_CONTROLS);
    i.putExtra(Constants.KEY_COMMAND, Constants.COMMAND_STOP);
    appContext.sendBroadcast(i);

    // Set the timer state
    timerState = Constants.STOPPED;

    // Set timer to initial duration
    setTimerFromCurrentStep();

    // Set play/pause button to display the "Play" image
    playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);

    // Stop any alarms
    stopAlarm();

    // Stop the timer service
    stopService(new Intent(this, BrewTimerService.class));

    // Set appropriate text on status bar
    setStatusText();
  }

  public void setStatusText() {
    if (currentSelectedPosition == currentActivePosition) {
      if (timerState == Constants.RUNNING) {
        stepStatusView.setText("IN PROGRESS");
        stepStatusView.setTextColor(Color.parseColor(ColorHandler.GREEN));
      }
      else if (timerState == Constants.PAUSED) {
        if (getTimerSeconds() == 0) {
          // If the timer is paused and the remaining time is 0, then it means
          // this step is complete.
          stepStatusView.setText("COMPLETE");
          stepStatusView.setTextColor(Color.parseColor(ColorHandler.GREEN));
        }
        else {
          // Otherwise, it means that the timer is just paused, and that this step is not
          // yet complete.
          stepStatusView.setText("PAUSED");
          stepStatusView.setTextColor(Color.parseColor(ColorHandler.RED));
        }
      }
      else if (timerState == Constants.STOPPED) {
        stepStatusView.setText("");
      }
    }
    else if (currentSelectedPosition < currentActivePosition) {
      if (timerState == Constants.RUNNING || timerState == Constants.PAUSED) {
        stepStatusView.setText("COMPLETE");
        stepStatusView.setTextColor(Color.parseColor(ColorHandler.GREEN));
      }
      else if (timerState == Constants.STOPPED) {
        stepStatusView.setText("");
      }
    }
    else if (currentSelectedPosition > currentActivePosition) {
      if (timerState == Constants.STOPPED) {
        stepStatusView.setText("");
      }
      else {
        stepStatusView.setText("PENDING");
        stepStatusView.setTextColor(Color.parseColor(ColorHandler.YELLOW));
      }
    }
  }

  public void onStepComplete() {
    // When we get to the next step, raise alarm, pause timer, switch views, update displayed time.
    pause();

    // Turn screen on
    Window wind;
    wind = this.getWindow();
    wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
  }

  public int getTimerSeconds() {
    int seconds = 0;

    try {
      seconds += Integer.parseInt(hoursView.getText().toString()) * 3600;
      seconds += Integer.parseInt(minutesView.getText().toString()) * 60;
      seconds += Integer.parseInt(secondsView.getText().toString());
    } catch (Exception e) {
      Log.d("BrewTimerActivity", "Couldn't get timer seconds, returning 0");
      return 0;
    }

    return seconds;
  }

  public void setTimerFromSeconds(int time) {
    int hours = 0, minutes = 0, seconds = 0;

    hours = (int) (time / 3600);
    minutes = (int) ((time - 3600 * hours) / 60);
    seconds = time - (hours * 3600) - (minutes * 60);

    hoursView.setText(nft.format(hours) + "");
    minutesView.setText(nft.format(minutes) + "");
    secondsView.setText(nft.format(seconds) + "");
  }

  public void setTimerFromCurrentStep() {
    if (! inst.showTimer()) {
      setTimerToNull();
    }
    else {
      hoursView.setText(nft.format(Utils.getHours(inst.getTimeToNextStep(), inst.getDurationUnits())) + "");
      minutesView.setText(nft.format(Utils.getMinutes(inst.getTimeToNextStep(), inst.getDurationUnits())) + "");
      secondsView.setText(nft.format(0) + "");
    }
  }

  public void setTimerToNull() {
    hoursView.setText("--");
    minutesView.setText("--");
    secondsView.setText("--");
  }

  public void stopAlarm() {
    // Tell the timer service to stop the alarm.
    Intent i = new Intent();
    i.setAction(Constants.BROADCAST_TIMER_CONTROLS);
    i.putExtra(Constants.KEY_COMMAND, Constants.COMMAND_STOP_ALARM);
    appContext.sendBroadcast(i);
  }

  @Override
  protected void onDestroy() {
    // Stop any running alarm
    stopAlarm();

    // Unregister from our receivers
    unregisterReceiver(queryReceiver);
    unregisterReceiver(countdownReceiver);
    super.onDestroy();
  }

  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.play_pause_button: {
        togglePlayPause();
        break;
      }
      case R.id.go_to_current_button: {
        mViewPager.setCurrentItem(currentActivePosition);
        stopAlarm();
        break;
      }
      case R.id.stop_button: {
        stop();
        break;
      }
    }
  }
}