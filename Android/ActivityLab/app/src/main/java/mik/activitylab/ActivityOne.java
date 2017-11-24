package mik.activitylab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityOne extends AppCompatActivity {
    private int onCreateCounter;
    private int onStartCounter;
    private int onResumeCounter;
    private int onRestartCounter;
    private TextView onCreateText;
    private TextView onStartText;
    private TextView onResumeText;
    private TextView onRestartText;
    private Button launchActivityTwoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_one);
        //Initialize views (TextViews and Button)
        onCreateText = (TextView) findViewById(R.id.onCreateTextView);
        onStartText = (TextView) findViewById(R.id.onStartTextView);
        onResumeText = (TextView) findViewById(R.id.onResumeTextView);
        onRestartText = (TextView) findViewById(R.id.onRestartTextView);
        launchActivityTwoButton = (Button) findViewById(R.id.startActivityTwoBtn);

        //Load the stored counters if bundle object is not null
        if(savedInstanceState != null){
            loadSaveInstanceState(savedInstanceState);
        }
        ++onCreateCounter;
        //Set the click event for Start Activity button
        launchActivityTwoButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ActivityOne.this, ActivityTwo.class);
                startActivity(intent);
            }
        });

        Log.i("Lab-Activity", "Entered onCreate() method for " + getLocalClassName());
    }

    @Override
    protected void onStart(){
        super.onStart();
        ++onStartCounter;
        Log.i("Lab-Activity", "Entered onStart() method for " + getLocalClassName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ++onRestartCounter;
        Log.i("Lab-Activity", "Entered onRestart() method for " + getLocalClassName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ++onResumeCounter;
        Log.i("Lab-Activity", "Entered onResume() method for " + getLocalClassName());
        displayCounts();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Lab-Activity", "Entered onPause() method for " + getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lab-Activity", "Entered onStop() method for " + getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lab-Activity", "Entered onDestroy() method for " + getLocalClassName());
    }


    private void displayCounts(){
        onCreateText.setText(getString(R.string.onCreateTextView, onCreateCounter));
        onStartText.setText(getString(R.string.onStartTextView, onStartCounter));
        onResumeText.setText(getString(R.string.onResumeTextView, onResumeCounter));
        onRestartText.setText(getString(R.string.onRestartTextView, onRestartCounter));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("saveOnCreate", onCreateCounter);
        savedInstanceState.putInt("saveOnStart", onStartCounter);
        savedInstanceState.putInt("saveOnResume", onResumeCounter);
        savedInstanceState.putInt("saveOnRestart", onRestartCounter);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void loadSaveInstanceState(Bundle savedInstanceState){
        onCreateCounter = (int) savedInstanceState.get("saveOnCreate");
        onStartCounter = (int) savedInstanceState.get("saveOnStart");
        onResumeCounter = (int) savedInstanceState.get("saveOnResume");
        onRestartCounter = (int) savedInstanceState.get("saveOnRestart");
    }
}
