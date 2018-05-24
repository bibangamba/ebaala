package com.bibangamba.ebaala.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bibangamba.ebaala.R;
import com.bibangamba.ebaala.adapters.EventAdapter;
import com.bibangamba.ebaala.adapters.OpenTimeAdapter;
import com.bibangamba.ebaala.model.Event;
import com.bibangamba.ebaala.model.OpentTime;
import com.bibangamba.ebaala.model.Place;
import com.bibangamba.ebaala.utils.FirebaseDatabaseUtil;
import com.bibangamba.ebaala.utils.HelperClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceDetails extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
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
    //@BindView(R.id.place_map)
    //ImageView mapIV;
    @BindView(R.id.collapsing_toobar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.event_list)
    ExpandableListView eventList;
    @BindView(R.id.open_close_list)
    ExpandableListView openTime;
    @BindView(R.id.services_tv)
    TextView servicesTV;
    @BindView(R.id.food_tv)
    TextView foodTv;
    @BindView(R.id.beers_tv)
    TextView beersTV;
    @BindView(R.id.shots_tv)
    TextView shotsTv;
    @BindView(R.id.hapennig_tv)
    TextView happeningTv;
    @BindView(R.id.open_tv)
    TextView openTv;

    public Place place;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private String happeningTonight;

    List<String> eventsHeader, openHeader;
    HashMap<String, List<Event>> eventsData;
    HashMap<String, List<OpentTime>>  openData;
    EventAdapter eventAdapter;
    OpenTimeAdapter openTimeAdapter;

    GoogleMap gMap;
    String[] placeInfo;
    Double latitude, longitude;
    boolean hasLocationPermission = true;
    public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    HelperClass helperClass = new HelperClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details_activity);
        mDatabase = FirebaseDatabaseUtil.getDatabase().getReference();
        storage = FirebaseStorage.getInstance();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        Bundle b = getIntent().getExtras();
        if (b != null) {
            placeInfo = b.getStringArray("placeInfo");
            latitude = b.getDouble("latitude");
            longitude = b.getDouble("longitude");
        }

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

        //SET MAP
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this);

    }

    public void bindPlaceDetailsToView() {

        String placeKey = placeInfo[0]; // or other values
        //placeKey = b.getString("placeKey");
        if (placeKey != null) {
            mDatabase.child("places").child(placeKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    place = dataSnapshot.getValue(Place.class);

                    //Get Week Day to sort out Happenning Event
                    int today = helperClass.weekDay();
                    happeningTonight = place.getEvents().get(today);

                    String happeningTxt = (happeningTonight.length() < 2) ? "No Event" : String.format("%s", happeningTonight);
                    collapsingToolbarLayout.setTitle(place.getName());
                    placeNameTV.setText(place.getName());
                    ratingTV.setText(String.format("%s", place.getRating()));

                    isInvisible(place.getFood(), foodTv, "Food available  ");
                    isInvisible(place.getBeer(), beersTV, String.format("Beer: %s", place.getBeer()));
                    isInvisible(place.getShots(), shotsTv, String.format("Shots for: %s", place.getShots()));
                    isInvisible(place.getServices(), servicesTV, String.format("Services: %s", place.getServices()));
                    isInvisible(place.getAssociatedTags(), tagsTV, String.format("Known for: %s", place.getAssociatedTags()));


                    happeningTv.setText("Happening Tonight: " + happeningTxt);
                    openTv.setText(openStatus(place.getOpen().get(today)));
                    Log.e("Today is:", "jj"+today);
                    addressTV.setText(place.getAddress());
                           /*
                           *  if (!place.getImage().isEmpty()) {
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
                           * */
                    //  }

                    Log.e(TAG, "onDataChangehhd: ");
                    //Load Events and Open/Close Times
                    //loadEvents(place.getEvents());
                    loadEvents(place.getEvents(), place.getOpen());

                    //Set Events Adapter
                    eventAdapter = new EventAdapter(eventsHeader, eventsData, getBaseContext());
                    eventList.setAdapter(eventAdapter);
                    //Set Open TIme Adapter
                    openTimeAdapter = new OpenTimeAdapter(openHeader, openData, getBaseContext());
                    openTime.setAdapter(openTimeAdapter);

                    //ClickListeners for EVents followed by those for OpenTime
                    eventList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            return false;
                        }
                    });

                    eventList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {

                            for (int x = 0; x < eventsHeader.size(); x++) {
                                if (x != groupPosition) eventList.collapseGroup(x);
                            }
                        }
                    });

                    eventList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                            Toast.makeText(PlaceDetails.this, "Event Selected", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                    //OPEN TIME CLICK
                    openTime.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            return false;
                        }
                    });

                    openTime.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {

                            for (int x = 0; x < openHeader.size(); x++) {
                                if (x != groupPosition) openTime.collapseGroup(x);
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /*Load Events*/
    private void loadEvents(ArrayList<String> eventList, ArrayList<String> openTime) {
        eventsHeader = new ArrayList<>();
        openHeader = new ArrayList<>();
        eventsData = new HashMap<>();
        openData = new HashMap<>();

        // Adding child data
        eventsHeader.add("Events");
        openHeader.add("Open/ Close time");

        // Adding child data
        List<OpentTime> openItem = new ArrayList<>();
        List<Event> eventItem = new ArrayList<>();

        for (int x = 0; x < eventList.size(); x++) {
            String event = (eventList.get(x).length() > 1) ? eventList.get(x) : "Closed";
            String hapenning = (eventList.get(x).length() > 1 && eventList.get(x) == happeningTonight) ? "(Happening tonight)" : "";

            eventItem.add(new Event(event, " ", "", "", helperClass.getDay(x), hapenning));
            String open = (openTime.get(x).length() > 1) ? openTime.get(x) : "Closed";
            openItem.add(new OpentTime(helperClass.getDay(x), open));

            Log.e("Added Time Sub ", "We are:"+openStatus(openTime.get(x)));

        }

        eventsData.put(eventsHeader.get(0), eventItem);
        openData.put(openHeader.get(0), openItem);

    }

    public String openStatus(String openTime_) {

        String status_value = "Closed";
        //Confirm that Value is not = 0 (Closed)
        if (openTime_.length() > 1) {
            String open = openTime_.substring(0, 7);
            String close = openTime_.substring(10);

            //String challenger = "08:00 pm";
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma");
            String currentTime = new SimpleDateFormat("hh:mma").format(new Date());
            try {
                Date currentTime_ = dateFormat.parse(currentTime);
                Date open_ = dateFormat.parse(open);
                Date close_ = dateFormat.parse(close);

               // if (currentTime_.after(open_) && currentTime_.before(close_))
                if (currentTime_.after(open_))
                    status_value = "Open";

                Log.e("time Taken", open_.toString());
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("Proof2", " Failed");
            }
        }

        return status_value;
    }

    public void isInvisible(String value, TextView textView, String result) {
        if (value != null && !value.isEmpty()) {
            textView.setText(result);
        } else {
            textView.setVisibility(View.GONE);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        loadMap();
    }

    //Grant Location Permissions
    private boolean confirmLocationPermission(){

        //Handle User Location
        // if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION))
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details
            hasLocationPermission = false;
           ActivityCompat.requestPermissions(this, new String[]{
                   android.Manifest.permission.ACCESS_FINE_LOCATION},
                   PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
        return hasLocationPermission;
    }

    //To Be completed Later
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    hasLocationPermission = true;
            }
        }
        loadMap();
    }

    private void loadMap() {
        try {
            if (hasLocationPermission){
                gMap.setBuildingsEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                latitude = (latitude == null) ? 0.310988 : latitude;
                longitude = (longitude == null) ? 32.583919 : longitude;

                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(new LatLng(0.310988, 32.583919))
                        .bearing(0.0f)
                        .zoom(14f)
                        .tilt(0.0f)
                        .build();

                gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                LatLng location = new LatLng(latitude, longitude);
                String title = (placeInfo[1] == null) ? "Night Spot" : placeInfo[1];
                gMap.addMarker(new MarkerOptions().position(location)
                        .title(title)
                        .snippet("Hangout").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .draggable(true));
                gMap.moveCamera(CameraUpdateFactory.newLatLng(location));

            }else {
                gMap.setBuildingsEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                confirmLocationPermission();
            }
        } catch (SecurityException ex){
            Log.e("Map Exception: %s", ex.getMessage());
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
