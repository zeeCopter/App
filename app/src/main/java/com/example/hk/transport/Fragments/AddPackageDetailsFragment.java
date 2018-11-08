package com.example.hk.transport.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hk.transport.FindingYourRideActivity;
import com.example.hk.transport.Fragments.HomeSubFragments.GoBookingFragment;
import com.example.hk.transport.Fragments.HomeSubFragments.HomeFragment;
import com.example.hk.transport.MasterActivity;
import com.example.hk.transport.R;

public class AddPackageDetailsFragment extends Fragment{

    Button nextBtn;

    public static AddPackageDetailsFragment addPackageDetailsFragment;

    public static AddPackageDetailsFragment getInstance() {
        if (addPackageDetailsFragment == null)
            return new AddPackageDetailsFragment();
        else
            return addPackageDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_package_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nextBtn = view.findViewById(R.id.nextBtn);

        ((MasterActivity)getActivity()).changeTitle("ADD PACKAGE DETAILS");

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MasterActivity)getActivity()).changeFragmentWithBack(HomeFragment.getInstance(),6);
            }
        });
    }
}
