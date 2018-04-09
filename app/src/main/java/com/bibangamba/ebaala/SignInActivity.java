package com.bibangamba.ebaala;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bibangamba.ebaala.model.User;
import com.bibangamba.ebaala.utils.FirebaseDatabaseUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    @VisibleForTesting
    public ProgressDialog mProgressDialog;
    Vibrator vibe;
    @BindView(R.id.signin_btn_relative_layout)
    RelativeLayout signinButtonRelativeLayout;
    @BindView(R.id.google_sign_in_button)
    SignInButton googleBtn;
    @BindView(R.id.fragment_holder_frame)
    FrameLayout fragmentHolderFrameLayout;
    DatabaseReference mDatabase;
    User user = new User();
    // [END declare_auth]
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabaseUtil.getDatabase().getReference();

        setContentView(R.layout.signin_activity);
        ButterKnife.bind(this);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        if (savedInstanceState == null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser, false);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    user.setFirstname(account.getGivenName());
                    user.setLastname(account.getFamilyName());
                    if (account.getPhotoUrl() != null) {
                        user.setProfilePictureURL(account.getPhotoUrl().toString());
                    }
                }
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null, false);
                // [END_EXCLUDE]
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        getSupportFragmentManager()
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    private void updateUI(FirebaseUser user, boolean firstSignIn) {
        hideProgressDialog();
        if (user != null) {
            if (firstSignIn) {
                writeNewUserToFirebaseRTDB(user);
            }
            googleBtn.setVisibility(View.GONE);
            fragmentHolderFrameLayout.setVisibility(View.VISIBLE);
            // TODO: 10/5/2017 test and make sure this works
//            Intent startMainActivityIntent = new Intent(this, Main2Activity.class);
            Intent startMainActivityIntent = new Intent(this, MainActivity.class);
            startMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMainActivityIntent);
            finish();

//            FormsListingFragment eventListFragment = new FormsListingFragment();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                    .replace(R.id.fragment_holder_frame, eventListFragment, FormsListingFragment.TAG)
//                    .commit();

        } else {
            fragmentHolderFrameLayout.setVisibility(View.GONE);
            googleBtn.setVisibility(View.VISIBLE);
//            signinButtonRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void writeNewUserToFirebaseRTDB(FirebaseUser firebaseUser) {
        user.setEmail(firebaseUser.getEmail());
        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getIdToken());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null, false);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @OnClick(R.id.google_sign_in_button)
    void signIn() {
        Toast.makeText(this, "google sign in", Toast.LENGTH_SHORT).show();
        vibe.vibrate(50);
        googleSignIn();
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
