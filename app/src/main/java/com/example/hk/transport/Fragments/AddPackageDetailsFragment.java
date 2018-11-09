package com.example.hk.transport.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hk.transport.FindingYourRideActivity;
import com.example.hk.transport.Fragments.HomeSubFragments.GoBookingFragment;
import com.example.hk.transport.Fragments.HomeSubFragments.HomeFragment;
import com.example.hk.transport.MasterActivity;
import com.example.hk.transport.R;
import com.example.hk.transport.Utilities.APIs.API;
import com.example.hk.transport.Utilities.Common;
import com.example.hk.transport.Utilities.Pojos.ModulePojo;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPackageDetailsFragment extends Fragment{

    Button nextBtn;
    EditText dimensionET,weightET;

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
        dimensionET = view.findViewById(R.id.dimensionET);
        weightET = view.findViewById(R.id.weightET);

        ((MasterActivity)getActivity()).changeTitle("ADD PACKAGE DETAILS");

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getModule();
            }
        });
    }

    private void getModule()
    {
        if(weightET.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(),"Enter Weight",Toast.LENGTH_SHORT).show();
        }
        else if(dimensionET.getText().toString().equals(""))
        {
            Toast.makeText(getActivity(),"Enter Dimensions",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Common.showProgressDialog(getActivity());
            API.getWebServices().getModule(4,22).enqueue(new Callback<ModulePojo>() {
                @Override
                public void onResponse(Call<ModulePojo> call, Response<ModulePojo> response) {
                    Common.dismissProgressDialog();
                    if(response.body().getStatus())
                    {
                        Common.modulePojo = response.body();
                        ((MasterActivity)getActivity()).changeFragmentWithBack(HomeFragment.getInstance(),6);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModulePojo> call, Throwable t) {
                    Common.dismissProgressDialog();
                    Toast.makeText(getActivity(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
