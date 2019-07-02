package com.example.rouletteapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eyalbira.loadingdots.LoadingDots;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import util.Constant;
import util.MySingalton;

public class RegistrationActivity extends AppCompatActivity
{
    private Context context;
    private TextView TV_SignIn;
    private Button Btn_SignUp;
    TextInputEditText TIET_user,TIET_email_id,TIET_mobileNo,TIET_sponsorId,TIET_password;
    CheckBox checkBox_SponserId;
    private LoadingDots progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }
    private void init()
    {
        context = this;
        TV_SignIn = findViewById(R.id.tv_sign_In);
        TIET_user =(TextInputEditText)findViewById(R.id.et_user_name);
        TIET_email_id = (TextInputEditText)findViewById(R.id.et_email_id);
        TIET_mobileNo = (TextInputEditText)findViewById(R.id.et_mobile_no);
        TIET_sponsorId = (TextInputEditText) findViewById(R.id.et_sponser_id);
        TIET_password = (TextInputEditText) findViewById(R.id.et_user_password);
        Btn_SignUp = (Button) findViewById(R.id.btn_signup);
        checkBox_SponserId = (CheckBox) findViewById(R.id.checkboxspoinerId);
        progressBar =  findViewById(R.id.reloadingdots);

        clickLisner();
    }

    private void clickLisner()
    {
        TV_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Btn_SignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signUp();
            }
        });

        checkBox_SponserId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    TIET_sponsorId.setText("QW000001");
                }
                else
                {
                    TIET_sponsorId.setText("");
                }
            }
        });
    }

    public void signUp()
    {
        String userName = TIET_user.getText().toString();
        if (userName.equals(""))
        {
            Toast.makeText(this,"Please enter User Name",Toast.LENGTH_SHORT).show();
        }
            else
            {
                String email_id = TIET_email_id.getText().toString();
                if (!Constant.isValidEmail(email_id))
                {
                    Toast.makeText(this,"Please enter valid email",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mobileNo = TIET_mobileNo.getText().toString();
                    if (mobileNo.equals("")|| mobileNo.length()<10)
                    {
                        Toast.makeText(this,"Please enter valid Mobile Number",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String sponsorId = TIET_sponsorId.getText().toString();
                        if (sponsorId.equals(""))
                        {
                            Toast.makeText(this,"please Enter Sponsor Id",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String password = TIET_password.getText().toString();
                            if (password.equals(""))
                            {
                                Toast.makeText(this,"please Enter Password",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Btn_SignUp.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                registration(userName,email_id,mobileNo,sponsorId,password,"255.255.255.0");
                            }
                        }
                    }
                }
            }
    }

    public void registration(final String userName, final String EmailId, final String mobileNo, final String sponserid,final String password,final String client_ip) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addRegistration";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        // String username = jsonObject.getString("username");
                       // String email = jsonObject.getString("EMail");
                        /*String mobileNo = jsonObject.getString("Mobile_No");
                        String userName = jsonObject.getString("Memb_Name");
                        String memberId = jsonObject.getString("membercode");
                        sessionManeger.createSession(username,userName,email,mobileNo,memberId);
                        finish();*/
                   //     signInVollySocialLogin(email);
                        Toast.makeText(context,"Registration Succefull Please Login to user account",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegistrationActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context," "+message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                    }
                }) {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",userName);
                params.put("Email",EmailId);
                params.put("Mobile_No", mobileNo);
                params.put("Sponsor_ID", sponserid);
                params.put("Password", password);
                params.put("client_ip",client_ip);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }

}
