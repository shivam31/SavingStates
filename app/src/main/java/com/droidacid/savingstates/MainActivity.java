package com.droidacid.savingstates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    EditText notesEditText;
    Button btnSettings;
    private static final int SETTINGS_INFO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesEditText = (EditText) findViewById(R.id.notesEditText);


        // 1. Make sure there is data to retrieve
        if(savedInstanceState != null) {

            String notes = savedInstanceState.getString("NOTES");

            notesEditText.setText(notes);
        }
        // 2. Retrieves the String stored in shared preferences or "EMPTY" if nothing
        String sPNotes = getPreferences(Context.MODE_PRIVATE).getString("NOTES", "Empty");

        if(!sPNotes.equals("Empty")) {
            notesEditText.setText(sPNotes);
        }

        // 3. Get a reference to the settings button
        btnSettings = (Button) findViewById(R.id.btnSettings);

        // 3. If the button is clicked create an intent to open the SettingsActivity class
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPrefs = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                // 3. Start the activity and then pass results to onActivityResult
                startActivityForResult(intentPrefs, SETTINGS_INFO);
            }
        });
    }

    // 3. Called after the settings intent closes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 3. Check that the intent with the id SETTINGS_INFO called here
        if(requestCode == SETTINGS_INFO) {
            updateNoteText();
        }
    }

    // 3. Update the text changes in the EditText box
    private void updateNoteText() {
        // Shared key value pairs are here
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Check if the checkbox was clicked
        if(sharedPrefs.getBoolean("pref_text_bold", false)) {
            // Set the text to bold
            notesEditText.setTypeface(null, Typeface.BOLD);
        } else {
            // If not checked set the text to normal
            notesEditText.setTypeface(null, Typeface.NORMAL);
        }
        // Get the value stored in the list preference or give a value of 16
        String textSizeStr = sharedPrefs.getString("pref_text_size", "16");
        // Convert the string returned to a float
        float textSizeFloat = Float.parseFloat(textSizeStr);
        // Set the text size for the EditText box
        notesEditText.setTextSize(textSizeFloat);
    }

    // 1. Called before Android kills an application, but doesn't protect you
    // if the user kills the app or restarts the device
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the value in the EditText using the key NOTES
        outState.putString("NOTES", notesEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    // 2. Will save key value pairs to SharedPreferences
    private void saveSettings() {

        // SharedPreferences allow you to save data even if the user kills the app
        // MODE_PRIVATE : Preferences shared only by your app
        // MODE_WORLD_READABLE : All apps can read
        // MODE_WORLD_WRITABLE : All apps can write
        // edit() allows us to enter key vale pairs

        SharedPreferences.Editor sPEditor =
                (SharedPreferences.Editor) getPreferences(Context.MODE_PRIVATE);
        // Add the key "NOTES" and assign it to the value
        sPEditor.putString("NOTES", notesEditText.getText().toString());
        sPEditor.commit();

    }

    // 2. Called if the app is forced to close
    @Override
    protected void onStop() {
        saveSettings();
        super.onStop();
    }
}
