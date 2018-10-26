package com.example.hk.transport.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hk.transport.R;
import com.example.hk.transport.Utilities.APIs.API;
import com.example.hk.transport.Utilities.Common;
import com.example.hk.transport.Utilities.Pojos.ForgetPasswordPojo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    RelativeLayout nextBtn;
    Button continueBtn;
    EditText emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_enter_email);

        nextBtn = findViewById(R.id.nextBtn);
        emailET = findViewById(R.id.emailET);
        continueBtn = findViewById(R.id.continueBtn);

        continueBtn.setText("RESEND PASSWORD LINK");

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailET.getText().toString().equals(""))
                {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if(Common.isValidEmailAddress(emailET.getText().toString()))
                {
                    Common.showProgressDialog(ForgetPasswordActivity.this);
                    API.getWebServices().forgetPassword(emailET.getText().toString()).enqueue(new Callback<ForgetPasswordPojo>() {
                        @Override
                        public void onResponse(Call<ForgetPasswordPojo> call, Response<ForgetPasswordPojo> response) {
                            Common.dismissProgressDialog();
                            if(response.body().getStatus())
                            {
                                startActivity(new Intent(ForgetPasswordActivity.this,RegisterEnterPasswordActivity.class));
                            }
                            else
                            {
                                Toast.makeText(ForgetPasswordActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgetPasswordPojo> call, Throwable t) {
                            Common.dismissProgressDialog();
                            Toast.makeText(ForgetPasswordActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
