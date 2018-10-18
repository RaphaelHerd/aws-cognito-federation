package com.gigya.android.sdk.sample.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gigya.android.sdk.sample.R;
import com.gigya.android.sdk.sample.ui.UIResponse;
import com.gigya.android.sdk.sample.ui.fragments.FingerprintActionsImpl;
import com.gigya.android.sdk.sample.ui.fragments.FingerprintFragment;
import com.gigya.android.sdk.sample.ui.viewmodel.LoginViewModel;

/**
 * Showing the account's info with the option to perform actions: Update, Logout and Purchase
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private LoginViewModel mLoginViewModel;
    private Button mUnlockBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        initView();
        setObserver();
    }


    @Override
    protected void onResume() {
        super.onResume();
        validateViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initialing the view
     */
    private void initView() {
        Button nativeLogin = findViewById(R.id.btn_native_login);
        Button raasLogin = findViewById(R.id.btn_raas_login);
        mUnlockBtn = findViewById(R.id.fingerprint_button);

        final EditText editLang = findViewById(R.id.screenset_lang);
        final EditText editPrefix = findViewById(R.id.screenset_prefix);


        nativeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginViewModel.showLoginUI();
            }
        });
        raasLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginViewModel.showPlugin(editPrefix.getText().toString(), editLang.getText().toString());
            }
        });

        mUnlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FingerprintFragment f = FingerprintFragment.show(getSupportFragmentManager(), FingerprintFragment.ScreenType.UNLOCK, null);
                f.setCallback(new FingerprintActionsImpl() {

                    @Override
                    public void onUnlock() {
                        f.dismiss();
                        showLoginActivity();
                    }
                });
            }
        });
    }

    private void validateViews() {
        if (mLoginViewModel.isSecured()) {
            mUnlockBtn.setEnabled(true);
        } else {
            mUnlockBtn.setEnabled(false);
        }
    }

    private void setObserver() {
        mLoginViewModel.getLoginLiveData().observe(this, new Observer<UIResponse>() {
            @Override
            public void onChanged(@Nullable UIResponse uiResponse) {
                switch (uiResponse.getResponseType()) {
                    case SUCCESS:
                        showLoginActivity();
                        break;

                    case IN_PROGRESS:
                        //TODO: Showing IN_PROGRESS indicator
                        break;

                    case ERROR:
                        Toast.makeText(LoginActivity.this, R.string.logout_error_message, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }


    /**
     *
     */
    private void showLoginActivity() {
        Intent accountIntent = new Intent(this, AccountActivity.class);
        startActivity(accountIntent);
    }
}
