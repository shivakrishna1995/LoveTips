package com.example.krish.lovetips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        backToLogin = (TextView)findViewById(R.id.afBackToSignInId);
        backToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.afBackToSignInId:
                startActivity(new Intent(ForgetActivity.this, Home.class));
                finish();
                break;
        }
    }
}
