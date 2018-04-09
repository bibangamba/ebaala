package com.bibangamba.ebaala.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bibangamba.ebaala.R;
import com.bibangamba.ebaala.model.Place;

/**
 * Created by bibangamba on 10/18/2017.
 */

public class PlacesViewHolder extends RecyclerView.ViewHolder {
    private TextView placeNameTV, associatedTagsTV, ratingTV;

    public PlacesViewHolder(View itemView) {
        super(itemView);
        placeNameTV = (TextView) itemView.findViewById(R.id.place_name_tv);
        associatedTagsTV = (TextView) itemView.findViewById(R.id.tags_tv);
        ratingTV = (TextView) itemView.findViewById(R.id.rating_tv);
    }

    public void bindDataToView(Place place) {
        placeNameTV.setText(place.getName());
        if (place.getAssociatedTags() != null) {
            if (!place.getAssociatedTags().isEmpty()) {
                associatedTagsTV.setText(String.format("Known for: %s", place.getAssociatedTags()));
            } else {
                associatedTagsTV.setVisibility(View.GONE);
            }
        }
        ratingTV.setText(String.format("%s", place.getRating()));
    }
}
