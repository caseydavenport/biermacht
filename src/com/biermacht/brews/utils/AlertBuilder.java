package com.biermacht.brews.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biermacht.brews.R;

/**
 * Created by Casey on 7/30/13.
 */
public class AlertBuilder {

    private Context context;

    public AlertBuilder(Context c)
    {
        this.context = c;
    }

    // Builders for all of the alerts
    public AlertDialog.Builder editTextStringAlert(final TextView text, final TextView title)
    {
        LayoutInflater factory = LayoutInflater.from(context);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_edit_text_string, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.edit_text);
        editText.setText(text.getText().toString());

        return new AlertDialog.Builder(context)
                .setTitle(title.getText().toString())
                .setView(alertView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        text.setText(editText.getText().toString());
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }

    public AlertDialog.Builder editTextMultilineStringAlert(final TextView text, final TextView title)
    {
        LayoutInflater factory = LayoutInflater.from(context);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_edit_text_multiline, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.edit_text);
        editText.setText(text.getText().toString());

        return new AlertDialog.Builder(context)
                .setTitle(title.getText().toString())
                .setView(alertView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        text.setText(editText.getText().toString());
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }

    public AlertDialog.Builder editTextIntegerAlert(final TextView text, final TextView title)
    {
        LayoutInflater factory = LayoutInflater.from(context);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_edit_text_integer, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.edit_text);
        editText.setText(text.getText().toString());

        return new AlertDialog.Builder(context)
                .setTitle(title.getText().toString())
                .setView(alertView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        text.setText(editText.getText().toString());
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }

    public AlertDialog.Builder editTextFloatAlert(final TextView text, final TextView title)
    {
        LayoutInflater factory = LayoutInflater.from(context);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_edit_text_float_2_4, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.edit_text);
        editText.setText(text.getText().toString());

        return new AlertDialog.Builder(context)
                .setTitle(title.getText().toString())
                .setView(alertView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        text.setText(editText.getText().toString());
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }
}
