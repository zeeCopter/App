package com.example.hk.transport.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hk.transport.MasterActivity;
import com.example.hk.transport.R;
import com.example.hk.transport.Utilities.APIs.API;
import com.example.hk.transport.Utilities.Common;
import com.example.hk.transport.Utilities.Pojos.LoginPojo;
import com.example.hk.transport.Utilities.SharePreferencesUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterEnterPasswordActivity extends AppCompatActivity {

    Button loginBtn;
    EditText passwordET;
    TextView createAccountTV,forgotPasswordTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_enter_password);

        loginBtn = findViewById(R.id.loginBtn);
        passwordET = findViewById(R.id.passwordET);
        passwordET = findViewById(R.id.passwordET);
        createAccountTV = findViewById(R.id.createAccountTV);
        forgotPasswordTV = findViewById(R.id.forgotPasswordTV);

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterEnterPasswordActivity.this,ForgetPasswordActivity.class));
            }
        });

        createAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterEnterPasswordActivity.this,RegisterEnterEmailSignUpActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordET.getText().toString().equals(""))
                    Toast.makeText(RegisterEnterPasswordActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                else
                {
                    Common.showProgressDialog(RegisterEnterPasswordActivity.this);
                    API.getWebServices().login("+92"+Common.registerMobileNumber,passwordET.getText().toString()).enqueue(new Callback<LoginPojo>() {
                        @Override
                        public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                            Common.dismissProgressDialog();
                            if(response.body().getStatus())
                            {
                                new SharePreferencesUtility(RegisterEnterPasswordActivity.this).saveLoginModel(response.body());
                                Common.loginPojo = response.body();
                                startActivity(new Intent(RegisterEnterPasswordActivity.this,MasterActivity.class));
                            }
                            else
                            {
                                Toast.makeText(RegisterEnterPasswordActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginPojo> call, Throwable t) {
                            Common.dismissProgressDialog();
                            Toast.makeText(RegisterEnterPasswordActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}