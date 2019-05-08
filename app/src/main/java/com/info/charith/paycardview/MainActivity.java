package com.info.charith.paycardview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.info.charith.ogocardview.OgoCardView;
import com.info.charith.ogocardview.WebViewListner;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="OgoCardRegister" ;
    OgoCardView cardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardView=findViewById(R.id.cardView);
        cardView.setMerchantId("lawganizer-test");
        cardView.setCustomerId("test123");
        cardView.setReturnUrl("https://sample-backend.ogo.exchange/response-handler");
        cardView.setOrderId(UUID.randomUUID().toString());
        cardView.setOgoBaseUrl("https://test-ipg.ogo.exchange");
        cardView.setOgoRegisterCard("/registercard-json");

        cardView.setListner(new WebViewListner() {
            @Override
            public void onSuccessResponse(String url) {
                Log.d(TAG,url);
            }

            @Override
            public void onErrorResponse(String error) {
                Log.d(TAG," ERROR "+error);

            }
        });


    }
}
