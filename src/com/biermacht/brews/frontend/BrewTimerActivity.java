package com.biermacht.brews.frontend;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
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
import android.widget.Toast;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.BrewTimerCollectionPagerAdapter;
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

    // ID used for the notification
    private static int NOTIFICATION_ID = 31;

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
    Instruction i;

    // Application context
	private Context appContext;

    // Broadcast receiver
    private class BCReceiver extends BroadcastReceiver
    {
        int seconds;

        @Override
        public void onReceive(Context c, Intent i)
        {
            // Set the timer
            seconds = getTimerSeconds() - 1;
            setTimerFromSeconds(seconds);

            // Fragment and instruction for the step we are currently counting down
            BrewTimerStepFragment frag = (BrewTimerStepFragment) cpAdapter.getItem(currentItem);
            Instruction ci = frag.getInstruction();

            // When we get to the next step, raise alarm, pause timer,
            // and switch views
            if (seconds == Units.toSeconds(ci.getNextDuration(), ci.getDurationUnits()))
            {
                pause();
                startAlarm();
                currentItem = currentItem + 1;
                mViewPager.setCurrentItem(currentItem);
            }

            // Stop if we've reached the end of a section
            if (seconds == 0)
                stop();

            // If the timer is running, schedule a new tick
            if (timerState == RUNNING)
                am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pi );
        }
    }

    // Receiver + alarm manager + intent + wakelock stuff
    private BCReceiver bcr;
    AlarmManager am;
    PendingIntent pi;
    PowerManager pm;
    PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_timer);

        // Timer is not yet running
        timerState = STOPPED;
        currentItem = 0;

        // Broadcast receiver, alarm manager, intent, for counting down
        bcr = new BCReceiver();
        registerReceiver(bcr, new IntentFilter(Utils.BROADCASE_TIMER) );
        pi = PendingIntent.getBroadcast(this, 0, new Intent(Utils.BROADCASE_TIMER), 0);
        am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BrewTimerWakeLock");

        // Configure formatter used for timer values
        nft = new java.text.DecimalFormat("#00.###");
        nft.setDecimalSeparatorAlwaysShown(false);

        // Set up ringtone alarm for alerts
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        
		// Set icon as back button
		getActionBar().setDisplayHomeAsUpEnabled(true);

        // Get inflater
        inflater = getLayoutInflater();

        // Get main layout
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        // Get context
        appContext = getApplicationContext();
        
        // Get recipe from calling activity
        id = getIntent().getLongExtra(Utils.INTENT_RECIPE_ID, Utils.INVALID_ID);
        mRecipe = Utils.getRecipeWithId(id);

        // Set title based on recipe name
        setTitle("Brew Timer");

        // Inflate timer controls
        timerControls = inflater.inflate(R.layout.view_timer_controls, mainLayout, false);

        // get countdown views
        hoursView = (TextView) timerControls.findViewById(R.id.hours);
        minutesView = (TextView) timerControls.findViewById(R.id.minutes);
        secondsView = (TextView) timerControls.findViewById(R.id.seconds);

        // Get buttons
        stopButton = (ImageButton) timerControls.findViewById(R.id.stop_button);
        playPauseButton = (ImageButton) timerControls.findViewById(R.id.play_pause_button);
        goToCurrentButton = (ImageButton) timerControls.findViewById(R.id.go_to_current_button);

        // Set button backrounds
        stopButton.setImageResource(R.drawable.av_stop);
        playPauseButton.setImageResource(R.drawable.av_play);

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
                    currentItem = mViewPager.getCurrentItem();
                    hoursView.setText(nft.format(Utils.getHours(i.getDuration(), i.getDurationUnits())) + "");
                    minutesView.setText(nft.format(Utils.getMinutes(i.getDuration(), i.getDurationUnits())) + "");
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
                if (wakeLock.isHeld())
                    wakeLock.release();
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
        stopAlarm();
    	
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
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
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
        mViewPager.setCurrentItem(currentItem);
        am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pi );
        playPauseButton.setImageResource(R.drawable.av_pause);
        wakeLock.acquire();
        stopAlarm();
    }

    public void pause()
    {
        timerState = PAUSED;
        mViewPager.setCurrentItem(currentItem);
        playPauseButton.setImageResource(R.drawable.av_play);
        stopAlarm();
    }

    public void stop()
    {
        timerState = STOPPED;
        mViewPager.setCurrentItem(0);
        hoursView.setText(nft.format(Utils.getHours(i.getDuration(), i.getDurationUnits())) + "");
        minutesView.setText(nft.format(Utils.getMinutes(i.getDuration(), i.getDurationUnits())) + "");
        secondsView.setText(nft.format(0) + "");
        playPauseButton.setImageResource(R.drawable.av_play);
        stopAlarm();
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

    public void startAlarm()
    {
        Context ctx = getApplicationContext();

        Intent notificationIntent = new Intent(ctx, BrewTimerActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra(Utils.INTENT_RECIPE_ID, mRecipe.getId());

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = ctx.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.icon_timer_light)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.icon_timer_light))
                .setTicker("Brew step complete")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Step complete!")
                .setContentText("Click to return to brew timer")
                .addAction(R.drawable.icon_timer_light, "Title", contentIntent);

        nm.notify(NOTIFICATION_ID, builder.build());

        // Turn screen on
        Window wind;
        wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        ringtone.play();
    }

    // Clears the notification in the notification bar that is created
    // when we start the alarm
    public void clearNotification()
    {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    // Turns off the ringing alarm, then clears the notification
    public void stopAlarm()
    {
        ringtone.stop();
        clearNotification();
    }

    @Override
    protected void onDestroy() {
        am.cancel(pi);
        unregisterReceiver(bcr);
        if (wakeLock.isHeld())
            wakeLock.release();
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
                if (wakeLock.isHeld())
                    wakeLock.release();
                stop();
                break;
            }
        }

    }
}
