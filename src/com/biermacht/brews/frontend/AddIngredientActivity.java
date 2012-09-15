package com.biermacht.brews.frontend;

import com.biermacht.brews.R;
import com.biermacht.brews.R.layout;
import com.biermacht.brews.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AddIngredientActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_ingredient, menu);
        return true;
    }
}
