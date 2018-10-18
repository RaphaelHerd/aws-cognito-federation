package com.gigya.android.sdk.sample.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gigya.android.sdk.sample.R;
import com.gigya.android.sdk.sample.ui.viewmodel.InitializeViewModel;
import com.gigya.android.sdk.sample.ui.views.InitScreenInputAreaView;

/**
 * login screen, used to get the api key and data center to initialize the SDK
 */
public class InitializeActivity extends AppCompatActivity {

    private static final String TAG = "InitializeActivity";
    private InitializeViewModel mInitViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Client.registerUser(this);
        setTheme(R.style.InitScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);
        mInitViewModel = ViewModelProviders.of(this).get(InitializeViewModel.class);
        setListeners();
    }

    /**
     * Setting the activity listeners
     */
    private void setListeners() {
        ((InitScreenInputAreaView) findViewById(R.id.inputAreaView)).setEventListener(new InitScreenInputAreaView.IInputAreaEvent() {
            @Override
            public void onInitBtnClicked(String apiKey, String dataCenter) {
                mInitViewModel.initialize(apiKey, dataCenter);
                showLoginActivity();
            }
        });
    }

    /**
     *
     */
    private void showLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
