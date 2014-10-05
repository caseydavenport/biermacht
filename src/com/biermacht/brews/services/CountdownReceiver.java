package com.biermacht.brews.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.biermacht.brews.utils.Constants;

public abstract class CountdownReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context c, Intent i)
    {
        int seconds = i.getIntExtra(Constants.KEY_SECONDS, 0);
        onNewTime(seconds);
    }

    public abstract void onNewTime(int seconds);
}
