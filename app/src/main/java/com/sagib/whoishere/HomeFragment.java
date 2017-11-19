package com.sagib.whoishere;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment {

    @BindView(R.id.btnProfile)
    SimpleDraweeView btnProfile;
    Unbinder unbinder;
    @BindView(R.id.btnMessages)
    ImageView btnMessages;
    @BindView(R.id.btnHome)
    ImageView btnHome;
    @BindView(R.id.btnSettings)
    ImageView btnSettings;
    @BindView(R.id.tvCurentLocation)
    BootstrapLabel tvCurentLocation;
    @BindView(R.id.rvUsersInRadius)
    RecyclerView rvUsersInRadius;
    FusedLocationProviderClient mFusedLocationProviderClient;
    @BindView(R.id.tvWelcome)
    TextView tvWelcome;
    Gson gson = new Gson();
    SharedPreferences prefs;
    User myUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, v);
        prefs = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvWelcome.setText(String.format("Welcome %s!", user.getDisplayName()));
            if (user.getPhotoUrl() != null)
                btnProfile.setImageURI(user.getPhotoUrl());
        }
        myUser = gson.fromJson(prefs.getString("User", ""), new TypeToken<User>() {
        }.getType());
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    tvCurentLocation.setText(getAddressFromLocation(location));
                    updateUserLocationOnDatabase(location, myUser);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> lastLocation = mFusedLocationProviderClient.getLastLocation();
            lastLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location result = task.getResult();
                    if (result != null) {
                        tvCurentLocation.setText(getAddressFromLocation(result));
                        updateUserLocationOnDatabase(result, myUser);
                    }
                }
            });
        }
        return v;
    }

    private String getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> fromLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
            if (fromLocation != null && fromLocation.size() > 0) {
                Address address = fromLocation.get(0);
                StringBuilder thisAddress = new StringBuilder("Your current location is:\n");
                for (int i = 0; i < 3; i++) {
                    if (address.getAddressLine(i) != null){
                        if (i == 0){
                            thisAddress.append(address.getAddressLine(i));
                        } else {
                            thisAddress.append(", ").append(address.getAddressLine(i));
                        }
                    }
                }
                return thisAddress.toString();
            } else {
                return "Location not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Location not found";
        }
    }

    private void updateUserLocationOnDatabase(Location location, User currentUser) {
        currentUser.setLastKnownLocation(location);
        FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUuid()).setValue(currentUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
