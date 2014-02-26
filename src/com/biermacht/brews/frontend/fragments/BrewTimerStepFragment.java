package com.biermacht.brews.frontend.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.provider.CalendarContract;
import android.provider.CalendarContract.*;

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class BrewTimerStepFragment extends Fragment {

	private int resource =  R.layout.fragment_brew_timer_step;
	private Recipe r;
    private Instruction i;
    private Context c;

    // Views
    ScrollView scrollView;
    ViewGroup pageView;
    TextView titleView;
    TextView descriptionView;
    ImageButton calendarButton;

    public static BrewTimerStepFragment newInstance(Recipe r, Instruction i)
    {
        Bundle bundle;
        BrewTimerStepFragment f = new BrewTimerStepFragment();

        bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_RECIPE, r);
        bundle.putParcelable(Constants.KEY_INSTRUCTION, i);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public String toString()
    {
        // For some reason we're calling this before onCreateView?
        if (this.i == null || this.r == null)
        {
            this.r = getArguments().getParcelable(Constants.KEY_RECIPE);
            this.i = getArguments().getParcelable(Constants.KEY_INSTRUCTION);
        }
        return i.getInstructionType();
    }

    public Instruction getInstruction()
    {
        // For some reason we're calling this before onCreateView?
        if (this.i == null || this.r == null)
        {
            this.r = getArguments().getParcelable(Constants.KEY_RECIPE);
            this.i = getArguments().getParcelable(Constants.KEY_INSTRUCTION);
        }
        return this.i;
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        // Get arguments
        this.r = getArguments().getParcelable(Constants.KEY_RECIPE);
        this.i = getArguments().getParcelable(Constants.KEY_INSTRUCTION);
        c = getActivity();

        // Get views
        scrollView = (ScrollView) inflater.inflate(resource, container, false);
		pageView = (LinearLayout) scrollView.findViewById(R.id.main_layout);
        titleView = (TextView) pageView.findViewById(R.id.title);
        descriptionView = (TextView) pageView.findViewById(R.id.description);
        calendarButton = (ImageButton) pageView.findViewById(R.id.calendar_button);

        // Only display calendar if this is a calendar instruction...
        if (i.getInstructionType().equals(Instruction.TYPE_CALENDAR))
        {
            calendarButton.setVisibility(View.VISIBLE);
            calendarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) 
                {
                	addToCalendarAlert().show();
                }
            });
        }

        descriptionView.setText(i.getBrewTimerText());

        View ingredientView;
        TextView ingredientViewTitle;
        TextView ingredientViewText;
        for (Ingredient ing : i.getRelevantIngredients())
        {
            ingredientView = inflater.inflate(R.layout.row_layout_edit_text, pageView, false);
            ingredientViewTitle = (TextView) ingredientView.findViewById(R.id.title);
            ingredientViewText = (TextView) ingredientView.findViewById(R.id.text);

            ingredientViewTitle.setText(ing.getName());
            ingredientViewText.setText(String.format("%2.2f", ing.getDisplayAmount())
                                       + " " + ing.getDisplayUnits());

            pageView.addView(ingredientView);
        }

        // Set view text
        titleView.setText(i.getBrewTimerTitle());
        titleView.setVisibility(View.GONE);

        // Turn off options menu
		setHasOptionsMenu(false);
		
		return scrollView;
	}

    public void createCalendarItems()
    {
        if (getCalendarId() == -1)
        {
            Log.d("BrewTimerActivity", "Calendar does not exist, creating");
            createCalendar();
        }

        if (getCalendarId() == -1)
        {
            Log.d("BrewTimerActivity", "Failed to create calendar, returning");
            return;
        }
        Log.d("BrewTimerActivity", "Adding events to calendar");
        createEvents();
    }

    private long getCalendarId() {
        String[] projection = new String[]{Calendars._ID};
        //String selection = Calendars.ACCOUNT_NAME + "=Biermacht AND" + Calendars.ACCOUNT_TYPE + "=" + CalendarContract.ACCOUNT_TYPE_LOCAL;

        String selection = "(" + Calendars.ACCOUNT_NAME + " = ?) AND (" + Calendars.ACCOUNT_TYPE + " = ?)";
        String[] selectionArgs = new String[] {"Biermacht", CalendarContract.ACCOUNT_TYPE_LOCAL};

        // use the same values as above:
        //String[] selArgs = new String[]{"Biermacht", CalendarContract.ACCOUNT_TYPE_LOCAL};
        Cursor cursor = c.getContentResolver(). query(Calendars.CONTENT_URI,
                                                        projection,
                                                        selection,
                                                        selectionArgs,
                                                        null);
        if (cursor.moveToFirst())
        {
            return cursor.getLong(0);
        }
        return -1;
    }

    private void createCalendar()
    {
        ContentValues values = new ContentValues();
        values.put(Calendars.ACCOUNT_NAME, "Biermacht");
        values.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(Calendars.NAME, "Biermacht Calendar");
        values.put(Calendars.CALENDAR_DISPLAY_NAME, "Biermacht Calendar");
        values.put(Calendars.CALENDAR_COLOR, 0xE6A627);
        values.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
        values.put(Calendars.OWNER_ACCOUNT, "davenport.cas@gmail.com");
        values.put(Calendars.CALENDAR_TIME_ZONE, "Europe/Berlin");

        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(Calendars.ACCOUNT_NAME, "com.biermacht.brews");
        builder.appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");
        Uri uri = c.getContentResolver().insert(builder.build(), values);
    }

    private void createEvents()
    {
        String title = "";
        String description = "";
        int daysFromNow = 0;

        // Create all the fermentation stage calendar events
        for (int i=0; i < r.getFermentationStages(); i++)
        {
            if (i == 0)
            {
                title = r.getRecipeName() + ": begin primary";
                description = "Begin primary fermentation.";
                daysFromNow = 0;
            }

            if (i == 1)
            {
                title = r.getRecipeName() + ": begin secondary";
                description = "Begin secondary fermentation.";
                daysFromNow = r.getFermentationAge(Recipe.STAGE_PRIMARY);
            }

            if (i == 2)
            {
                title = r.getRecipeName() + ": begin tertiary";
                description = "Begin tertiary fermentation.";
                daysFromNow = r.getFermentationAge(Recipe.STAGE_SECONDARY) + r.getFermentationAge(Recipe.STAGE_PRIMARY);
            }

            createEvent(title, description, daysFromNow);
        }

        // Bottle event
        title = r.getRecipeName() + ": bottle / keg";
        description = "Transfer to keg or bottles.";
        daysFromNow = r.getTotalFermentationDays();
        createEvent(title, description, daysFromNow);
        
        // Drink event
        title = r.getRecipeName() + ": ready to drink";
        description = "Ready to drink!";
        daysFromNow = r.getTotalFermentationDays() + r.getBottleAge();
        createEvent(title, description, daysFromNow);

    }

    private void createEvent(String title, String description, int daysFromNow)
    {
        Calendar start = Calendar.getInstance();
        start.add(Calendar.DATE, daysFromNow);

        long startTime = start.getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(Events.CALENDAR_ID, getCalendarId());
        values.put(Events.ORGANIZER, "Biermacht");
        values.put(Events.TITLE, title);
        values.put(Events.EVENT_LOCATION, "The Brewery");
        values.put(Events.DESCRIPTION, description);
        values.put(Events.EVENT_COLOR, 0xE6A627);
        values.put(Events.DTSTART, startTime);
        values.put(Events.DTEND, startTime);
        values.put(Events.EVENT_TIMEZONE, "Europe/Berlin");
        values.put(Events.EVENT_END_TIMEZONE, "Europe/Berlin");
        values.put(Events.RRULE,"FREQ=DAILY;COUNT=1;");
        values.put(Events.ACCESS_LEVEL, Events.ACCESS_PUBLIC);
        values.put(Events.SELF_ATTENDEE_STATUS, Events.STATUS_CONFIRMED);
        values.put(Events.ALL_DAY, 1);
        values.put(Events.ORGANIZER, "biermacht brews");
        values.put(Events.GUESTS_CAN_INVITE_OTHERS, 1);
        values.put(Events.GUESTS_CAN_MODIFY, 1);
        values.put(Events.GUESTS_CAN_SEE_GUESTS, 1);
        values.put(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);

        Uri uri = c.getContentResolver().insert(Events.CONTENT_URI, values);
        long eventId = new Long(uri.getLastPathSegment());
    }
    
    private AlertDialog.Builder addToCalendarAlert()
    {
    	String message = "Add the following events to your calendar?\n\n";
    	for (String s : getCalendarMessages())
    		message += " - " + s + "\n";

	    return new AlertDialog.Builder(this.c)
	    .setTitle("Add to calendar")
	    .setMessage(message)
	    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	
	        public void onClick(DialogInterface dialog, int which) 
	        {
	        	// Add events to calendar.
	        	createCalendarItems();
	        }
	
	    })
	
	    .setNegativeButton(R.string.cancel, null);
    }
    
    private ArrayList<String> getCalendarMessages()
    {
    	ArrayList<String> messages = new ArrayList<String>();
        String line = "";

        // Create all the fermentation stage calendar events
        for (int i=0; i < r.getFermentationStages(); i++)
        {
            if (i == 0)
            	messages.add("Begin primary fermentation.");

            if (i == 1)
                messages.add("Begin secondary fermentation.");

            if (i == 2)
            	messages.add("Begin tertiary fermentation.");
        }

        // Bottle event
        messages.add("Transer to keg or bottles.");
        messages.add("Ready to drink.");
        return messages;
    }
}
