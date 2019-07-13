package com.example.rouletteapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

public class PlayGameActivity extends AppCompatActivity
{
    private WebView mywebview;
    private String currentUrl = "http://site0.bidbch.com/";
    private String memberCode;
    //Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash_board);
      //  currentUrl = getIntent().getStringExtra("url");

        mywebview = (WebView)findViewById(R.id.webView1);
        mywebview  = new WebView(this);
        mywebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        mywebview .loadUrl(currentUrl);
        setContentView(mywebview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
       /* for (int i=0;i<20;i++)
        {
            handler=new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    Intent intent=new Intent(PlayGameActivity.this,PlayGameActivity.class);
                    startActivity(intent);
                    finish();
                }
            },20000);
        }*/
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Really Exit?").setMessage("Are you sure you want to exit game?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        Intent intent = new Intent(PlayGameActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
