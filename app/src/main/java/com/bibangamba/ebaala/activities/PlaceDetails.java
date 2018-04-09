package com.bibangamba.ebaala.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bibangamba.ebaala.R;
import com.bibangamba.ebaala.model.Place;
import com.bibangamba.ebaala.utils.FirebaseDatabaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceDetails extends AppCompatActivity {
    public static final String TAG = "PlaceDetails";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.place_name_tv)
    TextView placeNameTV;
    @BindView(R.id.rating_tv)
    TextView ratingTV;
    @BindView(R.id.tags_tv)
    TextView tagsTV;
    @BindView(R.id.address_tv)
    TextView addressTV;
    @BindView(R.id.map_iv)
    ImageView mapIV;
    @BindView(R.id.collapsing_toobar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private DatabaseReference mDatabase;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details_activity);
        mDatabase = FirebaseDatabaseUtil.getDatabase().getReference();
        storage = FirebaseStorage.getInstance();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        bindPlaceDetailsToView();

        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(PlaceDetails.this, android.R.color.transparent));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setUpAppBar();

    }

    private void bindPlaceDetailsToView() {
        Bundle b = getIntent().getExtras();
        String placeKey = ""; // or other values
        if (b != null) {
            placeKey = b.getString("placeKey");
            if (placeKey != null) {
                mDatabase.child("places").child(placeKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Place place = dataSnapshot.getValue(Place.class);
                        collapsingToolbarLayout.setTitle(place.getName());
                        placeNameTV.setText(place.getName());
                        ratingTV.setText(String.format("%s", place.getRating()));
                        tagsTV.setText(String.format("Known for: %s", place.getAssociatedTags()));
                        addressTV.setText(place.getAddress());
                        if (!place.getImage().isEmpty()) {
                            Log.i(TAG, place.getImage());
                            StorageReference storageReference = storage.getReferenceFromUrl(place.getImage());
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.with(PlaceDetails.this).load(uri)
                                            .placeholder(R.drawable.jazz_ville_map)
                                            .error(R.drawable.jazz_ville_map)
                                            .into(mapIV);
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

    }

    public void setUpAppBar() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean showTitle = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    showTitle = true;
                    toolbar.setTitle("yay!!");
                } else if (showTitle) {
                    showTitle = false;
                    toolbar.setTitle("");
                }
            }
        });
    }
}
