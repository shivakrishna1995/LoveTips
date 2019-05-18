package com.example.krish.lovetips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private TextView registration, forgetPassword, SignInId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registration = (TextView)findViewById(R.id.ahRegistrationId);
        forgetPassword = (TextView)findViewById(R.id.ahForgetPasswordId);
        SignInId = (TextView)findViewById(R.id.ahSignInId);
        registration.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        SignInId.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ahRegistrationId:
                startActivity(new Intent(Home.this,Registration.class));
                finish();
                break;
            case R.id.ahForgetPasswordId:
                startActivity(new Intent(Home.this,ForgetActivity.class));
                finish();
                break;
            case R.id.ahSignInId:
                startActivity(new Intent(Home.this,LoveTips.class));
                finish();
                break;
        }
    }
}
