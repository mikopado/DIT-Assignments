package mik.activitylab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityTwo extends AppCompatActivity {
    private int onCreateCounter;
    private int onStartCounter;
    private int onResumeCounter;
    private int onRestartCounter;
    private TextView onCreateText;
    private TextView onStartText;
    private TextView onResumeText;
    private TextView onRestartText;
    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        //Initialize Views (Text views and button)
        onCreateText = (TextView) findViewById(R.id.onCreateTextView2);
        onStartText = (TextView) findViewById(R.id.onStartTextView2);
        onResumeText = (TextView) findViewById(R.id.onResumeTextView2);
        onRestartText = (TextView) findViewById(R.id.onRestartTextView2);
        closeButton = (Button) findViewById(R.id.closeActivityBtn);

        if(savedInstanceState != null) {
            loadSaveInstanceState(savedInstanceState);
        }
        ++onCreateCounter;
        //Handle click event for Close Activity Button
        closeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
        Log.i("Lab-Activity", "Entered onCreate() method for " + getLocalClassName());
    }

    @Override
    protected void onStart() {
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
        Log.i("Lab-Activity", "Entered onPause() method for " + getLocalClassName());
        super.onPause();
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
        onCreateText.setText(getString(R.string.onCreateTextView2, onCreateCounter));
        onStartText.setText(getString(R.string.onStartTextView2, onStartCounter));
        onResumeText.setText(getString(R.string.onResumeTextView2, onResumeCounter));
        onRestartText.setText(getString(R.string.onRestartTextView2, onRestartCounter));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveActivityState(outState);
        super.onSaveInstanceState(outState);
    }

    private void loadSaveInstanceState(Bundle state){
        onCreateCounter = (int) state.get("saveOnCreate");
        onStartCounter = (int) state.get("saveOnStart");
        onResumeCounter = (int) state.get("saveOnResume");
        onRestartCounter = (int) state.get("saveOnRestart");
    }

    private void saveActivityState(Bundle state){
        state.putInt("saveOnCreate", onCreateCounter);
        state.putInt("saveOnStart", onStartCounter);
        state.putInt("saveOnResume", onResumeCounter);
        state.putInt("saveOnRestart", onRestartCounter);
    }


}
