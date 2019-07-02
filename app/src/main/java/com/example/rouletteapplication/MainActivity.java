package com.example.rouletteapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.eyalbira.loadingdots.LoadingDots;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class MainActivity extends AppCompatActivity
{
    private Context context;
    private TextView TV_SignUp;
    private Intent intent;
    private Button btn_signin;
    private String user_name,password;
    private TextInputEditText TIET_user_name,TIET_password;
   // private ProgressBar progressBar;
    private LoadingDots progressBar;
    SessionManeger sessionManeger ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        /*ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            Toast.makeText(MainActivity.this, "Network Available", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(MainActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

        }*/
    }

    private void init() {
        context = this;
        sessionManeger = new SessionManeger(context);
        TV_SignUp = findViewById(R.id.tv_sign_up);
        TIET_user_name = (TextInputEditText) findViewById(R.id.et_user_name);
        TIET_password = (TextInputEditText) findViewById(R.id.et_user_password);
        btn_signin = (Button) findViewById(R.id.btn_signin);
        progressBar =  findViewById(R.id.loadingdots);
        clickLisner();
    }

    private void clickLisner() {
        TV_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });
    }

    public void signIn() {
        user_name = TIET_user_name.getText().toString();
        if (user_name.equals(""))
        {
            Toast.makeText(this,"Please enter valid User Name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            password = TIET_password.getText().toString();
            if (password.equals(""))
            {
                Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            }
            else
            {
                btn_signin.setVisibility(View.GONE);
                signInVolly(user_name,password);
              //  Toast.makeText(this,"Login Succesfull",Toast.LENGTH_SHORT).show();
               // progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void signInVolly(final String userId, final String Password) {
        progressBar.setVisibility(View.VISIBLE);
        btn_signin.setAlpha(0f);
        String url = Constant.URL+"getLogin";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    progressBar.setVisibility(View.GONE);
                    btn_signin.setVisibility(View.VISIBLE);
                    btn_signin.setAlpha(1f);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                      /*  String username = jsonObject.getString("username");
                        String email = ;
                        String mobileNo = ;
                        String Memb_Name = jsonObject.getString("Memb_Name");*/
                      //  String userFile = ;
                        sessionManeger.createSession(jsonObject.getString("username"),jsonObject.getString("membercode"),jsonObject.getString("Email"),jsonObject.getString("Mobile_No"),jsonObject.getString("userFile"));
                        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                        intent.putExtra("membercode",jsonObject.getString("membercode"));
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(context,""+msg,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.GONE);
                    btn_signin.setAlpha(1f);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressBar.setVisibility(View.GONE);
                btn_signin.setAlpha(1f);
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
            }
        })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userId);
                params.put("Password",Password);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
