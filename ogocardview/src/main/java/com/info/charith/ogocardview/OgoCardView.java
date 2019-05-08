package com.info.charith.ogocardview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class OgoCardView extends LinearLayout {

    /** Core Items*/
    private Context mContext;
    private View view;


    /** Core Components*/
    TextInputEditText etCardHolderName, etMonth, etYear;
    TextInputEditText etCardNumber;
    ImageView ivVisa, ivMaster;
    Button btnNext;
    private TextWatcher textWatcherCardNumber, textWatcherMonth, textWatcherYear;

    public OgoCardView(Context context) {
        super(context);
        this.mContext=context;

        init();
    }

    public OgoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;

        init();
    }

    public OgoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OgoCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext=context;

        init();
    }

    private void init() {

        this.view=this;
        inflate(mContext,R.layout.activity_add_card,this);


        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
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

    }

    public void submit() {

        if (etCardNumber.getText().toString().replace(" ", "").length() == 16 && !TextUtils.isEmpty(etCardNumber.getText().toString()) && !TextUtils.isEmpty(etMonth.getText().toString()) && etMonth.getText().length() == 2
                && !TextUtils.isEmpty(etCardHolderName.getText().toString()) && !TextUtils.isEmpty(etYear.getText().toString()) && etYear.getText().length() == 2) {



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

}

