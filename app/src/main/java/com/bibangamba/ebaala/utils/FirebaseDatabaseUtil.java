package com.bibangamba.ebaala.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bibangamba on 7/25/2017.
 */

public class FirebaseDatabaseUtil {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;

    }
}
