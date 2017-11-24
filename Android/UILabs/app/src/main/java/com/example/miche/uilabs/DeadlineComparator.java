package com.example.miche.uilabs;

import java.util.Comparator;

/**
 * Created by miche on 21/11/2017.
 */
// TODO Additional 5 - DeadlineComparator class to sort todoitems based on the deadline date
public class DeadlineComparator implements Comparator<ToDoItem> {

    @Override
    public int compare(ToDoItem toDoItem, ToDoItem t1) {

        if(t1.getDate().getTime() > toDoItem.getDate().getTime()) {

            return -1;

        }else if(t1.getDate().getTime() > toDoItem.getDate().getTime()){

            return 1;
        }
        return 0;
    }
}
