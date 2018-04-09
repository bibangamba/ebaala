package com.bibangamba.ebaala.fragments;

import com.bibangamba.ebaala.AllDataListFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by bibangamba on 10/18/2017.
 */

public class TonightFragment extends AllDataListFragment {
    public static final String TAG = "TonightFragment";

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("places");
    }

    @Override
    public String getViewTag() {
        return TAG;
    }
}
