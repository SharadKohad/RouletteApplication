package com.example.rouletteapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import util.SessionManeger;

public class ProfileActivity extends AppCompatActivity
{
    SessionManeger sessionManeger;
    EditText ET_Email,ET_MobileNo,ET_MemberName;
    TextView TV_Name;
    ImageView IV_Back_Arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private void init() {
        sessionManeger = new SessionManeger(getApplicationContext());
        TV_Name = (TextView) findViewById(R.id.txt_ProfileName);
        ET_Email = (EditText) findViewById(R.id.EditText_ProfileEmailId);
        ET_MobileNo = (EditText) findViewById(R.id.EditText_PhoneNumber);
        ET_MemberName = (EditText) findViewById(R.id.EditText_MemberName);
        IV_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_profile);

        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        TV_Name.setText(hashMap.get(SessionManeger.KEY_ID));
        ET_MobileNo.setText(hashMap.get(SessionManeger.KEY_PHONE));//............
     //   ET_MobileNo.setText(hashMap.get(SessionManeger.KEY_NAME));
        ET_Email.setText(hashMap.get(SessionManeger.KEY_EMAIL));

        IV_Back_Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
