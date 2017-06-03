package com.example.carva.whatsnext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class TaskAdapter extends ArrayAdapter<Task>{

    private ArrayList<Task> objects;


    public TaskAdapter(Context context, int resource, ArrayList<Task> objects) {
        super(context,resource,objects);
        this.objects = objects;
    }


    public View getView(final int position, View convertView, final ViewGroup parent){
         View v = convertView;
        final View x = convertView;

        if( v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.todo_items,parent,false);
        }

        Task t = objects.get(position);
        if (t != null) {

            TextView tt = (TextView) v.findViewById(R.id.task_title);
            TextView tn = (TextView) v.findViewById(R.id.task_note);
            TextView td = (TextView) v.findViewById(R.id.task_date);
            TextView tTime = (TextView) v.findViewById(R.id.task_time);

            if (tt != null) {
                tt.setText(t.getName());
            }
            if (tn != null) {
                tn.setText(t.getNote());
            }
            if (td != null){
                td.setText(t.getMonth() + t.getDay()+", "+ t.getYear());

            }
            if (tTime != null){
                tTime.setText(t.getHour() + ":" + t.getMinute());
            }
        }
        return v;
    }

}
