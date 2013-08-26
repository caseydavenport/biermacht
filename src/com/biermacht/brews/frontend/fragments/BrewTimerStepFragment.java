package com.biermacht.brews.frontend.fragments;

import android.content.Context;
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

import com.biermacht.brews.R;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.utils.Constants;

public class BrewTimerStepFragment extends Fragment {

	private int resource =  R.layout.fragment_brew_timer_step;
	private Recipe r;
    private Instruction i;
	Context c = getActivity();

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
}
