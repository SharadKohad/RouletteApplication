package com.example.rouletteapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DepositActivity extends AppCompatActivity
{
    ImageView Img_Back;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        init();
    }

    private void init() {
        context = this;
        Img_Back = findViewById(R.id.img_back_arrow_deposit);
        clickLisner();
    }

    private void clickLisner() {
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
