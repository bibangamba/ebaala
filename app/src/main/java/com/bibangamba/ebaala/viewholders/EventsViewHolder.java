package com.bibangamba.ebaala.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bibangamba.ebaala.R;
import com.bibangamba.ebaala.model.Event;
import com.bibangamba.ebaala.model.Place;

/**
 * Created by bibangamba on 10/18/2017.
 */

public class EventsViewHolder extends RecyclerView.ViewHolder {
    private TextView eventNameTV, ratingTV;

    public EventsViewHolder(View itemView) {
        super(itemView);
        eventNameTV = (TextView) itemView.findViewById(R.id.place_name_tv);
        ratingTV = (TextView) itemView.findViewById(R.id.rating_tv);
    }

    public void bindDataToView(Event event) {
        eventNameTV.setText(event.getName());
        ratingTV.setText(String.format("%s", event.getDay()));
    }
}
