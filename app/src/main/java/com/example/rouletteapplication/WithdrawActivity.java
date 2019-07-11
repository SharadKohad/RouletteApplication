package com.example.rouletteapplication;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import util.SessionManeger;

public class WithdrawActivity extends AppCompatActivity
{
    ImageView Img_Back;
    private Context context;
    Button Btn_Withdraw;
    String btc = "",membercode,address="";
    TextInputEditText TIET_Amount,TIET_Address;
    SessionManeger sessionManeger;
    private LoadingDots progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        init();
    }

    private void init() {
        context = this;
        sessionManeger = new SessionManeger(context);
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        membercode = hashMap.get(SessionManeger.MEMBER_ID);
        Img_Back = findViewById(R.id.img_back_arrow_withdraw);
        Btn_Withdraw = findViewById(R.id.btn_withdraw);
        TIET_Amount = findViewById(R.id.et_usd_amount);
        TIET_Address = findViewById(R.id.et_address);
        progressBar =  findViewById(R.id.reloadingdots);
        clickLisner();
    }

    private void clickLisner() {
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Btn_Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btc = TIET_Amount.getText().toString();
                if (btc.equals(""))
                {
                    Toast.makeText(context,"Please enter USD Amount",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    address = TIET_Address.getText().toString();
                    if (address.equals(""))
                    {
                        Toast.makeText(context,"Please enter BTC Address",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Btn_Withdraw.setVisibility(View.GONE);
                        withdraw(membercode,btc,"BTC",address);
                    }
                }
            }
        });
    }

    public void withdraw(final String memberCode,final String usd_Amount,final String btc_type,final String address) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addWithdrawal";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    Btn_Withdraw.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {

                        Toast.makeText(context," "+message,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(context," "+message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    Btn_Withdraw.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Btn_Withdraw.setVisibility(View.VISIBLE);
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
                params.put("membercode",memberCode);
                params.put("USD_Amount",usd_Amount);
                params.put("BTC_Type", btc_type);
                params.put("Address",address);


                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
}
