package com.biermacht.brews.frontend;

import com.biermacht.brews.R;
import com.biermacht.brews.R.layout;
import com.biermacht.brews.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EditIngredientActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_ingredient, menu);
        return true;
    }
}
