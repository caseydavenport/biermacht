package com.biermacht.brews.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
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

  // keeps track of whether or not we've started the notification.
  private boolean notificationStarted;

  // Intent to start the BrewTimerActivity.  This is created and maintained when
  // the notification is updated.
  private Intent brewTimerIntent;

  // Ringtone to use when a step completes and the alarm is triggered.
  private Ringtone ringtone;

  // Receive timer updates using a CountdownReceiver
  private CountdownReceiver countdownReceiver = new CountdownReceiver() {
    @Override
    public void onNewTime(int seconds) {
      if (seconds == 0) {
        // The timer has finished counting down - start the alarm.
        ringtone.play();
      }

      // Update the notification.
      updateNotification(notificationTitle, seconds);
    }
  };

  private BroadcastReceiver messageHandler = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String command = intent.getStringExtra(Constants.KEY_COMMAND);

      if (command.equals(Constants.COMMAND_STOP)) {
        // We've received a stop command - stop the timer.  No notification update is necessary,
        // as the service is about to be torn down by the BrewTimerActivity.
        Log.d("BrewTimerService", "Received command to stop timer");
        timer.stop();
      }
      else if (command.equals(Constants.COMMAND_PAUSE)) {
        // We've received a pause command - pause the timer, and update the notification
        // to include that the timer is paused.  Updating the notification is necessary, so that
        // the correct pending intent is stored.
        Log.d("BrewTimerService", "Received command to pause timer");
        timer.pause();
        updateNotification(notificationTitle, timer.remainingSeconds);
      }
      else if (command.equals(Constants.COMMAND_START)) {
        // Received a start command - start the timer.  No update to the notification is
        // necessary, as this will occur on each timer tick.  Get the remaining seconds from the
        // Intent.  If no timer is provided, use the current timer's remaining seconds.
        Log.d("BrewTimerService", "Received command to start timer");
        int seconds = intent.getIntExtra(Constants.KEY_SECONDS, timer.remainingSeconds);
        timer.start(seconds);
      }
      else if (command.equals(Constants.COMMAND_QUERY)) {
        // Received a query for current timer state - respond with the current timer state.
        Log.d("BrewTimerService", "Received command to query data");
        respondToQuery();
      }
      else if (command.equals(Constants.COMMAND_STOP_ALARM)) {
        Log.d("BrewTimerService", "Received command to stop alarm.");
        ringtone.stop();
      }
      else {
        Log.e("BrewTimerService", "Received unknown command: " + command);
      }
    }
  };

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent == null) {
      Log.e("BrewTimerService", "NULL intent passed to brew timer service.  Likely due to " +
              "a service restart.  Canceling the service!");
      stopSelf();
      return Service.START_STICKY;
    }

    // Use the service ID to keep track of our corresponding notification
    notificationId = startId;
    notificationStarted = false;

    // Get the desire title and time from the intent
    notificationTitle = intent.getStringExtra(Constants.KEY_TITLE);
    currentStepNumber = intent.getIntExtra(Constants.KEY_STEP_NUMBER, 0);
    r = intent.getParcelableExtra(Constants.KEY_RECIPE);
    int startTime = intent.getIntExtra(Constants.KEY_SECONDS, - 1);

    // Create and register a new Timer.  This will count down and broadcast the remaining time.
    timer = new Timer(this);

    // Register timer receivers
    registerReceivers();

    // Set up ringtone to alert user when timer is complete.
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    if (uri == null) {
      uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }
    ringtone = RingtoneManager.getRingtone(this, uri);

    // Start the timer
    timer.start(startTime);

    // Create the notification
    updateNotification(notificationTitle, startTime);

    // Indicate that the service is running
    BrewTimerService.isRunning = true;

    return Service.START_STICKY;
  }

  @Override
  public void onDestroy() {
    // Indicate that the service is no longer running
    BrewTimerService.isRunning = false;

    // Unregister receivers
    unregisterReceiver(countdownReceiver);
    unregisterReceiver(messageHandler);
  }

  public void registerReceivers() {
    // Register our receiver for remaining time broadcasts
    registerReceiver(countdownReceiver, new IntentFilter(Constants.BROADCAST_REMAINING_TIME));

    // Register for start, pause, and stop messages
    registerReceiver(messageHandler, new IntentFilter(Constants.BROADCAST_TIMER_CONTROLS));
  }

  /**
   * Broadcasts the current timer state to the QUERY_RESP broadcast destination.  This includes
   * the current Recipe, step number, remaining time, and the timer state (paused, running, etc).
   */
  public void respondToQuery() {
    Log.d("BrewTimerService", "Responding to query");
    Intent i = new Intent();
    i.setAction(Constants.BROADCAST_QUERY_RESP);
    i.putExtra(Constants.KEY_RECIPE, r);
    i.putExtra(Constants.KEY_STEP_NUMBER, currentStepNumber);
    i.putExtra(Constants.KEY_SECONDS, timer.remainingSeconds);
    i.putExtra(Constants.KEY_TIMER_STATE, timer.timerState);
    sendBroadcast(i);
  }

  /**
   * Updates the notification in the notification bar which shows the current step and the
   * remaining time left.  This is updated every time the timer ticks down, as well as in special
   * cases (like when the timer changes state).
   * @param title
   * @param remaining
   */
  public void updateNotification(String title, int remaining) {
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
    if (timer.timerState == Constants.PAUSED) {
      remainingTime = "Paused";
    }

    // Notification builders
    NotificationCompat.Builder nBuilder =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher_64)
                    .setContentTitle(title)
                    .setContentText(remainingTime);

    // Intent for when notification is clicked.
    brewTimerIntent = new Intent(this, BrewTimerActivity.class);
    brewTimerIntent.putExtra(Constants.KEY_RECIPE, r);
    brewTimerIntent.putExtra(Constants.KEY_STEP_NUMBER, currentStepNumber);
    brewTimerIntent.putExtra(Constants.KEY_TIMER_STATE, timer.timerState);
    brewTimerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pi = PendingIntent.getActivity(this, 0, brewTimerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    nBuilder.setContentIntent(pi);
    Notification note = nBuilder.build();
    note.flags |= Notification.FLAG_NO_CLEAR;

    // Start the notification
    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    if (! notificationStarted) {
      startForeground(notificationId, note);
      notificationStarted = true;
    }
    else {
      nm.notify(notificationId, note);
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    //TODO for communication return IBinder implementation
    return null;
  }
}


class Timer {
  // Keep track of timer state
  public int timerState;

  // Keep track of remaining seconds
  public int remainingSeconds = 0;

  // More private fields
  private Context c;
  CountDownTimer timer;

  public Timer(Context c) {
    super();
    this.c = c;
    this.timerState = Constants.STOPPED;
  }

  /**
   * Starts the timer, initializing the timer to the given number of seconds.  If the timer is
   * already running, this is a no-op.
   * @param seconds
   */
  public void start(int seconds) {

    // If the timer is not currently running, start it.
    if (timerState != Constants.RUNNING) {
      timerState = Constants.RUNNING;
      remainingSeconds = seconds;

      // Create a new countdown timer which starts at the given number of seconds, and counts
      // down at a rate of one tick per 1000 milliseconds.
      timer = new CountDownTimer(seconds * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
          remainingSeconds = (int) millisUntilFinished / 1000;
          broadcastTime();
        }

        @Override
        public void onFinish() {
          // When the timer is finished, advertise that there is no time left remaining.  This
          // case it not handled by onTick(), since it is never called with 0 time remaining.
          // Also, set the timer state to paused, since we are no longer counting down.
          timerState = Constants.PAUSED;
          remainingSeconds = 0;
          broadcastTime();
        }
      };
      timer.start();
    }
  }

  /**
   * An alternative to start(time) which starts the timer with the remaining number
   * of seconds left.
   */
  public void start() {
    start(remainingSeconds);
  }

  /**
   * Stops timer execution.  This cancels the current timer and sets the timerState
   * appropriately.
   */
  public void stop() {
    timerState = Constants.STOPPED;
    timer.cancel();
  }

  /**
   * Pauses timer execution. This cancels the current timer, and sets the timerState
   * appropriately.
   */
  public void pause() {
    timerState = Constants.PAUSED;
    timer.cancel();
  }

  /**
   * Broadcasts the current remaining time to the BrewTimerActivity, where it is displayed
   * in the timer view.
   */
  public void broadcastTime() {
    Intent i = new Intent();
    i.setAction(Constants.BROADCAST_REMAINING_TIME);
    i.putExtra(Constants.KEY_SECONDS, remainingSeconds);
    c.sendBroadcast(i);
  }
}