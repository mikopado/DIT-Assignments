package com.example.miche.uilabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ToDoManagerActivity extends AppCompatActivity {


    private ListView toDoItemListView;
    private List<ToDoItem> toDoItemList = new ArrayList<>();

    // Add a ToDoItem Request Code
    private static final int ADD_TODO_ITEM_REQUEST = 0;

    private static final String FILE_NAME = "TodoManagerActivityData.txt";
    private static final String TAG = "Lab-UserInterface";

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    ToDoListAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO Base 1.1 - Inflate footerView for footer_view.xml file
        setContentView(R.layout.footer_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO Base 1.4 - Create a new TodoListAdapter for this ListActivity's ListView and set the adapter for todoItemListView
        mAdapter = new ToDoListAdapter(getApplicationContext(), toDoItemList);
        toDoItemListView = (ListView) findViewById(R.id.toDoItemListView);
        toDoItemListView.setAdapter(mAdapter);

        // Todo Base 1.2, 1.3 - initialize the FAB and set the on click Listener. OnClickListener will launch the AddTodoActivity
        // waiting for the result back
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                log("Entered footerView.OnClickListener.onClick()");

                Intent intent = new Intent(ToDoManagerActivity.this, AddToDoActivity.class);
                startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        log("Entered onActivityResult()");

        // TODO Base 1.5 - if the request code is equal to addtodo request(redundant as it is only request),
        // check if the result code is ok
        // If it's ok, extract todoitem data from the intent and add new item to the list adapter
        // No need to check for resultCode different than OK as nothing has been set for other cases
        if(requestCode == ADD_TODO_ITEM_REQUEST){

            if(resultCode == RESULT_OK){

                ToDoItem newItem = new ToDoItem(data);
                mAdapter.add(newItem);

            }
        }

    }

    // Do not modify below here

    @Override
    public void onResume() {
        super.onResume();

        // Load saved ToDoItems, if necessary
        if (mAdapter.getCount() == 0)
            loadItems();

        //Todo Additional 2 - helps to notify the change in text color after clicking cancel in add to do activity
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save ToDoItems
        saveItems();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // TODO Additional 5 - inflate menu which contains Priority item and deadline item
        // Could have been used menu.add method as well to add new menu itmes
        getMenuInflater().inflate(R.menu.menu_to_do_manager, menu);

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO Additional 5- Added to the switch statement two new cases: priority_tab and deadline_tab
        switch (item.getItemId()) {

            case R.id.priority_tab:

                // TODO Additional 5- if priority action menu item then sort the list based on PriorityComparator and then notify the adapter about the changes
                Collections.sort(toDoItemList, new PriorityComparator());

                mAdapter.notifyDataSetChanged();
                return true;

            case R.id.deadline_tab:

                // TODO Additional 5 - If Deadline action menu item is clicked then sort the list based on deadlineComparator. Notify adapter for changes
                Collections.sort(toDoItemList, new DeadlineComparator());

                mAdapter.notifyDataSetChanged();
                return true;

            case MENU_DELETE:
                mAdapter.clear();
                return true;
            case MENU_DUMP:
                dump();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dump() {

        for (int i = 0; i < mAdapter.getCount(); i++) {
            String data = ((ToDoItem) mAdapter.getItem(i)).toLog();
            log("Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","));
        }

    }

    // Load stored ToDoItems
    private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            String priority = null;
            String status = null;
            Date date = null;

            while (null != (title = reader.readLine())) {
                priority = reader.readLine();
                status = reader.readLine();
                date = ToDoItem.FORMAT.parse(reader.readLine());
                mAdapter.add(new ToDoItem(title, ToDoItem.Priority.valueOf(priority),
                        ToDoItem.Status.valueOf(status), date));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Save ToDoItems to file
    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }


}
