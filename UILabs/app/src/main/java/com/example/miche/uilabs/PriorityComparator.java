package com.example.miche.uilabs;

import java.util.Comparator;

/**
 * Created by miche on 21/11/2017.
 */
// TODO Additional 5 - Create a PriorityComparator class to compare todoitem. The items are sorted first by priority status,
    // then if same priority they are sorted by deadline date time
public class PriorityComparator implements Comparator<ToDoItem> {
    @Override
    public int compare(ToDoItem toDoItem, ToDoItem t1) {

        // If priority is different compare objects by priority
        if(toDoItem.getPriority() != t1.getPriority()){

            return t1.getPriority().ordinal() - toDoItem.getPriority().ordinal();

        }else{

            // Compare objects by date time if objects have same priority
            if(t1.getDate().getTime() > toDoItem.getDate().getTime()) {

                return -1;

            }else if(t1.getDate().getTime() > toDoItem.getDate().getTime()){

                return 1;

            }
            return 0;
        }
    }
}
