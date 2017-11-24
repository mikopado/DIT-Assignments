package com.example.miche.flaqmocktest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String CHOICES = "pref_number_of_choices";
    public static final String REGIONS = "pref_regions_to_include";
    public static final String HIGH_SCORE = "high_score";

    private boolean phoneDevice = true;
    private boolean preferencesChanged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // when app is launched creates and initialize SharedPreferences using default values set in preferences.xml
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false); //false says that only the first time the method is called needs to set default values

        // A OnSharedPreferenceListener is necessary to notify the MainActivity that preferences are changed
        // GetDefaultSharedPreferences returns a reference to the SharedPreference object representing the app's preference
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferencesChangeListener);

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if(screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            phoneDevice = false;
        }
        //If it is running on phone device force the screen orientation to Portrait
        if(phoneDevice){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        //When app running in portrait every time user calls SettingActivity, MainActivity is stopped when it comes back onStart is called
        // so it needs to check if preference are changed
        if(preferencesChanged){
            // Initialize quizFragment
            MainActivityFragment quizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.quiz_fragment);

            // Update app's prefernces
            quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(this));

            quizFragment.resetQuiz();
            preferencesChanged = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // As the options menu is shown only on portrait orientation we need to check first the orientation
        int orientation = getResources().getConfiguration().orientation;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // TODO 7 how change menu based on orientation
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            // Inflate the menu; this adds items to the action bar if it is present.
            MenuItem item = menu.findItem(R.id.action_settings);
            item.setVisible(false);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This is called when an item on menu is selected
        //By default the only item is Settings but we weant to launch a SettingsActivityFragment instead
        // So we need to use an Intent object for launching the Settings Activity
        // Todo 7 switch selection between two menu items: action settings and help settings
        // Todo 7 for help settings create new dialog fragment and show it
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent preferencesIntent = new Intent(this, SettingsActivity.class);
                startActivity(preferencesIntent);
                return true;
            case R.id.help_settings:
                HelpDialogFragment helpDialog = new HelpDialogFragment();
                helpDialog.show(getSupportFragmentManager(), "Help Dialog");
                return true;

        }


        return super.onOptionsItemSelected(item);


    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // When user changes the prefernces this listener is called, so set preferences changed to true
            preferencesChanged = true;
            // then retrieve reference to the quizFragment to then reset the quiz with new preferences
            MainActivityFragment quizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.quiz_fragment);

            if(key.equals(CHOICES)){ // if the CHOICES are the element which has been changed then update guess rows and reset quiz
                quizFragment.updateGuessRows(sharedPreferences);
                quizFragment.resetQuiz();
            }
            else if(key.equals(REGIONS)){// if regions to include in the quiz have been changed
                // Get all the enabled regions from preferences
                Set<String> regions = sharedPreferences.getStringSet(REGIONS, null);

                if(regions != null && regions.size() > 0){ // If the set is not empty as it should be because at least one region needs to be enabled
                    quizFragment.updateRegions(sharedPreferences);
                    quizFragment.resetQuiz();
                }
                else{// IF the set is empty we need to set the default region value
                    // We get the default region value from the res/values/string.xml and then to modify the Shared Preferences
                    // we need to call the Editor (a key-value object) and then put the regions set into it.
                    // To save asynchronously the data we call apply method of Editor
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    regions.add(getString(R.string.default_region));
                    editor.putStringSet(REGIONS, regions);
                    editor.apply();

                    // Display a message notifying that preferences have been changed
                    Toast.makeText(MainActivity.this, R.string.default_region_message, Toast.LENGTH_SHORT).show();
                }
                // TODO 9

            }

            Toast.makeText(MainActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
        }
    };
}
