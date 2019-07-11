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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SessionManeger sessionManeger;
    String membercode;
    TextView TV_UserName,TV_Email;
    LinearLayout gameplay,ImgDeposit,ImgWithdraw;
    String numbber = "0.00007853";
    Button Btn_Diposit;
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

        gameplay = (LinearLayout) findViewById(R.id.ll_game);
        ImgDeposit = (LinearLayout) findViewById(R.id.ll_deposit);
        ImgWithdraw = (LinearLayout) findViewById(R.id.ll_withdraw);
        Btn_Diposit = (Button) findViewById(R.id.btn_deposit);

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
        clickable();

       dashBoardData(membercode);
    }

    public void clickable() {
        gameplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(HomeActivity.this, PlayGameActivity.class);
                intent.putExtra("url","http://site0.bidbch.com/");
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

        ImgWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, WithdrawActivity.class);
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
        if (id == R.id.nav_profile)
        {
            Intent intent=new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
            // Handle the camera action
        }
        else if (id == R.id.nav_deposit)
        {
            Intent intent=new Intent(HomeActivity.this, DepositActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_withdrawl)
        {
            Intent intent=new Intent(HomeActivity.this, WithdrawActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_tools) {

        }
        else if (id == R.id.nav_share) {

        }
        else if (id == R.id.nav_logout) {
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

    public void dashBoardData(final String memberId) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"getDashboard?membercode="+memberId;
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    int total_Balance = jsonObject.getInt("Total_Balance");
                  //  String Total_Direct_Income = jsonObject.getString("Total_Direct_Income");
                  //  String Total_Direct_Referral = jsonObject.getString("Total_Direct_Referral");
                    int Bonus_Cash = jsonObject.getInt("Bonus_Cash");
                    Constant.TOTAL_BALANCE = total_Balance;
                    Constant.BOUNCE_CASH = Bonus_Cash;
                    Btn_Diposit.setText(""+total_Balance);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String message= "";
                if (error instanceof ServerError)
                {
                    message = "The server could not be found. Please try again after some time!!";
                }
                else if (error instanceof TimeoutError)
                {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                System.out.println("error........"+error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Content-Type","application/json");
                return headers;
            }
        };
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
    }
}
