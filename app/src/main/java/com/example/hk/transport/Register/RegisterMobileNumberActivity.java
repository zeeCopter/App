package com.example.hk.transport.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hk.transport.R;
import com.example.hk.transport.Utilities.APIs.API;
import com.example.hk.transport.Utilities.Common;

public class RegisterMobileNumberActivity extends AppCompatActivity {

    Button continueBtn;
    EditText numberET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile_number);

        continueBtn = findViewById(R.id.continueBtn);
        numberET = findViewById(R.id.numberET);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberET.getText().toString().equals(""))
                    Toast.makeText(RegisterMobileNumberActivity.this,"Please Enter Mobile Number",Toast.LENGTH_SHORT).show();
                else if(numberET.getText().toString().length() < 10)
                    Toast.makeText(RegisterMobileNumberActivity.this,"Please Enter Valid Mobile Number",Toast.LENGTH_SHORT).show();
                else
                {
                    Common.registerMobileNumber = numberET.getText().toString();
                    startActivity(new Intent(RegisterMobileNumberActivity.this,RegisterVerifyMobileActivity.class));
                }

            }
        });

    }
}