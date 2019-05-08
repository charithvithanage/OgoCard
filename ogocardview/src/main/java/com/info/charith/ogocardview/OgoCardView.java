package com.info.charith.ogocardview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cc.cloudist.acplibrary.ACProgressFlower;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OgoCardView extends LinearLayout {

    /**
     * Core Items
     */
    private Context mContext;
    private static final String TAG = "RestaurantFinder";
    private WebView webView;

    /**
     * Core Components
     */
    TextInputEditText etCardHolderName, etMonth, etYear;
    TextInputEditText etCardNumber;
    ImageView ivVisa, ivMaster;
    Button btnNext;
    LinearLayout contentView;

    private TextWatcher textWatcherCardNumber, textWatcherMonth, textWatcherYear;
    String primaryColor = "#000000";

    String MERCHANT_ID;
    String CUSTOMER_ID;
    String ORDER_ID;
    String RETURN_URL;
    String OGO_BASE_URL;
    String OGO_REGISTER_CARD;

    JSONObject addCardRequest = new JSONObject();

    String addCardRequestString;
    Gson gson;
    ACProgressFlower progressDialog;

    WebViewListner listner;

    public OgoCardView(Context context) {
        super(context);
        this.mContext = context;

        init();
    }

    public OgoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        init();
    }

    public OgoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OgoCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;

        init();
    }

    private void init() {


        inflate(mContext, R.layout.activity_add_card, this);


        gson = new Gson();

        progressDialog = Utils.showProgressDialog(mContext);

        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        contentView = findViewById(R.id.contentView);

        etCardNumber = findViewById(R.id.etCartNumber);
        etCardHolderName = findViewById(R.id.etCardHolderName);
        etMonth = findViewById(R.id.etMonth);
        etYear = findViewById(R.id.etYear);

        textWatcherCardNumber = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = etCardNumber.length();

                if (etCardNumber.getText().toString().startsWith("5")) {
                    ivVisa.setVisibility(View.GONE);
                    ivMaster.setVisibility(View.VISIBLE);
                }

                if (etCardNumber.getText().toString().startsWith("4")) {
                    ivVisa.setVisibility(View.VISIBLE);
                    ivMaster.setVisibility(View.GONE);
                }

                if (textLength == 0) {
                    ivVisa.setVisibility(View.GONE);
                    ivMaster.setVisibility(View.GONE);
                }

                if (etCardNumber.length() > 4) {
//                    etCardNumber.removeTextChangedListener(tv);
                    if (textLength > 15) {
                        s.replace(16, textLength, "");
                        etMonth.requestFocus();
                    } else {
                        for (int i = 1; i <= (textLength / 4); i++) {
                            MarginSpan marginSPan = new MarginSpan(20);
                            s.setSpan(marginSPan, (i * 4) - 1, (i * 4), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
//                    etCardNumber.addTextChangedListener(tv);
                }
            }
        };

        textWatcherMonth = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = etMonth.length();
                if (textLength > 1) {
                    if (Integer.valueOf(etMonth.getText().toString()) < 13 && Integer.valueOf(etMonth.getText().toString()) > 0) {
                        etYear.requestFocus();
                    } else {
                        etMonth.setError("Check the month");
                    }
                }
            }
        };

        textWatcherYear = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = etYear.length();


                if (textLength > 1) {
                    if (Integer.valueOf(etYear.getText().toString()) < 28 && Integer.valueOf(etYear.getText().toString()) > 17) {
                        etCardHolderName.requestFocus();
                    } else {
                        etYear.setError("Check the year");
                    }
                }
            }
        };


        etCardNumber.addTextChangedListener(textWatcherCardNumber);
        etYear.addTextChangedListener(textWatcherYear);
        etMonth.addTextChangedListener(textWatcherMonth);

        ivMaster = findViewById(R.id.ivMaster);
        ivVisa = findViewById(R.id.ivVisa);
        ivMaster.setVisibility(View.GONE);
        ivVisa.setVisibility(View.GONE);
        contentView = findViewById(R.id.contentView);
        webView = findViewById(R.id.webview);
        webView.setVisibility(View.GONE);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.setAcceptFileSchemeCookies(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);

    }

    private void setStyle() {
        etCardHolderName.setTextColor(Color.parseColor(primaryColor));
        etCardNumber.setTextColor(Color.parseColor(primaryColor));
        etMonth.setTextColor(Color.parseColor(primaryColor));
        etYear.setTextColor(Color.parseColor(primaryColor));
    }

    public void submit() {


        if (etCardNumber.getText().toString().replace(" ", "").length() == 16 && !TextUtils.isEmpty(etCardNumber.getText().toString()) && !TextUtils.isEmpty(etMonth.getText().toString()) && etMonth.getText().length() == 2
                && !TextUtils.isEmpty(etCardHolderName.getText().toString()) && !TextUtils.isEmpty(etYear.getText().toString()) && etYear.getText().length() == 2) {

            try {
                addCardRequest.put("cardHolderName", etCardHolderName.getText().toString());
                addCardRequest.put("cardNumber", etCardNumber.getText().toString().replace(" ", ""));
                addCardRequest.put("expiryMonth", etMonth.getText().toString());
                addCardRequest.put("expiryYear", etYear.getText().toString());
                addCardRequest.put("merchantId", MERCHANT_ID);
                addCardRequest.put("customerId", CUSTOMER_ID);
                addCardRequest.put("orderId", ORDER_ID);
                addCardRequest.put("returnUrl", RETURN_URL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                addCard();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {

            if (TextUtils.isEmpty(etCardNumber.getText().toString())) {
                etCardNumber.setError("You must enter card number");
            } else if (etCardNumber.getText().length() != 16) {
                etCardNumber.setError("Check your card number");
            }


            if (TextUtils.isEmpty(etCardHolderName.getText().toString())) {
                etCardHolderName.setError("You must enter card holder name");
            }

            if (TextUtils.isEmpty(etMonth.getText().toString())) {
                etMonth.setError("Enter expiry month");
            } else if (etMonth.getText().length() != 2) {
                etMonth.setError("Check expiry month");
            }

            if (TextUtils.isEmpty(etYear.getText().toString())) {
                etYear.setError("Enter expiry year");
            } else if (etYear.getText().length() != 2) {
                etYear.setError("Check expiry year");
            }

        }
    }

    private void addCard() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(mContext);
                String url = OGO_BASE_URL + OGO_REGISTER_CARD;
                //volley callback, send card details to server, if response "success = true" load web view

                Log.d(TAG, url);
                Log.d(TAG, gson.toJson(addCardRequest));

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        url, addCardRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            contentView.setVisibility(View.GONE);
                            if (response.getString("success").equals("true")) {

                                progressDialog.dismiss();

                                webView.setVisibility(View.VISIBLE);

                                webView.loadData(response.getString("result"), "text/html; " +
                                        "charset=utf-8", "UTF-8");

                                WebViewClient yourWebClient = new WebViewClient() {

                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        if (url.contains("add-success=true")) {
                                            listner.onSuccessResponse(url);
                                        }else {
                                            listner.onErrorResponse(url);
                                        }

                                        return false;
                                    }

                                    @Override
                                    public void onPageFinished(WebView view, String url) {


                                    }

                                    @Override
                                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                        super.onReceivedError(view, request, error);
                                    }

                                    @Override
                                    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                                        super.onReceivedHttpError(view, request, errorResponse);
                                    }
                                };

                                webView.setWebViewClient(yourWebClient);

                            } else {
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            contentView.setVisibility(View.GONE);
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> header = new HashMap<String, String>();
                        header.put("Content-Type", "application/json");
                        return header;
                    }
                };

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonObjectRequest);

//                new AddCardAsync().execute();
            }
        });
    }

    public void setPrimaryColor(String colorCode) {
        this.primaryColor = colorCode;
        setStyle();
    }

    public void setMerchantId(String merchantId) {
        this.MERCHANT_ID = merchantId;
    }


    public void setCustomerId(String customerId) {
        this.CUSTOMER_ID = customerId;
    }


    public void setOrderId(String orderId) {
        this.ORDER_ID = orderId;
    }

    public void setReturnUrl(String returnUrl) {
        this.RETURN_URL = returnUrl;
    }


    public void setOgoBaseUrl(String ogoBaseUrl) {
        this.OGO_BASE_URL = ogoBaseUrl;
    }


    public void setOgoRegisterCard(String ogoRegisterCard) {
        this.OGO_REGISTER_CARD = ogoRegisterCard;
    }

    public WebViewListner getListner() {
        return listner;
    }

    public void setListner(WebViewListner listner) {
        this.listner = listner;
    }
}

