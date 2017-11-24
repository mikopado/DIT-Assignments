package com.example.miche.flaqmocktest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by miche on 09/11/2017.
 */
// Todo 7 create new dialog fragment class for help settings dialog
public class HelpDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("For each guess correct at the first attempt you will get the amount of " +
                "point equivalent to the number of possible choices. For each wrong answer your score is decremted by one");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();

    }
}
