package com.sagib.whoishere;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @BindView(R.id.frame)
    FrameLayout frame;
    Context context;
    SharedPreferences prefs;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        context = this;
        prefs = getSharedPreferences("User", MODE_PRIVATE);
        ButterKnife.bind(this);
        Fresco.initialize(this);
        checkForUser();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                DatabaseReference newUser = FirebaseDatabase.getInstance().getReference("Users").push();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String facebookUID = "noFacebook";
                String userUID = newUser.getKey();
                for (UserInfo user : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                    if (user.getProviderId().equals("facebook.com")) {
                        facebookUID = AccessToken.getCurrentAccessToken().getUserId();
                        userUID = facebookUID;
                        newUser = FirebaseDatabase.getInstance().getReference("Users").child(userUID);

                    }
                }
                User user = new User(currentUser.getDisplayName(), currentUser.getPhotoUrl().toString(), currentUser.getEmail(), userUID, currentUser.getUid(), facebookUID, null);
                String userJson = gson.toJson(user);
                prefs.edit().putString("User", userJson).commit();
                newUser.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new PermissionFragment(), "Permission").commit();
                        } else {
                            updateUI();
                        }
                    }
                });
            } else {
                checkForUser();
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showToast("Sign in Cancelled");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showToast("No Internet Connection");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showToast("Unknown Error");
                    return;
                }
            }
        }
    }

    private void checkForUser() {
        if (auth.getCurrentUser() != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new PermissionFragment(), "Permission").commit();
            } else {
                updateUI();
            }
        } else {
            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.FirebaseLoginTheme)
                            .setLogo(R.drawable.original_logo)
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).setPermissions(Arrays.asList("user_friends")).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    private void updateUI() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment(), "HomeFragment").commit();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
