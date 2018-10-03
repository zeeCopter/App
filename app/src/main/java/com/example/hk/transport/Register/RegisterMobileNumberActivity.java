package com.example.hk.transport.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.hk.transport.R;

public class RegisterMobileNumberActivity extends AppCompatActivity {

    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile_number);

        continueBtn = findViewById(R.id.continueBtn);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterMobileNumberActivity.this,RegisterVerifyMobileActivity.class));
            }
        });

    }
}