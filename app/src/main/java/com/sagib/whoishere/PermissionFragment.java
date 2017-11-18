package com.sagib.whoishere;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class PermissionFragment extends Fragment {

    private static final int PERMISSION_CODE = 123;
    @BindView(R.id.tvReqDesc)
    TextView tvReqDesc;
    @BindView(R.id.tvUserTitle)
    TextView tvUserTitle;
    @BindView(R.id.btnGrantAccess)
    BootstrapButton btnGrantAccess;
    @BindView(R.id.tvApprovedPermission)
    TextView tvApprovedPermission;
    @BindView(R.id.btnStart)
    BootstrapButton btnStart;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_permission, container, false);
        unbinder = ButterKnife.bind(this, v);
        tvApprovedPermission.setVisibility(View.GONE);
        btnStart.setVisibility(View.GONE);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnGrantAccess)
    public void onBtnGrantAccessClicked() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            Log.d("SagiB", "here");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("SagiB", "approved");
                tvApprovedPermission.setVisibility(View.VISIBLE);
                btnStart.setVisibility(View.VISIBLE);
            } else {
                Log.d("SagiB", "denied");
                Toast.makeText(getContext(), "You need to grant access to continue.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.btnStart)
    public void onBtnStartClicked() {
        Toast.makeText(getContext(), "Updating UI...", Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
    }
}
