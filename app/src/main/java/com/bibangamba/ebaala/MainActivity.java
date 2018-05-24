package com.bibangamba.ebaala;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bibangamba.ebaala.activities.SettingsActivity;
import com.bibangamba.ebaala.fragments.PlacesFragment;
import com.bibangamba.ebaala.fragments.ProfileFragment;
import com.bibangamba.ebaala.fragments.TonightFragment;
import com.bibangamba.ebaala.model.User;
import com.bibangamba.ebaala.utils.CircleTransform;
import com.bibangamba.ebaala.utils.FirebaseDatabaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView drawerNavigation;
    String mTitle = "eBaala", mDrawerTitle;
    ActionBarDrawerToggle drawerToggle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    DatabaseReference mDatabase;
    DatabaseReference usersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabaseUtil.getDatabase().getReference();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mTitle = mDrawerTitle = getTitle().toString();
        setupDrawer();
        setupDrawerContent(drawerNavigation);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }
        View navigationDrawerHeader = drawerNavigation.getHeaderView(0);
        final LinearLayout navLL = (LinearLayout) navigationDrawerHeader.findViewById(R.id.nav_linear_layout);
        final TextView drawerEmail = (TextView) navigationDrawerHeader.findViewById(R.id.drawer_email);
        final TextView drawerUsername = (TextView) navigationDrawerHeader.findViewById(R.id.drawer_username);
        final ImageView drawerProfilePictureIV = (ImageView) navigationDrawerHeader.findViewById(R.id.drawer_profile_image);

        Log.e("TEST 1 ", "LOGEED IN");
        if (savedInstanceState == null) {
            if (getUid() != null) {
                placesMenuItemSelectedFirstTime();

                usersReference = mDatabase.child("users").child(getUid());
                usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        drawerUsername.setText(String.format("%s %s", user.getFirstname(), user.getLastname()));
                        drawerEmail.setText(user.getEmail());
                        Picasso.with(MainActivity.this).load(user.getProfilePictureURL())
                                .placeholder(R.drawable.ic_account_circle_white_36dp)
                                .error(R.drawable.ic_account_circle_white_36dp)
                                .transform(new CircleTransform())
                                .into(drawerProfilePictureIV);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }


    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (getActionBar() != null)
                    getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (getActionBar() != null)
                    getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    public void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        switch (item.getItemId()) {
                            case R.id.nav_tonight:
                                tonightMenuItemSelected();
                                break;
                            case R.id.nav_places:
                                placesMenuItemSelected();
                                break;
                            case R.id.nav_profile:
                                profileMenuItemSelected();
                                break;
                            case R.id.nav_help_feedback:
                                composeEmail(new String[]{"andrewbibangamba@gmail.com"}, getAppVersionSimple());
                                break;
                            case R.id.nav_settings:
                                settingsMenuItemSelected();
                                break;
                            case R.id.nav_uber:
                                startExternalApp("com.ubercab");
                                break;
                            case R.id.nav_safeboda:
                                startExternalApp("com.safeboda.passenger");
                                break;
                        }
                        drawer.closeDrawers();
                        return true;
                    }
                });
    }

    private String getAppVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pInfo != null) {
            String ver = pInfo.versionName;
            int verCode = pInfo.versionCode;
            return "Version: " + ver + " Version Code: " + verCode;
        } else {
            return null;
        }

    }

    private String getAppVersionSimple() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        return "Version: " + versionName + " Version Code: " + versionCode;
    }


    private void placesMenuItemSelected() {
        setTitle(R.string.places);
        drawerNavigation.setCheckedItem(R.id.nav_places);

        PlacesFragment placesFragment = new PlacesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction tripsFragmentTransaction = fragmentManager.beginTransaction();
        tripsFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.content_frame, placesFragment, PlacesFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    private void placesMenuItemSelectedFirstTime() {
        setTitle(R.string.places);
        drawerNavigation.setCheckedItem(R.id.nav_places);

        PlacesFragment placesFragment = new PlacesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction tripsFragmentTransaction = fragmentManager.beginTransaction();
        tripsFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, placesFragment, PlacesFragment.TAG)
                .commit();
    }

    private void tonightMenuItemSelected() {
        setTitle(R.string.tonight);
        drawerNavigation.setCheckedItem(R.id.nav_tonight);

        TonightFragment tonightFragment = new TonightFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction tripsFragmentTransaction = fragmentManager.beginTransaction();
        tripsFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.content_frame, tonightFragment, PlacesFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    private void profileMenuItemSelected() {
        setTitle(R.string.profile);
        drawerNavigation.setCheckedItem(R.id.nav_profile);

        ProfileFragment profileFragment = new ProfileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction tripsFragmentTransaction = fragmentManager.beginTransaction();
        tripsFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.content_frame, profileFragment, PlacesFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    private void settingsMenuItemSelected() {
        setTitle(R.string.settings);
        drawerNavigation.setCheckedItem(R.id.nav_settings);
        startActivity(new Intent(this, SettingsActivity.class));

//        SettingsFragment settingsFragment = new SettingsFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        FragmentTransaction tripsFragmentTransaction = fragmentManager.beginTransaction();
//        tripsFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .replace(R.id.content_frame, settingsFragment, PlacesFragment.TAG)
//                .addToBackStack(null)
//                .commit();
    }

    private void startExternalApp(String packageName) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            Toast.makeText(this, "Application not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
