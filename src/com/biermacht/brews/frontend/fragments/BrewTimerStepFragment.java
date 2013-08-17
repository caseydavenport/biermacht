package com.biermacht.brews.frontend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.InstructionArrayAdapter;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.Recipe;

import java.util.ArrayList;

public class BrewTimerStepFragment extends Fragment {

	private int resource;
	private Recipe r;
    private Instruction i;
	Context c;

    // Views
    ViewGroup pageView;
    TextView titleView;
    TextView descriptionView;
    ImageButton calendarButton;

	public BrewTimerStepFragment(Context c, Recipe r, Instruction i)
	{
		this.resource = R.layout.fragment_brew_timer_step;
		this.r = r;
		this.c = c;
        this.i = i;
	}

    @Override
    public String toString()
    {
        return i.getInstructionType();
    }

    public Instruction getInstruction()
    {
        return this.i;
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        // Get views
		pageView = (LinearLayout) inflater.inflate(resource, container, false);
        titleView = (TextView) pageView.findViewById(R.id.title);
        descriptionView = (TextView) pageView.findViewById(R.id.description);
        calendarButton = (ImageButton) pageView.findViewById(R.id.calendar_button);

        // Only display calendar if this is a calendar instruction...
        if (i.getInstructionType().equals(Instruction.TYPE_CALENDAR))
        {
            calendarButton.setVisibility(View.VISIBLE);
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

        // Turn off options menu
		setHasOptionsMenu(false);
		
		return pageView;
	}
}
