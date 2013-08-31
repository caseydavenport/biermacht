package com.biermacht.brews.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.utils.Callbacks.BooleanCallback;
import com.biermacht.brews.utils.Callbacks.Callback;

/**
 * Created by Casey on 7/30/13.
 */
public class AlertBuilder {

    private Context context;
    private Callback callback;

    public AlertBuilder(Context c, Callback cb)
    {
        this.context = c;
        this.callback = cb;
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
                        callback.call();
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
                        callback.call();
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
                        callback.call();
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
                        callback.call();
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }

    public AlertDialog.Builder editTextFloatCheckBoxAlert(final TextView text, final TextView title, boolean checked, final BooleanCallback cb)
    {
        LayoutInflater factory = LayoutInflater.from(context);
        final LinearLayout alertView = (LinearLayout) factory.inflate(R.layout.alert_view_edit_text_float_with_check_box, null);
        final EditText editText = (EditText) alertView.findViewById(R.id.edit_text);
        final CheckBox checkBox = (CheckBox) alertView.findViewById(R.id.check_box);

        // Set text
        editText.setText(text.getText().toString());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checkBox.isChecked())
                {
                    editText.setEnabled(false);
                    editText.setClickable(false);
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                    callback.call();
                    editText.setText(text.getText().toString());
                }
                else
                {
                    editText.setEnabled(true);
                    editText.setClickable(true);
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                }
            }
        });

        // Set the box to be checked or not.
        checkBox.setChecked(checked);

        // If checked initially, grey out edit text
        if (checked)
        {
            editText.setEnabled(false);
            editText.setClickable(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }

        return new AlertDialog.Builder(context)
                .setTitle(title.getText().toString())
                .setView(alertView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which)
                    {
                        cb.call(checkBox.isChecked());
                        callback.call();
                        text.setText(editText.getText().toString());
                    }

                })

                .setNegativeButton(R.string.cancel, null);
    }
}
