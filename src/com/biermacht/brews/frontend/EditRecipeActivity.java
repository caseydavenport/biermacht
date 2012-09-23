package com.biermacht.brews.frontend;

import com.biermacht.brews.R;
import com.biermacht.brews.R.layout;
import com.biermacht.brews.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EditRecipeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_recipe, menu);
        return true;
    }
}
