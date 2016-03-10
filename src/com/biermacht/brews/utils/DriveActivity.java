package com.biermacht.brews.utils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.biermacht.brews.recipe.Recipe;
import com.biermacht.brews.xml.RecipeXmlWriter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by casey on 3/6/16.
 */
public abstract class DriveActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  public GoogleApiClient driveClient;
  private ProgressDialog progressDialog;
  private int action;
  private ArrayList<Recipe> recipes;

  private static final int NONE = 0;
  private static final int FILE_OPEN = 1;
  private static final int FILE_WRITE = 2;

  // Callback for after creating Drive contents.
  final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new
          ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(DriveApi.DriveContentsResult result) {

              if (! result.getStatus().isSuccess()) {
                // TODO HANDLE ERROR
                return;
              }
              final DriveContents driveContents = result.getDriveContents();

              // Write content to DriveContents
              OutputStream outputStream = driveContents.getOutputStream();
              Writer writer = new OutputStreamWriter(outputStream);
              Recipe r = new Recipe();
              try {
                writer.write(new RecipeXmlWriter(getApplicationContext()).getXmlText(recipes));
                writer.close();
              } catch (IOException e) {
                Log.e("MainActivity", e.getMessage());
              }

              // Determine the default title for the file.
              String title = RecipeXmlWriter.generateFileName("all-recipes-");
              if (recipes.size() == 1) {
                title = RecipeXmlWriter.generateFileName(recipes.get(0).getRecipeName() + "-");
              }

              // Start intent to pick and create file.
              MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                      .setTitle(title)
                      .setMimeType("text/xml").build();
              IntentSender intentSender = Drive.DriveApi
                      .newCreateFileActivityBuilder()
                      .setInitialMetadata(metadataChangeSet)
                      .setInitialDriveContents(result.getDriveContents())
                      .setActivityTitle("Save Recipe(s)")
                      .build(driveClient);
              try {
                startIntentSenderForResult(intentSender,
                                           Constants.REQUEST_DRIVE_FILE_CREATE,
                                           null, 0, 0, 0);
              } catch (IntentSender.SendIntentException e) {
                // TODO Handle the exception
              }
            }
          };

  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    // Create the progress dialog view - displayed when connecting to Google APIs.
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Connecting to Google APIs...");
    progressDialog.setIndeterminate(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.setCancelable(false);

    // Create the connection driveClient.
    driveClient = new GoogleApiClient.Builder(this)
            .addApi(Drive.API)
            .addScope(Drive.SCOPE_FILE)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
  }

  // Abstract methods to be overridden by the subclass.  These handle drive events.
  public abstract void onDriveFilePicked(Intent data);

  public abstract void onDriveFileWritten(Intent data);

  public void connectToDrive() {
    driveClient.connect();
    progressDialog.show();
  }

  @Override
  protected void onStop() {
    driveClient.disconnect();
    super.onStop();
  }

  public void pickFile() {
    if (! driveClient.isConnected()) {
      this.action = FILE_OPEN;
      connectToDrive();
    }
    else {
      _pickFile();
    }
  }

  private void _pickFile() {
    Log.d("DriveActivity", "Starting Google Drive file picker intent");
    this.action = NONE;
    IntentSender i = Drive.DriveApi
            .newOpenFileActivityBuilder()
            .build(driveClient);
    try {
      this.startIntentSenderForResult(i, Constants.REQUEST_DRIVE_FILE_OPEN, null, 0, 0, 0);
    } catch (IntentSender.SendIntentException e) {
      e.printStackTrace();
    }
  }

  public void writeFile(ArrayList<Recipe> recipes) {
    // Store the given recipes so we can write them later.
    this.recipes = recipes;

    if (! driveClient.isConnected()) {
      this.action = FILE_WRITE;
      connectToDrive();
    }
    else {
      _writeFile();
    }
  }

  private void _writeFile() {
    Log.d("DriveActivity", "Writing file to Google Drive.");
    this.action = NONE;
    IntentSender i = Drive.DriveApi
            .newOpenFileActivityBuilder()
            .build(driveClient);

    Drive.DriveApi.newDriveContents(driveClient)
            .setResultCallback(driveContentsCallback);
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    switch (requestCode) {
      case Constants.REQUEST_CONNECT_TO_DRIVE:
        Log.d("DriveActivity", "Result from Google API: " + resultCode);
        handleConnectToDrive(resultCode, data);
        break;
      case Constants.REQUEST_DRIVE_FILE_OPEN:
        Log.d("MainActivity", "Result from Google Drive file access: " + resultCode);
        handleDriveFileOpen(resultCode, data);
        break;
      case Constants.REQUEST_DRIVE_FILE_CREATE:
        Log.d("DriveActivity", "Result from Google Drive file create: " + resultCode);
        handleDriveFileWritten(resultCode, data);
        break;
      default:
        super.onActivityResult(requestCode, resultCode, data);
        break;
    }
  }

  private void handleConnectToDrive(int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      // Make sure the app is not already connected or attempting to connect
      if (! this.driveClient.isConnecting() &&
              ! this.driveClient.isConnected()) {
        this.driveClient.connect();
      }
    }
  }

  private void handleDriveFileOpen(int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      // Pass to the onFilePicked method for the subclass to handle.
      this.onDriveFilePicked(data);
    }
  }

  private void handleDriveFileWritten(int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      // Pass to the onDriveFileWritten method for the subclass to handle.
      this.onDriveFileWritten(data);
    }

    // Clear recipes.
    this.recipes = null;
  }

  @Override
  public void onConnected(Bundle bundle) {
    Log.d("DriveApiHelper", "Connected to Google APIs");
    if (progressDialog.isShowing()) {
      Log.d("DriveApiHelper", "Dismissing progress dialog");
      progressDialog.dismiss();
    }

    // Based on the action, call the correct method.
    switch (this.action) {
      case FILE_OPEN:
        _pickFile();
        break;
      case FILE_WRITE:
        _writeFile();
        break;
      default:
        Log.e("DriveApiHelper", "Unrecognized action: " + this.action);
        break;
    }
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(ConnectionResult result) {
    Log.d("MainActivity", "Google API Connection failed");
    if (result.hasResolution()) {
      try {
        result.startResolutionForResult(this, Constants.REQUEST_CONNECT_TO_DRIVE);
      } catch (IntentSender.SendIntentException e) {
        driveClient.connect();
      }
    }
    else {
      GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(),
                                                         Constants.REQUEST_CONNECT_TO_DRIVE).show();
    }

  }
}
