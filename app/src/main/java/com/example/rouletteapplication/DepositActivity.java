package com.example.rouletteapplication;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eyalbira.loadingdots.LoadingDots;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import util.Constant;
import util.MySingalton;
import util.SessionManeger;

public class DepositActivity extends AppCompatActivity
{
    ImageView Img_Back;
    private Context context;
    Button Btn_payment;
    Dialog dialog;
    WindowManager.LayoutParams lp;
    TextInputEditText ET_Current_rate,ET_Usd_Amount;
    String mybtc = "",address = "";
    private LoadingDots progressBar;
    String btc = "",membercode;
    SessionManeger sessionManeger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        init();
    }

    private void init() {
        context = this;
        sessionManeger = new SessionManeger(context);
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        membercode = hashMap.get(SessionManeger.MEMBER_ID);
        Img_Back = findViewById(R.id.img_back_arrow_deposit);
        Btn_payment = findViewById(R.id.btn_payment_address);
        ET_Current_rate = findViewById(R.id.current_rate);
        progressBar =  findViewById(R.id.reloadingdots);
        ET_Usd_Amount = findViewById(R.id.et_usd_amount);

        clickLisner();
        getBTC_Current_Rate();
    }

    private void clickLisner() {
        Img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btc = ET_Usd_Amount.getText().toString();
                if (btc.equals(""))
                {
                    Toast.makeText(context,"Please enter USD Amount",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Btn_payment.setVisibility(View.GONE);
                    deposit(membercode,btc,"BTC");
                }
            }
        });
    }

    public void showBounceCash(final Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_deposit_key);
        dialog.setCancelable(true);
        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ImageView ImgBRcode ;
        TextView TV_Address;

        ImgBRcode = dialog.findViewById(R.id.img_brcode);
        TV_Address = dialog.findViewById(R.id.txt_payment_address);

        Picasso.with(context).load("https://api.qrserver.com/v1/create-qr-code/?data="+address).into(ImgBRcode);
        TV_Address.setText(""+address);

        ((Button) dialog.findViewById(R.id.btn_close)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                /*Toast.makeText(context,"Diposit Succefull ",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DepositActivity.this,MainActivity.class);
                startActivity(intent);*/
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",((TextView) dialog.findViewById(R.id.txt_payment_address)).getText().toString());
                if (clipboard == null) return;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),"Copy",Toast.LENGTH_LONG).show();
            }
        });

     /*   ((ImageView) dialog.findViewById(R.id.img_copy)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",((TextView) dialog.findViewById(R.id.txt_payment_address)).getText().toString());
                if (clipboard == null) return;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),"Copy",Toast.LENGTH_LONG).show();
            }
        });*/

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void getBTC_Current_Rate() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String strurl = "https://min-api.cryptocompare.com/data/price?fsym=USD&tsyms=BTC";
        //  String strurl = "https://blockchain.info/tobtc?currency=USD&value=1";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, strurl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    String[] kv = response.split(":");
                    String value = kv[1];
                    if (value.substring(value.length()-1).equalsIgnoreCase("}"))
                    {
                        mybtc = value.substring(0,value.length()-1);
                        ET_Current_rate.setText(""+mybtc);
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e);
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        System.out.println("error...");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjectRequest);
    }

    public void deposit(final String memberCode,final String usd_Amount,final String btc_type) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"addDeposit";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    Btn_payment.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        address = jsonObject.getString("Address");
                        showBounceCash(context);
                    }
                    else
                    {
                        Toast.makeText(context," "+message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    Btn_payment.setVisibility(View.VISIBLE);
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
                        Btn_payment.setVisibility(View.VISIBLE);
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

                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(jsonObjRequest);
    }
}
