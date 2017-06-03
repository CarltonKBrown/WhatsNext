package com.example.carva.whatsnext;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "whatsNext";
    private ListView mTaskListView;
    private TaskAdapter t_adapter;
    ArrayList<Task>t_parts;
    ArrayList<String> keys;
    Calendar dateTime = Calendar.getInstance();
    Calendar remTime = Calendar.getInstance();
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    int dpYear, dpMonth, dpDay, tpHour, tpMinute;
    String task = "";
    String note = "";
    String task_info = "";
    String task_note = "";
    String task_time = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTaskListView= (ListView) findViewById(R.id.todo_list);

        updateGUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final AlertDialog.Builder abuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.add_items,null);

                abuilder.setView(mView);
                final AlertDialog dialog = abuilder.create();

                final Intent alertIntent = new Intent(MainActivity.this, AlertReceiver.class);
                final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Button cancelBtn = (Button) mView.findViewById(R.id.Cancel);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                final Button addBtn = (Button) mView.findViewById(R.id.Add);
                addBtn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {

                        final EditText taskEditText = (EditText)dialog.findViewById(R.id.task_name);
                        final EditText taskEditText2 = (EditText)dialog.findViewById(R.id.note);
                         task = String.valueOf(taskEditText.getText());
                         note = String.valueOf(taskEditText2.getText());

                        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                        if(task.equals("")){
                            Toast.makeText(MainActivity.this, "Task title missing", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else {
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putString(task + "-" + note + "-" + updateDateTime(), task + "-" + note + "-" + updateDateTime());

                            editor.apply();

                            alertIntent.putExtra("test", task);

                            remTime.set(dpYear, dpMonth, dpDay, tpHour, tpMinute, 0);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, remTime.getTimeInMillis(),
                                    PendingIntent.getBroadcast(MainActivity.this, 1, alertIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT));

                            Toast.makeText(MainActivity.this, "Task added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            updateGUI();
                        }
                    }
                });

                Button dateBtn = (Button) mView.findViewById(R.id.Date);
                dateBtn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                            updateDate();
                    }
                });

                Button timeBtn = (Button) mView.findViewById(R.id.Time);
                timeBtn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        updateTime();
                    }
                });
                final Button remBtn = (Button) mView.findViewById(R.id.Reminder);
                remBtn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {

                        updateRemDate();

                        updateRemTime();
                    }
                });
                dialog.show();

                return true;

            case R.id.action_about:

                final AlertDialog.Builder cbuilder = new AlertDialog.Builder(MainActivity.this);
                View nView = getLayoutInflater().inflate(R.layout.about,null);

                cbuilder.setView(nView);
                final AlertDialog dialog_about = cbuilder.create();

                String msg = "                                        CREATED BY:\n"+
                        "                            Carlton Brown (1303815)\n" +
                        "                                   Mobile Computing\n" +
                        "                    University of Technology, Jamaica";
                TextView about = (TextView) nView.findViewById(R.id.aboutView);
                about.setText(msg);

                Button okBtn = (Button) nView.findViewById(R.id.aboutDismiss);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_about.dismiss();
                    }
                });
                dialog_about.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private  void updateRemDate(){
        DatePickerDialog datePickerDialog;
        datePickerDialog =  new DatePickerDialog(this, r, remTime.get(Calendar.YEAR),
                remTime.get(Calendar.MONTH), remTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    DatePickerDialog.OnDateSetListener r = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            remTime.set(Calendar.YEAR,year);
            remTime.set(Calendar.MONTH, monthOfYear);
            remTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dpYear = year;
            dpMonth = monthOfYear;
            dpDay = dayOfMonth;
        }
    };

    private void updateRemTime(){
        new TimePickerDialog(this, rt, remTime.get(Calendar.HOUR_OF_DAY),
                remTime.get(Calendar.MINUTE),
                true).show();
    }

    TimePickerDialog.OnTimeSetListener rt = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            remTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
            remTime.set(Calendar.MINUTE,minute);
            tpMinute = minute;
            tpHour = hourOfDay;
        }
    };
    private void updateDate() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR),
                dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                dateTime.set(Calendar.YEAR,year);
                dateTime.set(Calendar.MONTH, monthOfYear);
                dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTime();
        }
    };

    private void updateTime(){
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE),
                true).show();
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                dateTime.set(Calendar.MINUTE,minute);
                updateDateTime();
        }
    };

    private String updateDateTime(){
        return formatDateTime.format(dateTime.getTime());
    }



    private void updateGUI(){

        t_parts = new ArrayList<>();
        Task task_item;
        keys = new ArrayList<>();

        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Map<String,?> allEntries = sharedPref.getAll();
        for(Map.Entry<String,?> entry : allEntries.entrySet()){
            keys.add(entry.getKey());
        }

        Log.d(TAG, String.valueOf(sharedPref.getAll().size()));
        int num = keys.size();
        Log.d(TAG, String.valueOf(num));
        for(int i = 0; i < num; i++) {
            String trial = sharedPref.getString(keys.get(i),"");

                task_info = trial.substring(0, trial.indexOf("-"));
                task_note = trial.substring(trial.indexOf("-") + 1, trial.lastIndexOf("-"));
                task_time = trial.substring(trial.lastIndexOf("-")+1);


            if (task_time.equals("") || task_time.equals(null)) {
                task_time = updateDateTime();
            }
            Log.d(TAG, String.valueOf(task_time));
            String task_month = task_time.substring(0, 3);

            task_time = task_time.substring(3, task_time.length());

            String task_day = task_time.substring(0, task_time.indexOf(','));

            task_time = task_time.substring(task_time.indexOf(',') + 1, task_time.length());

            String task_year = task_time.substring(1,5);

            task_time = task_time.substring(6, task_time.length());

            String task_hour = task_time.substring(0, task_time.indexOf(':'));

            String task_min = task_time.substring(task_time.indexOf(':')+1, task_time.lastIndexOf(":")) +
                    " " + task_time.substring(task_time.length() - 2, task_time.length());

            //Task Object instantiated
            task_item = new Task();

            //Task object initialised with data captured
            task_item.setName(task_info);
            task_item.setNote(task_note);
            task_item.setYear(task_year);
            task_item.setMonth(task_month);
            task_item.setDay(task_day);
            task_item.setHour(task_hour);
            task_item.setMinute(task_min);

            //Add object to custom ArrayList of Task
            t_parts.add(task_item);
        }
        if(t_adapter == null) { //If custom list adapter is empty, instantiate new list adapter with values entered.
            t_adapter = new TaskAdapter(MainActivity.this,R.layout.todo_items, t_parts);
            mTaskListView.setAdapter(t_adapter);
        }else{
            //if not empty, the custom adapter simply clears list view, then updates with updated ArrayList values
            t_adapter.clear();
            t_adapter.addAll(t_parts);
          t_adapter.notifyDataSetChanged();
        }
    }
    //Method to delete specified task
    public void deleteTask(View view){

        //View object referenced
        View parent = (View) view.getParent();

        //TextViews referenced
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        TextView noteTextView = (TextView) parent.findViewById(R.id.task_note);
        TextView dateTextView = (TextView) parent.findViewById(R.id.task_date);
        TextView timeTextView = (TextView) parent.findViewById(R.id.task_time);

        //TextViews values assigned to strings for comparison
        String task = String.valueOf(taskTextView.getText());
        String note = String.valueOf(noteTextView.getText());
        String date = String.valueOf(dateTextView.getText());
        String time = String.valueOf(timeTextView.getText());

        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        for(int i=0; i < keys.size(); i++){

            //String manipulations to obtain values from TextView
            String sp = sharedPref.getString(keys.get(i), "");
            String sp_task = sp.substring(0,sp.indexOf("-") );
            String sp_note = sp.substring(sp.indexOf("-") + 1, sp.lastIndexOf("-"));
            String sp_date = sp.substring(sp.lastIndexOf("-") + 1, sp.indexOf(",") + 6);
            String sp_time = sp.substring(sp.indexOf(",") + 7, sp.indexOf(":") + 3) + sp.substring(sp.lastIndexOf(":")+3);

            //Compares TextView values with values of task selected to be deleted
            if(task.equals(sp_task) && note.equals(sp_note) && date.equals(sp_date) && time.equals(sp_time)){
                editor.remove(keys.get(i));
                keys.remove(i);
            }
        }

        //Apply changes to shared preferences
        editor.apply();

        //Notifies user that task is deleted
        Toast.makeText(MainActivity.this,"Task deleted", Toast.LENGTH_SHORT).show();

        //calls method to update View
        updateGUI();
    }

    public void editTask(View view){
        //View parent = (View) view.getParent();
        Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
        startActivity(intent);
        Toast.makeText(MainActivity.this,"It click enuh",Toast.LENGTH_SHORT).show();
    }
}
