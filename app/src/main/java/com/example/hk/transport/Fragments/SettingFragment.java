package com.example.hk.transport.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hk.transport.MasterActivity;
import com.example.hk.transport.R;
import com.example.hk.transport.Register.RegisterEnterPasswordActivity;
import com.example.hk.transport.Utilities.APIs.API;
import com.example.hk.transport.Utilities.Common;
import com.example.hk.transport.Utilities.Pojos.ProfilePojo;
import com.example.hk.transport.Utilities.Pojos.ResetPasswordPojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {

    public static SettingFragment settingFragment;
    TextView changePasswordTV,emailTV,mobileNoTV,nameTV;
    Dialog dialog;

    public static SettingFragment getInstance()
    {
        if(settingFragment == null)
            return new SettingFragment();
        else
            return settingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MasterActivity)getActivity()).changeTitle("Setting");

        nameTV = view.findViewById(R.id.nameTV);
        mobileNoTV = view.findViewById(R.id.mobileNoTV);
        emailTV = view.findViewById(R.id.emailTV);
        changePasswordTV = view.findViewById(R.id.changePasswordTV);

        changePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordDialog();
            }
        });

        getProfile();
    }

    private void getProfile()
    {
        Common.showProgressDialog(getActivity());
        API.getWebServices().getProfile(Common.loginPojo.getUserId()).enqueue(new Callback<ProfilePojo>() {
            @Override
            public void onResponse(Call<ProfilePojo> call, Response<ProfilePojo> response) {
                Common.dismissProgressDialog();
                if(response.body().getStatus())
                {
                    nameTV.setText(response.body().getFirstName()+" "+response.body().getLastName());
                    mobileNoTV.setText(response.body().getMobileNumber());
                    emailTV.setText(response.body().getEmailAddress());
                }
                else
                {
                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfilePojo> call, Throwable t) {
                Common.dismissProgressDialog();
                Toast.makeText(getActivity(), "Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePasswordDialog()
    {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);

        final EditText oldPasswordET,newPasswordET;
        oldPasswordET = dialog.findViewById(R.id.oldPasswordET);
        newPasswordET = dialog.findViewById(R.id.newPasswordET);

        Button changePasswordBtn = (Button) dialog.findViewById(R.id.changePasswordBtn);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oldPasswordET.getText().toString().equals(""))
                    Toast.makeText(getActivity(),"Enter Old Password",Toast.LENGTH_SHORT).show();
                else if(newPasswordET.getText().toString().equals(""))
                    Toast.makeText(getActivity(),"Enter Old Password",Toast.LENGTH_SHORT).show();
                else
                {
                    Common.showProgressDialog(getActivity());
                    API.getWebServices().resetPassword(Common.loginPojo.getUserId(),newPasswordET.getText().toString(),oldPasswordET.getText().toString()).enqueue(new Callback<ResetPasswordPojo>() {
                        @Override
                        public void onResponse(Call<ResetPasswordPojo> call, Response<ResetPasswordPojo> response) {
                            Common.dismissProgressDialog();
                            if(response.body().getStatus())
                            {
                                dialog.dismiss();
                                Toast.makeText(getActivity(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResetPasswordPojo> call, Throwable t) {
                            Common.dismissProgressDialog();
                            Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        dialog.show();
    }
}
