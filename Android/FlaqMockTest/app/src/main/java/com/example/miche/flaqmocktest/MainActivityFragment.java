package com.example.miche.flaqmocktest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = "FlagQuiz Activity"; // to identify in the log message this activity
    // TODO 4 make FLAGS_IN_QUIZ not final it will let to change the value later on
    private static int FLAGS_IN_QUIZ;
    //    TODO 5 set a variable scoreTotal to keep tracking total points,
    //  TODO 5 score counter to keep tracking points of a single question
    // TODO 5 declare scoreTextView and make choices a class field
    public int scoreTotal;
    private int scoreCounter;
    private TextView scoreTextView;
    private String choices;

    // TODO 6 set counter variable
    private int countFirstAttemptQuestions;

    private TextView guessTitleTextView;
    private int highScore;

    private boolean isCapitalQuestion;
    private List<String> fileNameList;          // store flag image file names for the currently enabled regions
    private List<String> quizCountriesList;     // holds the flag file names for the countries in the current quiz
    private Set<String> regionsSet;             // stores regions for the current quiz
    private String correctAnswer;               // holds the flag file name for the current correct answer
    private int totalGuesses;                   // all guesses, either correct or incorrect
    private int correctAnswers;
    private int guessRows;
    private SecureRandom random;           // random number generator to randomly pick the flags to include in the quiz
    private Handler handler;            // uses to delay loading next flag
    private Animation shakeAnimation;

    private LinearLayout quizLinearLayout;
    private TextView questionNumberTextView;
    private ImageView flagImageView;
    private LinearLayout[] guessLinearLayouts;
    private TextView answerTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        fileNameList = new ArrayList<>();
        quizCountriesList = new ArrayList<>();
        random = new SecureRandom();
        handler = new Handler();
//        TODO 4
        FLAGS_IN_QUIZ = Integer.parseInt(getString(R.string.flags_in_quiz));
        scoreTextView = (TextView) view.findViewById(R.id.scoreTextView);
        scoreTextView.setText(getString(R.string.scoreText, scoreTotal));

        isCapitalQuestion = false;
        guessTitleTextView = (TextView) view.findViewById(R.id.guessCountryTextView);
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3);

        quizLinearLayout = (LinearLayout) view.findViewById(R.id.quizLinearLayout);
        questionNumberTextView = (TextView) view.findViewById(R.id.questionNumberTextView);
        flagImageView = (ImageView) view.findViewById(R.id.flagImageView);
        guessLinearLayouts = new LinearLayout[4];
        guessLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] = (LinearLayout) view.findViewById(R.id.row3LinearLayout);
        guessLinearLayouts[3] = (LinearLayout) view.findViewById(R.id.row4LinearLayout);
        answerTextView = (TextView) view.findViewById(R.id.answerTextView);

        for(LinearLayout row : guessLinearLayouts){ // for each button register guessButtonListener as a OnClickListener
            for(int column = 0; column < row.getChildCount(); column++){
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(guessButtonListener);
            }
        }
        // Set the question text -- first parameter is the string and the other two are the parameter to replace the placeholder in the question
        questionNumberTextView.setText(getString(R.string.question, 1, FLAGS_IN_QUIZ));

        return view;
    }

    public void updateGuessRows(SharedPreferences sharedPreferences){
        // This method is called by MainActivity when the app is launched and every time the number of guess buttons are changed by user
        // Get the number of the choices that it is stored under the constant string CHOICES of SettingsActivityFragment.
        choices = sharedPreferences.getString(MainActivity.CHOICES, null);

        // Divide by two to get number of rows as for each row we have two buttons.
        guessRows = Integer.parseInt(choices) / 2;

        // First make all the rows not visible. Use View.Gone to make also the space available instead View.Invisible would keep the space of the invisible items
        for(LinearLayout layout : guessLinearLayouts){
            layout.setVisibility(View.GONE);
        }

        // Then for all guess rows selected make them Visible
        for(int row = 0; row < guessRows; row++){
            guessLinearLayouts[row].setVisibility(View.VISIBLE);
        }
    }

    public void updateRegions(SharedPreferences sharedPreferences){
        // Method called from MainActivity when app is launched and every time the user changes the regions to be included in the quiz
        // Get a set of strings with all regions to be enabled from REGIONS string constant that stores the name of preference in SettingsActivityFragment
        regionsSet = sharedPreferences.getStringSet(MainActivity.REGIONS, null);
    }


    public void resetQuiz(){
        // This method sets up and start a quiz.
        // Get assets where the image files are stored.
        AssetManager assets = getActivity().getAssets();
        // Clear the fileNameList where the file image files are stored to prepare the new quiz with the new image file names for the enabled regions
        fileNameList.clear();


        //TODO 5 reset score total
        scoreTotal = 0;
        // TODO 6 reset counter variable
        countFirstAttemptQuestions = 0;

        isCapitalQuestion = false;
        try {
            for (String region : regionsSet) {
                // Store all image file names paths for the enabled regions
                String[] paths = assets.list(region);

                // for each path add the file name in fileNameList without the .png extension
                for(String path : paths){
                    fileNameList.add(path.replace(".png", ""));
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "Error loading image file names", e);
        }

        // reset counters from the previous quiz
        correctAnswers = 0;
        totalGuesses = 0;
        quizCountriesList.clear();

        int flagCounter = 1;
        int numberOfFlags = fileNameList.size();

        // Add a number (up to FLAGS_IN_QUIZ) of random file names to the quizCountriesList
        while (flagCounter <= FLAGS_IN_QUIZ){

            int randomIndex = random.nextInt(numberOfFlags); // Generate a random number from 0 to the total number of flags in the fileNameList
            // Get a random file name
            String fileName = fileNameList.get(randomIndex);

            // if the region is enabled and it hasn't been chosen already then add to the quizCountriesList
            if(!quizCountriesList.contains(fileName)){
                quizCountriesList.add(fileName);
                ++flagCounter;
            }
        }

        loadNextFlag(); // Start the quiz loading the first flag
    }

    private void loadNextFlag(){
        // It sets and display the next flag and corresponding set of answer buttons
        // Get file name of next flag and remove from list. Update correct answer and clear answerTextView
        String nextImage = quizCountriesList.remove(0);
        correctAnswer = nextImage;
        answerTextView.setText("");
        guessTitleTextView.setText(getString(R.string.guess_country));
        //        TODO 5 set scoreCounter equal to number of choices every time new flag is loaded
        scoreCounter = Integer.parseInt(choices);
        // TODO 5 set Text for score equal to the total
        scoreTextView.setText(getString(R.string.scoreText, scoreTotal));

        // Display the current question number
        questionNumberTextView.setText(getString(R.string.question, (correctAnswers + 1), FLAGS_IN_QUIZ));

        // Get the region name as a substring of nextImage up to the '-'. This is because the file name are stored like
        // regionName-CountryName
        String region = nextImage.substring(0, nextImage.indexOf('-'));
        // Necessary to get the image from asset folder and then load the image
        AssetManager assets = getActivity().getAssets();

        // Get a stream to the asset representing the next flag (open asset folder where region is the subfolder and then nextImage.png the name of file)
        try(InputStream stream = assets.open(region + "/" + nextImage + ".png")){
            // Create a drawable file from the stream to load it to the flagImageView
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            flagImageView.setImageDrawable(flag);
            animate(false); // Animate the flag when display (reveal out)
        }
        catch (IOException expection){
            Log.e(TAG, "Error loading " + nextImage, expection);
        }

        Collections.shuffle(fileNameList); // shuffle the fileNameList
        // Put at the end of the fileNameList the correct answer so it avoids to pick the correct answer when place buttons in the next for loops
        // As then the correct answer will replace one of the button
        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));

        // for each number of rows (2, 4,6 or 8) place buttons and enable them
        for(int row = 0; row < guessRows; row++){
            for(int column = 0; column < guessLinearLayouts[row].getChildCount(); column++){
                Button newGuessButton  = (Button) guessLinearLayouts[row].getChildAt(column);
                newGuessButton.setEnabled(true);
                // get the fileName and set it as a button text
                String fileName = fileNameList.get((row * 2) + column);
                newGuessButton.setText(getCountryName(fileName));
            }
        }

        // Replace one of the button with the correct answer
        int row = random.nextInt(guessRows);
        int column = random.nextInt(2);
        LinearLayout randomRow = guessLinearLayouts[row];
        String countryName = getCountryName(correctAnswer);
        ((Button)randomRow.getChildAt(column)).setText(countryName);
    }

    private String getCountryName(String name){
        // Get Country name --- remember that fileName is in the format of regionName-countryName
        // so get substring from dash ('-') to the end and then replace the underscore with whitespace
        // TODO 8 modify get country name to fit the new file name format
        return  name.substring(name.indexOf('-') + 1, name.lastIndexOf('-')).replace('_', ' ');
    }
    // TODO 8 create new method to get capital name
    private String getCapitalName(String name){
        return name.substring(name.lastIndexOf('-') + 1).replace('-', ' ');
    }

    private void animate(boolean animateOut){
        // Executes the circular reveal animation of the entire Layout (quizLinearLayout) during the transition between questions

        if(correctAnswers == 0){ // prevent animation into UI for the first flag
            return;
        }

        // Claculate the center horizontally and vertically
        int centerX = (quizLinearLayout.getLeft() + quizLinearLayout.getRight()) / 2;
        int centerY = (quizLinearLayout.getTop() + quizLinearLayout.getBottom()) / 2;

        // Calculate animation radius
        int radius = Math.max(quizLinearLayout.getWidth(), quizLinearLayout.getHeight());

        Animator animator;

        if(animateOut){ // Animate out is when the layout is going to disappear
            // Create a circular reveal animation when the animation finishes it load the next flag
            animator = ViewAnimationUtils.createCircularReveal(quizLinearLayout, centerX, centerY, radius, 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loadNextFlag();
                }
            });
        }
        else{ // Animate in is when the new question is going to be uploaded
            animator = ViewAnimationUtils.createCircularReveal(quizLinearLayout, centerX, centerY, 0, radius);

        }

        animator.setDuration(500);
        animator.start();
    }

    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get the reference to the button and increment total guesses

            Button guessButton = (Button) v;
            String guess = guessButton.getText().toString();

            String answer = "";
            if (!isCapitalQuestion) {
                answer = getCountryName(correctAnswer);


                ++totalGuesses;

                // if the guess button text is equal to the correct answer, then increment correct answers
                // and set the answerTextView equal to correct answer and color to green (correct answer color)
                // and disable buttons so they cannot be clicked
                if (guess.equals(answer)) {
                    ++correctAnswers;
                    // TODO 5 if answer is correct update the total score
                    scoreTotal += scoreCounter;
                    answerTextView.setText(answer + "!");
                    answerTextView.setTextColor(getResources().getColor(R.color.correct_answer, getContext().getTheme()));

                    // TODO 6 increment counter if user guess to first attempt
                    if (scoreCounter == Integer.parseInt(choices)) {
                        ++countFirstAttemptQuestions;
                    }
                    // TODO 8 Change the quiz for capital question
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            guessTitleTextView.setText("Guess the Capital");
                            for(int row = 0; row < guessRows; row++){
                                for(int column = 0; column < guessLinearLayouts[row].getChildCount(); column++){
                                    Button newGuessButton  = (Button) guessLinearLayouts[row].getChildAt(column);
                                    newGuessButton.setEnabled(true);
                                    // get the fileName and set it as a button text
                                    String fileName = fileNameList.get((row * 2) + column);
                                    newGuessButton.setText(getCapitalName(fileName));
                                }
                            }
                            int row = random.nextInt(guessRows);
                            int column = random.nextInt(2);
                            LinearLayout randomRow = guessLinearLayouts[row];
                            String capitalName = getCapitalName(correctAnswer);
                            ((Button)randomRow.getChildAt(column)).setText(capitalName);
                            answerTextView.setText("");
                        }
                    }, 500);

                    isCapitalQuestion = true;

                } else {// if the guess is incorrect, a shake animation will be apply to the image to notify the incorrect answer
                    // Display the Incorrect red color text and disble the guess button
                    flagImageView.startAnimation(shakeAnimation);
                    // TODO 5 If answer is wrong decrement score counter of 1
                    scoreCounter -= 1;
                    answerTextView.setText(R.string.incorrect_answer);
                    answerTextView.setTextColor(getResources().getColor(R.color.incorrect_answer, getContext().getTheme()));
                    guessButton.setEnabled(false);
                }
            }else{
                // If the user guess the last question then pop up a dialog message which display the stats (quiz results) of the quiz
                // and rest the quiz after the dialog is closed by the user
                // TODO 8 make the capital question and then keep going the quiz
                answer = getCapitalName(correctAnswer);

                if(guess.equals(answer)){
                    scoreTotal += 10;

                }

                answerTextView.setText(answer + "!");
                answerTextView.setTextColor(getResources().getColor(R.color.correct_answer, getContext().getTheme()));
                disableButtons();
                isCapitalQuestion = false;
                if(correctAnswers == FLAGS_IN_QUIZ){
                    QuizResultsFragment quizResults = new QuizResultsFragment();
                    quizResults.initialize(MainActivityFragment.this);
                    // This makes the dialog not cancelable. It means the user must interact with it it cannot click back button or other to
                    // exit
                    quizResults.setCancelable(false);
                    quizResults.show(getFragmentManager(), "quiz results");

                }
                else{ // if it's not last question then just animate out the layout and move to the next question
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animate(true);
                        }
                    }, 2000);
                }
            }

        }
    };

    private void disableButtons(){// Method called when user makes a correct guess
        for(int row = 0; row < guessRows; row++){
            LinearLayout guessRow = guessLinearLayouts[row];
            for(int i = 0; i < guessRow.getChildCount(); i++){
                guessRow.getChildAt(i).setEnabled(false);
            }
        }
    }

    public static class QuizResultsFragment extends DialogFragment{
        MainActivityFragment fragment;

        public void initialize(MainActivityFragment fragment){
            this.fragment = fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // TODO 6 update dialog message including also the first attempt counter
            builder.setMessage(getString(R.string.results, fragment.totalGuesses, (1000/ (double) fragment.totalGuesses)) + "\n" + getString(R.string.scoreFirstAttempt, fragment.countFirstAttemptQuestions, FLAGS_IN_QUIZ));

            // Set only one button on Dialog, the click's event of this button will reset the quiz
            builder.setPositiveButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fragment.resetQuiz();
                }
            });

            return builder.create();
        }
    }


}


