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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    @BindView(R.id.rbFriends)
    RadioButton rbFriends;
    @BindView(R.id.rbPublic)
    RadioButton rbPublic;
    @BindView(R.id.rgSelection)
    RadioGroup rgSelection;
    ArrayList<FacebookFriend> facebookFriends = new ArrayList<>();
    ArrayList<UserOnList> usersList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, v);
        rgSelection.check(R.id.rbFriends);
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
        rgSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbFriends:
                        getFacebookFriends();
                        break;
                    case R.id.rbPublic:
                        getPublicList();
                        break;
                }
            }
        });
        getFacebookFriends();
        return v;
    }

    private void getFacebookFriends() {
        usersList.clear();
        facebookFriends.clear();
        new GraphRequest(AccessToken.getCurrentAccessToken(), AccessToken.getCurrentAccessToken().getUserId() + "/friends", null, HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
            /* handle the result */
                JSONObject jsonObject = response.getJSONObject();
                try {
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject friend = data.getJSONObject(i);
                        FacebookFriend fFriend = new FacebookFriend(friend.get("name").toString(), friend.get("id").toString(), 100000);
                        facebookFriends.add(fFriend);
                        Log.d("SagiB Friend", friend.toString());
                    }
                    for (int i = 0; i < facebookFriends.size(); i++) {
                        FirebaseDatabase.getInstance().getReference("Users").child(facebookFriends.get(i).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                addToList(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ).executeAsync();
    }

    private void getPublicList() {
        usersList.clear();
        FirebaseDatabase.getInstance().getReference("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addToList(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                addToList(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addToList(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            User value = dataSnapshot.getValue(User.class);
            if (value != null && value.getmLocation() != null && myUser.getmLocation() != null) {
                float[] results = new float[1];
                Location.distanceBetween(value.getmLocation().getLat(), value.getmLocation().getLon(), myUser.getmLocation().getLat(), myUser.getmLocation().getLon(), results);
                usersList.add(new UserOnList(value, results[0]));
                refreshList();
            }
        }
    }

    private String getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> fromLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
            if (fromLocation != null && fromLocation.size() > 0) {
                Address address = fromLocation.get(0);
                StringBuilder thisAddress = new StringBuilder("Your current location is:\n");
                for (int i = 0; i < 3; i++) {
                    if (address.getAddressLine(i) != null) {
                        if (i == 0) {
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
        mLocation newmLocation = new mLocation(location.getLatitude(), location.getLongitude(), Calendar.getInstance().getTimeInMillis());
        currentUser.setmLocation(newmLocation);
        FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUuid()).setValue(currentUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void refreshList() {
        UsersListAdapter usersListAdapter = new UsersListAdapter(usersList, getContext());
        rvUsersInRadius.setAdapter(usersListAdapter);
        rvUsersInRadius.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UsersListViewHolder> {

        private ArrayList<UserOnList> data;
        private Context context;
        private LayoutInflater inflater;

        public UsersListAdapter(ArrayList<UserOnList> data, Context context) {
            this.data = data;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public UsersListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.person_item, parent, false);
            return new UsersListViewHolder(v);
        }

        @Override
        public void onBindViewHolder(UsersListViewHolder holder, int position) {
            UserOnList userOnList = data.get(position);
            float distance = userOnList.getDistance();
            if (distance >= 1000) {
                distance = distance / 1000;
                holder.tvDistance.setText(String.valueOf(new DecimalFormat("##.##").format(distance) + "km"));
            } else {
                holder.tvDistance.setText(String.valueOf(distance + "m"));
            }
            holder.tvUserName.setText(userOnList.getUser().getDisplayName());
            holder.ivProfile.setImageURI(userOnList.getUser().getProfileImg());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class UsersListViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView ivProfile;
            TextView tvUserName;
            TextView tvDistance;

            public UsersListViewHolder(View itemView) {
                super(itemView);
                ivProfile = itemView.findViewById(R.id.ivProfile);
                tvUserName = itemView.findViewById(R.id.tvUserName);
                tvDistance = itemView.findViewById(R.id.tvDistance);
            }
        }
    }
}
