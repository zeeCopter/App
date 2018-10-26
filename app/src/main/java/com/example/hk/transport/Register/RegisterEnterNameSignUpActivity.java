package com.example.hk.transport.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hk.transport.MasterActivity;
import com.example.hk.transport.R;
import com.example.hk.transport.Utilities.Common;

public class RegisterEnterNameSignUpActivity extends AppCompatActivity {

    Button continueBtn;
    TextView secondHeadingTV,firstNameET,lastNameET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_enter_name);

        continueBtn = findViewById(R.id.continueBtn);
        secondHeadingTV = findViewById(R.id.secondHeadingTV);
        firstNameET = findViewById(R.id.firstNameET);
        lastNameET = findViewById(R.id.lastNameET);

        secondHeadingTV.setText(secondHeadingTV.getText().toString()+" "+ Common.registerMobileNumber);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstNameET.getText().toString().equals(""))
                    Toast.makeText(RegisterEnterNameSignUpActivity.this,"Enter First Name",Toast.LENGTH_SHORT).show();
                else if(lastNameET.getText().toString().equals(""))
                    Toast.makeText(RegisterEnterNameSignUpActivity.this,"Enter Last Name",Toast.LENGTH_SHORT).show();
                else
                {
                    Common.registerFirstName = firstNameET.getText().toString();
                    Common.registerLastName = lastNameET.getText().toString();
                    startActivity(new Intent(RegisterEnterNameSignUpActivity.this,RegisterEnterPasswordSignUpActivity.class));
                }
            }
        });
    }
}