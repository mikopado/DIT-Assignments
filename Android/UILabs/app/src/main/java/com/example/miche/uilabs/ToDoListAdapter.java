package com.example.miche.uilabs;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by miche on 17/11/2017.
 */

public class ToDoListAdapter extends ArrayAdapter<ToDoItem> {

    //TODO Add a view holder class to hold the view allowing smooth scrolling and avoiding call findByViewId methods
    private static class ViewHolder {
        TextView titleView;
        CheckBox statusView;
        TextView dateView;
        Spinner spinnerPriority;
        RelativeLayout itemLayout;
        //TextView priorityView;
    }
    private List<ToDoItem> mItems;
    private final Context mContext;

    private static final String TAG = "Lab-UserInterface";

    private final long FIVE_MINUTES = 300000;
    private final long ONE_DAY = 86400000;


    public ToDoListAdapter(@NonNull Context context, List<ToDoItem> toDoItems) {
        super(context, -1, toDoItems);
        mContext = context;
        mItems = toDoItems;
    }

    public void add(ToDoItem item) {

        //TODO Additional 2 - add a alarm notification to the new item. Using hashcode as a unique identifier
        pushAlarmNotification(item, item.hashCode());

        mItems.add(item);
        notifyDataSetChanged();
    }

    //TODO Additional 4 - remove item from the list
    public void remove(ToDoItem item){

        //Todo Additional 4 - cancel the alarm attached to the item through hashcode, which unique
        // identifies the pending intent that is attached to the notification receiver
        cancelAlarm(item.hashCode());

        mItems.remove(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        //TODO Base 2.1 - Get the current ToDoItem
        final ToDoItem toDoItem = getItem(position);

        final ViewHolder viewHolder;

        // TODO Base 2 - if the covertView is null then inflate the view and keep all view elements in view holder
        if(convertView == null) {

            viewHolder = new ViewHolder();
            //TODO Base 2.2 - inflate the view for this todo item
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.todo_item, parent, false);

            //TODO Base 2.3 to 2.7 - initialize all the views for this todo item with the help of viewholder.
            viewHolder.dateView = convertView.findViewById(R.id.dateView);
            viewHolder.titleView = convertView.findViewById(R.id.titleView);
            viewHolder.statusView = convertView.findViewById(R.id.statusCheckBox);
            viewHolder.itemLayout = convertView.findViewById(R.id.RelativeLayout1);
            //viewHolder.priorityView = convertView.findViewById(R.id.priorityView);

            // TODO Additional 3 - initialize spinner. Create array adapter from priority options array in arrays.xml.
            // Then use a custom layout for spinner item. Used custom layou to modify text color of spinner item
            viewHolder.spinnerPriority = convertView.findViewById(R.id.spinnerPriority);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.priority_options, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // todo Additional 3 - set adapter to apply the adapter to the spinner
            viewHolder.spinnerPriority.setAdapter(adapter);

            convertView.setTag(viewHolder);

        } else { // if the view already exists, load the viewHolder class who has been set previously
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //TODO Base 2.3 to 2.7 - Fill in specific ToDoItem data
        // Set the to do item values to the view elements
        viewHolder.dateView.setText(ToDoItem.FORMAT.format(toDoItem.getDate()));
        viewHolder.titleView.setText(toDoItem.getTitle());
        // Todo Base 2.6 - set priorityView text
        //viewHolder.priorityView.setText(toDoItem.getPriority().toString());

        // Todo Additional 3 - Set spinner priority listener
        setSpinner(toDoItem, viewHolder.spinnerPriority);

        // Todo Base 2.4 - set the status checkbox based on the todoItem status property
        viewHolder.statusView.setChecked(toDoItem.getStatus() == ToDoItem.Status.DONE);
        //TODO Additional 1 - notifyChange status will update the background color based on the current status
        notifyChangeStatus(convertView, toDoItem.getStatus() == ToDoItem.Status.DONE);

        // TODO BASE 2.4 - use on click listener because on changed listener didn't keep the status change.
        viewHolder.statusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log("Entered onCheckedChanged()");

                // TODO Base 2.4 - Set up and implement a Listener, which
                // is called when the user toggles the status checkbox
                viewHolder.statusView.setChecked(viewHolder.statusView.isChecked());

                // TODO Additional 1
                notifyChangeStatus((View)view.getParent(), viewHolder.statusView.isChecked());

                toDoItem.setStatus(viewHolder.statusView.isChecked() ? ToDoItem.Status.DONE : ToDoItem.Status.NOTDONE);

            }
        });

        // Todo Additional 2 - set the title view color to red if deadline is one day to go.
        changeTextColorWhenDeadlineLessThanOneDay(toDoItem, viewHolder.titleView);


        //TODO Additional 4 - delete event after long click on the list item
        deleteItemOnLongPress(toDoItem, viewHolder.itemLayout);

        // Return the View you just created
        return convertView;
    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }

    // Clears the list adapter of all items.

    public void clear(){

        mItems.clear();
        notifyDataSetChanged();

    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {

        return mItems.size();

    }

    // Retrieve the number of ToDoItems

    @Override
    public ToDoItem getItem(int pos) {

        return mItems.get(pos);

    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {

        return pos;

    }
    // Todo Additional 1 - support method to change background color of the todo item anytime the status changes
    private void notifyChangeStatus(View view, boolean isDone){

        view.setBackgroundColor(isDone ? getContext().getResources().getColor(R.color.status_done,
                getContext().getTheme()) : getContext().getResources().getColor(R.color.status_notdone, getContext().getTheme()));

    }

    private void setSpinner(final ToDoItem item, Spinner spinner){

        // TODO Additional 3 - set the spinner to the selected priority option
        spinner.setSelection(item.getPriority().ordinal());

        // todo Additional 3 - set a onItemSelectedListener for spinner to register anytime user modify priority after todoItem has been created
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Todo Additional 3 - assign the selected item to the adapterview. Then edit the todoItem priority property
                // to store the changes
                adapterView.setSelection(adapterView.getSelectedItemPosition());

                if(i == 0){

                    item.setPriority(ToDoItem.Priority.LOW);

                }else if(i == 1){

                    item.setPriority(ToDoItem.Priority.MED);

                }else{

                    item.setPriority(ToDoItem.Priority.HIGH);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Todo Additional 2 - set the title view color to red if deadline is one day to go.
    private void changeTextColorWhenDeadlineLessThanOneDay(ToDoItem item, TextView view){

        // Todo Additional 2 - set the title view color to red if deadline is one day to go.
        if(item.getDate().getTime() - System.currentTimeMillis() <= ONE_DAY){

            view.setTextColor(Color.RED);

        }else{

            view.setTextColor(Color.BLACK);

        }

    }

    //TODO Additional 4 - handle long press on each todo item
    private void deleteItemOnLongPress(final ToDoItem item, View view){

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                //TODO Additional 4- Create vibration when long press
                Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(2000);

                //TODO Additional 4 - Create alert dialog to delete item
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("Delete");
                builder.setMessage("Would you like to delete the item?");
                builder.setCancelable(true);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Todo Additional 4 - remove the item if user clicks OK
                        remove(item);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
                return false;
            }
        });
    }

    //TODO Additional 2 - create a alarm notification for specific item
    private void pushAlarmNotification(ToDoItem item, int customID){

        //TODO set Notification and alarm 5 min to the deadline
        long fiveMinutesLess = item.getDate().getTime() - FIVE_MINUTES;

        // if statement to avoid alarm sounding when the app is launched and the deadline already passed
        if(System.currentTimeMillis() < fiveMinutesLess){

            // Todo - create an intent for the broadcast receiver. Used the action because it turns out working better than call intent(context, receiver.class)
            Intent intent = new Intent("com.example.miche.todoappcw3.SET_ALARM");
            // TODO - Add extra fields. Title to customize the notification message. CustomID to make a unique notification for each item
            // This allows to cancel alarm using the customID
            intent.putExtra("title", item.getTitle());
            intent.putExtra("requestID", customID);

            // Allows to pass an intent to foreign application. Foreign applications can have same permission of the application.
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, customID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Create alarm manager and set it to five minutes before deadline for the pending intent.
            AlarmManager alarm = (AlarmManager)mContext.getSystemService(ALARM_SERVICE);
            alarm.set(AlarmManager.RTC_WAKEUP, fiveMinutesLess, pendingIntent);

        }

    }
    //TODO Additional 4 - cancel alarm from a single item by ID
    private void cancelAlarm(int requestID){

        Intent intent = new Intent("com.example.miche.todoappcw3.SET_ALARM");
        AlarmManager alarm =(AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pendingIntent);

    }

}
