package com.biermacht.brews.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.recipe.Recipe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
  /**
   * Determines if the given value is within the given range
   *
   * @param a
   * @param low
   * @param high
   * @return
   */
  public static boolean isWithinRange(double a, double low, double high) {
    if (a <= high && a >= low) {
      return true;
    }
    else {
      return false;
    }
  }

  public static int getHours(double time, String units) {
    int num_hours = 0;
    if (units.equals(Units.MINUTES)) {
      for (int i = 60; i <= time; i += 60) {
        num_hours++;
      }
    }
    if (units.equals(Units.HOURS)) {
      num_hours = (int) time;
    }
    return num_hours;
  }

  public static int getMinutes(double time, String units) {
    int num_minutes = 0;
    int num_hours = 0;

    if (units.equals(Units.HOURS)) {
      time = 60 * time;
      units = Units.MINUTES;
    }

    if (units.equals(Units.MINUTES)) {
      num_hours = getHours(time, units);
      num_minutes = (int) (time - (60 * num_hours));
    }

    return num_minutes;
  }

  public static String convertStreamToString(InputStream is) throws Exception {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = null;
    while ((line = reader.readLine()) != null) {
      sb.append(line).append("\n");
    }
    reader.close();
    return sb.toString();
  }

  /**
   * @return Beer XML standard version in use
   */
  public static int getXmlVersion() {
    return 1;
  }

  public static Recipe scaleRecipe(Context c, Recipe r, double newVolume) {
    double oldVolume = r.getDisplayBatchSize();
    double ratio = newVolume / oldVolume;

    r.setDisplayBatchSize(newVolume);
    r.setDisplayBoilSize(r.getDisplayBoilSize() * ratio);

    for (Ingredient i : r.getIngredientList()) {
      if (i instanceof Misc) {
        ((Misc) i).setDisplayAmount(i.getDisplayAmount() * ratio, i.getDisplayUnits());
      }
      else {
        i.setDisplayAmount(i.getDisplayAmount() * ratio);
      }
      new DatabaseAPI(c).updateIngredient(i, Constants.DATABASE_USER_RECIPES);
    }

    r.save(c);
    return r;
  }

  /**
   * This method adjusts the height of the given listView to match the combined height of all if its
   * children and the dividers between list items.  This is used to set the height of the mash step
   * list such that it does not scroll, since it is encompassed by a ScrollView.
   *
   * @param listView
   *         ListView to adjust.
   */
  public static void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      return;
    }

    int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
    int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
    View view = null;
    for (int i = 0; i < listAdapter.getCount(); i++) {
      view = listAdapter.getView(i, view, listView);

      view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

      if (i == 0) {
        view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
        //totalHeight += view.getMeasuredHeight();
      }

      totalHeight += view.getMeasuredHeight();
    }
    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.setLayoutParams(params);
    listView.requestLayout();
  }

  public static double parseDouble(String s, Double defaultValue) {
    try {
      return Double.parseDouble(s);
    } catch (Exception e) {
      Log.e("Utils", "Exception parsing double: " + s);
      return defaultValue;
    }
  }

  public static float parseFloat(String s, float defaultValue) {
    try {
      return Float.parseFloat(s);
    } catch (NumberFormatException e) {
      Log.e("Utils", "Exception parsing float: " + s);
      return defaultValue;
    }
  }

  public static int parseInt(String s, int defaultValue) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      Log.e("Utils", "Exception parsing int: " + s);
      return defaultValue;
    }
  }
}
