package com.asas.tymscape;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {
    //View related variables
    com.google.android.material.textfield.TextInputLayout tvEventName, tvEventDescription, tvEventDate, tvEventTime;
    RadioButton rbCategory, rbPriority;
    RadioGroup categoryRbg, priorityRbg;
    ImageView edit, delete, cancel, date, time;

    //DB related Variables
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference eventRef = db.getReference("event");


    private EventModel eventModel;

    //Date and Time related variables
    private int mDate, mMonth, mYear;
    private int curHour, curMinute;
    TimePickerDialog timePickerDialog;
    Calendar calendar;

    //String variables
    String ampm;
    String eDate, eTime, eTitle, eDescription, eCategory, ePriority;
    String eid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        //Getting references to views
        tvEventName = findViewById(R.id.eventTitle);
        tvEventDescription = findViewById(R.id.eventDescription);
        tvEventDate = findViewById(R.id.eventDate);
        tvEventTime = findViewById(R.id.eventTime);
        categoryRbg = findViewById(R.id.radioCategory);
        priorityRbg = findViewById(R.id.radioPriority);

        //References to icons for setting Click Listeners
        edit = findViewById(R.id.edit_btn);
        delete = findViewById(R.id.delete_btn);
        cancel = findViewById(R.id.cancel_btn);
        date = findViewById(R.id.datepicker);
        time = findViewById(R.id.timechooser);


        //SETTING VALUES ALREADY SELECTED BY USER
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            eventModel = (EventModel) intent.getSerializableExtra("data");
            eid = eventModel.getEid();

            tvEventName.getEditText().setText(eventModel.getEname());
            tvEventDate.getEditText().setText(eventModel.getDate());
            tvEventTime.getEditText().setText(eventModel.getTime());

            //Initialising date and time string with already set values
            eDate = eventModel.getDate();
            eTime = eventModel.getTime();

            //Selecting appropriate radio buttons
            String c = eventModel.getCategory();
            if (c != null & !c.isEmpty()) {
                switch (c) {
                    case "Home":
                        rbCategory = findViewById(R.id.radioHome);
                        break;
                    case "Work/College":
                        rbCategory = findViewById(R.id.radioWorkCollege);
                        break;
                    case "Personal":
                        rbCategory = findViewById(R.id.radioPersonal);
                        break;
                    default:
                        rbCategory = findViewById(R.id.radioOther);
                        break;
                }
                rbCategory.setChecked(true);
            }

            String p = eventModel.getPriority();
            if (p != null & !p.isEmpty()) {
                switch (p) {
                    case "Critical":
                        rbPriority = findViewById(R.id.radioCritical);
                        break;
                    case "Important":
                        rbPriority = findViewById(R.id.radioImportant);
                        break;
                    default:
                        rbPriority = findViewById(R.id.radioNormal);
                        break;
                }
                rbPriority.setChecked(true);
            }

            //Setting description(if available)
            if (!eventModel.getDescription().isEmpty())
                tvEventDescription.getEditText().setText(eventModel.getDescription());
        }

        //ENABLING DATE AND TIME FUNCTIONALITY
        date = findViewById(R.id.datepicker);
        time = findViewById(R.id.timechooser);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar Cal = Calendar.getInstance();
                mDate = Cal.get(Calendar.DATE);
                mMonth = Cal.get(Calendar.MONTH);
                mYear = Cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        eDate = "" + date + "/" + (month + 1) + "/" + year;
                        tvEventDate.getEditText().setText(eDate);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                curHour = calendar.get(Calendar.HOUR_OF_DAY);
                curMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        } else {
                            ampm = "AM";
                        }
                        eTime = "" + hourOfDay + ":" + minutes;
                        tvEventTime.getEditText().setText(eTime + " " + ampm);
                    }
                }, curHour, curMinute, true);

                timePickerDialog.show();
            }
        });

        //UPDATE THE DATABASE WITH THE NEW VALUES ENTERED
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     if(isValidName() & isValidDate() & isValidTime()){
                         getValues();
                         //If any value is edited, data is updated, else old data itself is maintained
                         eventModel.setEname(eTitle);
                         eventModel.setDate(eDate);
                         eventModel.setTime(eTime);
                         eventModel.setCategory(eCategory);
                         eventModel.setPriority(ePriority);

                         if(!eDescription.equals(""))
                             eventModel.setDescription(eDescription);

                         //update data of event node
                         eventRef.child(eid).setValue(eventModel);

                         //If successfully updates, display toast and navigate to SelectedEventActivity
                         Toast.makeText(EditEventActivity.this, "Event edited successfully!", Toast.LENGTH_LONG).show();
                         Intent i = new Intent(getApplicationContext(), SelectedEventActivity.class);
                         i.putExtra("data", eventModel);
                         startActivity(i);
                         finish();
                     }
            }
        });

        //DELETE EVENT
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Should we add Confirmation functionality?
                eventRef.child(eid).removeValue();
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                Toast.makeText(EditEventActivity.this, "Event deleted!", Toast.LENGTH_SHORT).show();
            }
        });

        //CANCEL EDITING
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditEventActivity.this, "Updates (if any) Discarded", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditEventActivity.this, Home.class));
                finish();
            }
        });
    }

    //Get the values from the views
    private void getValues() {
        eTitle = tvEventName.getEditText().getText().toString().trim();
        eDescription = tvEventDescription.getEditText().getText().toString().trim();
        rbCategory = findViewById(categoryRbg.getCheckedRadioButtonId());
        eCategory = rbCategory.getText().toString().trim();
        rbCategory = findViewById(priorityRbg.getCheckedRadioButtonId());
        ePriority = rbCategory.getText().toString().trim();
    }

    //Validation Methods - for event name, date and time
    // description is optional
    // Category will already be selected hence, cannot be null
    private Boolean isValidName() {
        String name = tvEventName.getEditText().getText().toString();
        if (name.isEmpty()) {
            tvEventName.setError("Field cannot be empty");
            return false;
        } else {
            tvEventName.setError(null);
            return true;
        }
    }

    private Boolean isValidDate() {
        String date = tvEventDate.getEditText().getText().toString();
        if (date.isEmpty()) {
            tvEventDate.setError("Field cannot be empty");
            return false;
        } else {
            tvEventDate.setError(null);
            return true;
        }
    }

    private Boolean isValidTime() {
        String time = tvEventTime.getEditText().getText().toString();
        if (time.isEmpty()) {
            tvEventTime.setError("Field cannot be empty");
            return false;
        } else {
            tvEventTime.setError(null);
            return true;
        }
    }
}
