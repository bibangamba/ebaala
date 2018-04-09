package com.bibangamba.ebaala.fragments;

import com.bibangamba.ebaala.AllDataListFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by bibangamba on 10/18/2017.
 */

public class PlacesFragment extends AllDataListFragment {
    public static final String TAG = "PlacesFragment";

    public PlacesFragment() {

    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("places").limitToLast(100);
    }

    @Override
    public String getViewTag() {
        return TAG;
    }
}