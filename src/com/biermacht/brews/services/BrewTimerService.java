package com.biermacht.brews.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.BrewTimerActivity;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;

public class BrewTimerService extends Service {

    // Flag to indicate if service is running
    public static boolean isRunning = false;

    // Create a timer object to count down with
    private Timer timer;

    // Current notification title
    String notificationTitle;

    // Identifier for our notification
    private int notificationId;

    // Recipe currently being timed
    private Recipe r;

    // Current step
    private int currentStepNumber;

    // Receive timer updates using a CountdownReceiver
    private CountdownReceiver countdownReceiver = new CountdownReceiver() {
        @Override
        public void onNewTime(int seconds) {
            updateNotification(notificationTitle, seconds);
        }
    };

    private BroadcastReceiver messageHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getStringExtra(Constants.KEY_COMMAND);

            if (command.equals(Constants.COMMAND_STOP)){
                Log.d("BrewTimerService", "Received command to stop timer");
                timer.stop();
            }
            else if (command.equals(Constants.COMMAND_PAUSE)){
                Log.d("BrewTimerService", "Received command to pause timer");
                timer.pause();
            }
            else if (command.equals(Constants.COMMAND_START)) {
                Log.d("BrewTimerService", "Received command to start timer");
                int seconds = intent.getIntExtra(Constants.KEY_SECONDS, timer.remainingSeconds);
                timer.start(seconds);
            }
            else if (command.equals(Constants.COMMAND_QUERY))
            {
                Log.d("BrewTimerService", "Received command to query data");
                respondToQuery();
            }
            else {
                Log.e("BrewTimerService", "Received unknown command: " + command);
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null)
        {
            Log.e("BrewTimerService", "NULL intent passed to brew timer service.  Likely due to " +
                    "a service restart.  Canceling the service!");
            stopSelf();
            return Service.START_NOT_STICKY;
        }

        // Use the service ID to keep track of our corresponding notification
        notificationId = startId;

        // Get the desire title and time from the intent
        notificationTitle = intent.getStringExtra(Constants.KEY_TITLE);
        currentStepNumber = intent.getIntExtra(Constants.KEY_STEP_NUMBER, 0);
        r = intent.getParcelableExtra(Constants.KEY_RECIPE);
        int startTime = intent.getIntExtra(Constants.KEY_SECONDS, -1);

        // Create the notification
        updateNotification(notificationTitle, startTime);

        // Register timer receivers
        registerReceivers();

        // Start the timer
        timer.start(startTime);

        // Indicate that the service is running
        BrewTimerService.isRunning = true;

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        // Indicate that the service is no longer running
        BrewTimerService.isRunning = false;

        // Unregister receivers
        unregisterReceiver(countdownReceiver);
        unregisterReceiver(timer);
        unregisterReceiver(messageHandler);
    }

    public void registerReceivers()
    {
        // Create and register a new Timer.  This will count down and broadcast the remaining time.:w
        timer = new Timer(this);
        registerReceiver(timer, new IntentFilter(Constants.BROADCAST_TIMER));

        // Register our receiver for remaining time broadcasts
        registerReceiver(countdownReceiver, new IntentFilter(Constants.BROADCAST_REMAINING_TIME));

        // Register for start, pause, and stop messages
        registerReceiver(messageHandler, new IntentFilter(Constants.BROADCAST_TIMER_CONTROLS));
    }

    public void respondToQuery()
    {
        Log.d("BrewTimerService", "Responding to query");
        Intent i = new Intent();
        i.setAction(Constants.BROADCAST_QUERY_RESP);
        i.putExtra(Constants.KEY_RECIPE, r);
        i.putExtra(Constants.KEY_STEP_NUMBER, currentStepNumber);
        i.putExtra(Constants.KEY_SECONDS, timer.remainingSeconds);
        i.putExtra(Constants.KEY_TIMER_STATE, timer.timerState);
        sendBroadcast(i);
    }

    public void updateNotification(String title, int remaining)
    {
        // Build remaining time string
        java.text.DecimalFormat nft = new java.text.DecimalFormat("#00.###");
        nft.setDecimalSeparatorAlwaysShown(false);

        // Grab hours, minutes, and seconds
        int hours = (int) (remaining / 3600);
        int minutes = (int) ((remaining - 3600 * hours) / 60);
        int seconds = remaining - (hours * 3600) - (minutes * 60);

        String remainingTime = "Next step in: " +
                               nft.format(hours) + ":" +
                               nft.format(minutes) + ":" +
                               nft.format(seconds);

        // If no time remains, the timer is paused.
        if (remaining == 0) {
            remainingTime = "Paused";
        }

        //; Notification builders
        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_64)
                        .setContentTitle(title)
                        .setContentText(remainingTime);

        // Intent for when notification is clicked.
        Intent i = new Intent(this, BrewTimerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        nBuilder.setContentIntent(pi);
        Notification note = nBuilder.build();
        note.flags |= Notification.FLAG_NO_CLEAR;

        // Start the notification
        startForeground(notificationId, note);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}

// Broadcast receiver
class Timer extends BroadcastReceiver
{
    // Keep track of timer state
    public int timerState = Constants.STOPPED;

    // Keep track of remaining seconds
    public int remainingSeconds = 0;

    // More private fields
    private Context c;
    AlarmManager alarmManager;
    PendingIntent pendingTimerIntent;

    public Timer(Context c)
    {
        super();
        this.c = c;
        this.alarmManager = (AlarmManager)(c.getSystemService(Context.ALARM_SERVICE));
        this.pendingTimerIntent = PendingIntent.getBroadcast(c, 0, new Intent(Constants.BROADCAST_TIMER), 0);

    }

    public void start(int seconds)
    {
        if (timerState != Constants.RUNNING) {
            timerState = Constants.RUNNING;
            remainingSeconds = seconds;
            this.alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 1000,
                    pendingTimerIntent);
        }
    }

    // Alternative to start(time) which just continues using the current
    // remaining seconds.
    public void start()
    {
        start(remainingSeconds);
    }

    public void stop()
    {
        timerState = Constants.STOPPED;
    }

    public void pause()
    {
        timerState = Constants.PAUSED;
    }

    public void broadcastTime()
    {
        Intent i = new Intent();
        i.setAction(Constants.BROADCAST_REMAINING_TIME);
        i.putExtra(Constants.KEY_SECONDS, remainingSeconds);
        c.sendBroadcast(i);
    }

    @Override
    public void onReceive(Context c, Intent i)
    {
        // If we're not running, stop ticking down.
        if (timerState != Constants.RUNNING)
            return;

        // Decrement current seconds
        remainingSeconds = remainingSeconds - 1;

        // Broadcast time
        broadcastTime();

        // Stop when we reach zero seconds remaining
        if (remainingSeconds <= 0)
            stop();

        // If the timer is running, schedule a new tick
        if (timerState == Constants.RUNNING)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                             SystemClock.elapsedRealtime() + 1000,
                             pendingTimerIntent);
    }
}