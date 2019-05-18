package com.example.krish.lovetips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Registration extends AppCompatActivity implements View.OnClickListener {


    private TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        backToLogin = (TextView)findViewById(R.id.ahBackToLoginId);
        backToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ahBackToLoginId:
                startActivity(new Intent(Registration.this,Home.class));
                finish();
                break;
        }
    }
}
