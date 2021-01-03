package com.asas.tymscape;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;
import java.util.List;

public class EventsAdapter extends FirebaseRecyclerAdapter<EventModel, EventsAdapter.EventsAdaptervh> {

    public EventsAdapter(@NonNull FirebaseRecyclerOptions<EventModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public EventsAdaptervh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_events, null);
        return new EventsAdaptervh(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventsAdaptervh holder, int position, @NonNull EventModel model) {
        //Getting data from the model and storing in variables
        String eventname = model.getEname();
        String date = model.getDate();
        String time = model.getTime();
        String prefix = eventname.substring(0, 1);

        //Setting values for the RecylerView widgets using ViewHolder object
        //Displays event name, date of the event and time of the event as well as first letter of the event
        holder.tvEventname.setText(eventname);
        holder.tvPrefix.setText(prefix);
        holder.tvDate.setText(date);
        holder.tvTime.setText(time);

        //Part of the onBindViewHolder() method lies in Home.java, where the EventAdapter class is instantiated
        //that part contains the onclick method for the items, as Intent to go from Home.java to SelectedEventActivity.java
        //cannot be defined here. Hence, it is defined in home.java itself
    }

    class EventsAdaptervh extends RecyclerView.ViewHolder {

        TextView tvPrefix, tvEventname, tvDate, tvTime;
        View view;
        public EventsAdaptervh(@NonNull View itemView) {
            super(itemView);
            tvPrefix = itemView.findViewById(R.id.prefix);
            tvEventname = itemView.findViewById(R.id.eventname);
            tvDate = itemView.findViewById(R.id.date);
            tvTime = itemView.findViewById(R.id.time);
            view = itemView;
        }
    }
}