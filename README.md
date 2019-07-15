## Using android library

*Step 1.* 
Add the JitPack repository to your build file.
Add it in your root build.gradle at the end of repositories:

``` java 
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

*Step 2.*
 Add the dependency

``` java 
dependencies {
	    implementation 'com.github.charithvithanage:OgoCard:0.1.4'
}
```

Add the following code to the activity

*.MainActivity*

```java
public class MainActivity extends AppCompatActivity {

    private static final String TAG ="OgoCardRegister" ;
    OgoCardView cardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardView=findViewById(R.id.cardView);
        cardView.setMerchantId(MERCHANT_ID);
        cardView.setCustomerId(CLIENT_ID);
        cardView.setReturnUrl(RETURN_URL);
        cardView.setOrderId(ORDER_ID);
        cardView.setOgoBaseUrl(OGO_BASE_URL);
        cardView.setOgoRegisterCard(OGO_REGISTER_CARD);

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
```

*.activity_main*

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

    <com.info.charith.ogocardview.OgoCardView
            android:id="@+id/cardView"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
    </com.info.charith.ogocardview.OgoCardView>

</LinearLayout>
```
