package com.example.rouletteapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import util.SessionManeger;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    ImageView gameplay,ImgDeposit;
    SessionManeger sessionManeger;
    String membercode;
    TextView TV_UserName,TV_Email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManeger = new SessionManeger(getApplicationContext());

        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        String userName = hashMap.get(SessionManeger.KEY_ID);
        String userEmail = hashMap.get(SessionManeger.KEY_EMAIL);
        membercode = hashMap.get(SessionManeger.MEMBER_ID);

        gameplay = (ImageView) findViewById(R.id.img_game);
        ImgDeposit = (ImageView) findViewById(R.id.img_game2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View hView = navigationView.getHeaderView(0);

        TV_UserName = (TextView)  hView.findViewById(R.id.txt_name);
        TV_Email = (TextView)  hView.findViewById(R.id.txt_mobile);
        TV_UserName.setText(userName);
        TV_Email.setText(userEmail);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        gameplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, PlayGameActivity.class);
                intent.putExtra("membercode","235656");
                startActivity(intent);
            }
        });

        ImgDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, DepositActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent=new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_deposit) {

        } else if (id == R.id.nav_withdrawl) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(HomeActivity.this)
                    .setMessage("Are you sure you want to logout?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            sessionManeger.logoutUser();
                            finish();
                        }
                    }).setNegativeButton("No", null).show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
