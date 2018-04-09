package com.bibangamba.ebaala;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bibangamba.ebaala.activities.PlaceDetails;
import com.bibangamba.ebaala.fragments.PlacesFragment;
import com.bibangamba.ebaala.fragments.TonightFragment;
import com.bibangamba.ebaala.model.Place;
import com.bibangamba.ebaala.utils.FirebaseDatabaseUtil;
import com.bibangamba.ebaala.viewholders.PlacesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bibangamba on 6/5/2017.
 */
public abstract class AllDataListFragment extends Fragment {
    private final String TAG = "AllDataListFragment";
    Query mQuery;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Place, PlacesViewHolder> placesAdapter;
    private RecyclerView recyclerView;
    private TextView emptyDatasetFeedbackTV;
    private LinearLayoutManager linearLayoutManager;
    private String VIEW_TAG;
    //--DONE TODO: 7/12/2017 before deploying, delete all trips and make sure empty recycler view logic works as expected
    private String emptyRecyclerViewMessage = "";


    public AllDataListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.places_recycler_view_layout, container, false);

        mDatabase = FirebaseDatabaseUtil.getDatabase().getReference();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.places_recycler_view);
        emptyDatasetFeedbackTV = (TextView) rootView.findViewById(R.id.empty_dataset_tv);
//        recyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setRecyclerViewDivider();

// Set up FirebaseRecyclerAdapter with the Query
        mQuery = getQuery(mDatabase);
        VIEW_TAG = getViewTag();

        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    recyclerView.setVisibility(View.GONE);
                    emptyDatasetFeedbackTV.setVisibility(View.VISIBLE);
                    switch (VIEW_TAG) {
                        case PlacesFragment.TAG:
                            emptyRecyclerViewMessage = "Sorry, we don't have any places listed yet.";
                            emptyDatasetFeedbackTV.setText(emptyRecyclerViewMessage);

                            break;
                        case TonightFragment.TAG:
                            emptyRecyclerViewMessage = "Nothing seems to be available tonight.";
                            emptyDatasetFeedbackTV.setText(emptyRecyclerViewMessage);

                            break;
                    }
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyDatasetFeedbackTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (VIEW_TAG.equals(PlacesFragment.TAG) || VIEW_TAG.equals(TonightFragment.TAG)) {
            placesAdapter = new FirebaseRecyclerAdapter<Place, PlacesViewHolder>(Place.class, R.layout.places_recycler_view_item, PlacesViewHolder.class, mQuery) {
                @Override
                protected void populateViewHolder(PlacesViewHolder viewHolder, Place model, int position) {
                    final DatabaseReference placeReference = getRef(position);
                    final String placeKey = placeReference.getKey();

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (VIEW_TAG) {
                                case PlacesFragment.TAG:
                                    // TODO: 10/18/2017 consider using an activity instead so the user can't access the navigation drawer when viewing details
                                    Intent intent = new Intent(getActivity(), PlaceDetails.class);
                                    Bundle args = new Bundle();
                                    args.putString("placeKey", placeKey);
                                    intent.putExtras(args);
                                    startActivity(intent);

                                    //PlaceDetailsFragment placeDetailsFragment = new PlaceDetailsFragment();
//                                    Bundle args = new Bundle();
//                                    args.putString(PlaceDetailsFragment.TRIP_KEY, placeKey);
//                                    placeDetailsFragment.setArguments(args);
//                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                    fragmentManager.beginTransaction().replace(R.id.content_frame, placeDetailsFragment).addToBackStack(null).commit();

                                    break;
                                case TonightFragment.TAG:
                                    // TODO: 10/20/2017 get tonight event data
                                    Toast.makeText(getActivity(), "Not complete yet", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });

                    viewHolder.bindDataToView(model);
                }

            };
            recyclerView.setAdapter(placesAdapter);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (placesAdapter != null) {
            placesAdapter.cleanup();
        }
    }

    public void setRecyclerViewDivider() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    public abstract String getViewTag();
}
