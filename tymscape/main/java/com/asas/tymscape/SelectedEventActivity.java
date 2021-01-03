package com.asas.tymscape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.internal.GmsClientEventManager;

public class SelectedEventActivity  extends  AppCompatActivity{
    //View Related variables
    com.google.android.material.textfield.TextInputLayout tvEventName, tvEventDescription, tvEventDate, tvEventTime;
    TextView tvScreenTitle;
    RadioButton rbCategory, rbPriority;
    RadioGroup category, priority;
    ImageView edit;

    //EventModel
    private EventModel eventModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_event);

        //Getting reference to views
        tvScreenTitle = findViewById(R.id.tvScreenTitleView);
        tvEventName = findViewById(R.id.eventTitle);
        tvEventDescription = findViewById(R.id.eventDescription);
        tvEventDate = findViewById(R.id.eventDate);
        tvEventTime = findViewById(R.id.eventTime);

        edit = findViewById(R.id.icEdit);

        //Getting data from Intent and displaying the same
        Intent intent = getIntent();
        if(intent.getExtras() != null){
            eventModel = (EventModel) intent.getSerializableExtra("data");

            //Setting event data in respective fields
            tvScreenTitle.setText(eventModel.getEname());
            tvEventName.getEditText().setText(eventModel.getEname());
            tvEventDate.getEditText().setText(eventModel.getDate());
            tvEventTime.getEditText().setText(eventModel.getTime());

            //Finding category and checking respective radio button
            String c = eventModel.getCategory();
            if(c!= null & !c.isEmpty())
            {
                switch(c){
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

            //Finding priority and checking respective radio button
            String p = eventModel.getPriority();
            if(p!=null & !p.isEmpty()) {
                switch(p){
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

            //checking if event has description and setting the same
            if(!eventModel.getDescription().isEmpty())
                tvEventDescription.getEditText().setText(eventModel.getDescription());

            //Disabling views - to not allow editing of the event data

            tvEventName.setEnabled(false);
            tvEventDescription.setEnabled(false);
            tvEventDate.setEnabled(false);
            tvEventTime.setEnabled(false);

            //Disabling RadioButtons for Priority
            findViewById(R.id.radioNormal).setEnabled(false);
            findViewById(R.id.radioCritical).setEnabled(false);
            findViewById(R.id.radioImportant).setEnabled(false);

            //Disabling RadioButtons for Category
            findViewById(R.id.radioHome).setEnabled(false);
            findViewById(R.id.radioWorkCollege).setEnabled(false);
            findViewById(R.id.radioPersonal).setEnabled(false);
            findViewById(R.id.radioOther).setEnabled(false);

            //setting click listener on edit icon to navigate to EditEventActivity.java - add event data with intent
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), EditEventActivity.class);
                    i.putExtra("data", eventModel);
                    startActivity(i);
                    finish();
                }
            });

        }
    }
}
