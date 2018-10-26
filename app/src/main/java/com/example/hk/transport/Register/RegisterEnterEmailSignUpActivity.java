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
import com.example.hk.transport.Utilities.Common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterEnterEmailSignUpActivity extends AppCompatActivity {

    Button continueBtn;
    TextView secondHeadingTV;
    EditText emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_enter_email);

        continueBtn = findViewById(R.id.continueBtn);
        secondHeadingTV = findViewById(R.id.secondHeadingTV);
        emailET = findViewById(R.id.emailET);

        secondHeadingTV.setText(secondHeadingTV.getText().toString()+" "+ Common.registerMobileNumber);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailET.getText().toString().equals(""))
                    Toast.makeText(RegisterEnterEmailSignUpActivity.this,"Enter Email Address",Toast.LENGTH_SHORT).show();
                else if(Common.isValidEmailAddress(emailET.getText().toString()))
                {
                    Common.registerEmail = emailET.getText().toString();
                    startActivity(new Intent(RegisterEnterEmailSignUpActivity.this,RegisterEnterNameSignUpActivity.class));
                }
            }
        });
    }
}