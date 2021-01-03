package com.asas.tymscape;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.events.Event;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity{
    ImageView logout;
    RecyclerView recyclerView;
    EventsAdapter eventsAdapter;

    com.google.android.material.floatingactionbutton.FloatingActionButton add_btn;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //Get reference to the current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //get the events of the current user
        Query query = FirebaseDatabase.getInstance().getReference("event").orderByChild("uid").equalTo(currentUser.getUid());

        //Fetching data from database and converting it into EventModel format
        FirebaseRecyclerOptions<EventModel> options =
                new FirebaseRecyclerOptions.Builder<EventModel>()
                        .setQuery(query, EventModel.class)
                        .build();

        //instantiating adapter class
        eventsAdapter = new EventsAdapter(options) {
            //Part of onBindViewHolder that contains the onClickListener for event event item.
            @Override
            protected void onBindViewHolder(@NonNull EventsAdaptervh holder, int position, @NonNull EventModel model) {
                super.onBindViewHolder(holder, position, model);
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), SelectedEventActivity.class);
                        intent.putExtra("data", model);
                        startActivity(intent);
                    }
                });
            }
        };

        //Setting adapter for recylerview
        recyclerView.setAdapter(eventsAdapter);


        /*Add code for Add Event Button here*/
        add_btn = findViewById((R.id.add_btn));
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddEventActivity.class));
                finish();
            }
        });

        //Logout Functionality
        logout = (ImageView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(Home.this, "Signing out...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Home.this, LogIn.class));
                finish();
                Toast.makeText(Home.this, "Successfully signed out!", Toast.LENGTH_SHORT).show();
                }});
    }

    //
    @Override
    protected void onStart(){
        super.onStart();
        eventsAdapter.startListening();
    }

    //
    @Override
    protected void onStop() {
        super.onStop();
        eventsAdapter.stopListening();
    }
}