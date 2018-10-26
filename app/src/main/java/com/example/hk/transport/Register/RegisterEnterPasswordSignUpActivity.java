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
import com.example.hk.transport.Utilities.Pojos.SignUpPojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterEnterPasswordSignUpActivity extends AppCompatActivity {

    Button loginBtn;
    EditText passwordET;
    TextView createAccountTV,forgotPasswordTV,secondHeadingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_enter_password);

        loginBtn = findViewById(R.id.loginBtn);
        passwordET = findViewById(R.id.passwordET);
        createAccountTV = findViewById(R.id.createAccountTV);
        forgotPasswordTV = findViewById(R.id.forgotPasswordTV);
        secondHeadingTV = findViewById(R.id.secondHeadingTV);

        secondHeadingTV.setText("SIGN UP WITH YOUR PASSWORD");

        createAccountTV.setVisibility(View.GONE);
        forgotPasswordTV.setVisibility(View.GONE);

        loginBtn.setText("SIGNUP");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordET.getText().toString().equals(""))
                    Toast.makeText(RegisterEnterPasswordSignUpActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                else
                {
                    Common.showProgressDialog(RegisterEnterPasswordSignUpActivity.this);
                    API.getWebServices().signUp("+92"+Common.registerMobileNumber,passwordET.getText().toString(),Common.registerFirstName,Common.registerLastName,Common.registerEmail).enqueue(new Callback<SignUpPojo>() {
                        @Override
                        public void onResponse(Call<SignUpPojo> call, Response<SignUpPojo> response) {
                            Common.dismissProgressDialog();
                            if(response.body().getStatus())
                            {
                                startActivity(new Intent(RegisterEnterPasswordSignUpActivity.this,RegisterEnterPasswordActivity.class));
                            }
                            else
                            {
                                Toast.makeText(RegisterEnterPasswordSignUpActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SignUpPojo> call, Throwable t) {
                            Common.dismissProgressDialog();
                            Toast.makeText(RegisterEnterPasswordSignUpActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}