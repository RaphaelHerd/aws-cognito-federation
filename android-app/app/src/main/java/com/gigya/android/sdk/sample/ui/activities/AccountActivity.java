package com.gigya.android.sdk.sample.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gigya.android.sdk.sample.R;
import com.gigya.android.sdk.sample.model.User;
import com.gigya.android.sdk.sample.ui.UIResponse;
import com.gigya.android.sdk.sample.ui.fragments.FingerprintActionsImpl;
import com.gigya.android.sdk.sample.ui.fragments.FingerprintFragment;
import com.gigya.android.sdk.sample.ui.viewmodel.AccountViewModel;
import com.gigya.android.sdk.sample.wrapper.GigyaSDKWrapper;
import com.gigya.socialize.android.GSAPI;
import com.gigya.socialize.android.managers.GigyaFingerprintManager;
import com.gigya.socialize.android.managers.IFingerprintCallbacks;

/**
 *
 */
public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AccountActivity";
    private TextInputLayout mFirstNameWrapper;
    private TextInputLayout mLastNameWrapper;

    private TextView mProgressText;

    private LinearLayout mProgressLayout;
    private AccountViewModel mAccountViewModel;

    private Button optInBtn;
    private Button optOutBtn;
    private Button lockBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAccountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        initView();
        setObserver();
        getUserData();
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

    @Override
    protected void onResume() {
        super.onResume();
        validateViews();
    }

    private void validateViews(){
        if (!GSAPI.getInstance().fingerprint.isOptIn()) {
            optInBtn.setEnabled(true);
            lockBtn.setEnabled(false);
            optOutBtn.setEnabled(false);
        } else {
            optInBtn.setEnabled(false);
            optOutBtn.setEnabled(true);
            lockBtn.setEnabled(true);
        }
        if (GSAPI.getInstance().fingerprint.isLocked()) {
            lockBtn.setEnabled(false);
        }
    }

    /**
     *
     */
    private void initView() {
        mProgressLayout = findViewById(R.id.progress_bar_layout);
        mFirstNameWrapper = findViewById(R.id.firstName_input_wrapper);
        mLastNameWrapper = findViewById(R.id.lastName_input_wrapper);
        mProgressText = findViewById(R.id.progress_bar_message);

        Button btnUpdate = findViewById(R.id.button_update);
        Button btnLogout = findViewById(R.id.button_logout);
        optInBtn = findViewById(R.id.button_optin);
        optOutBtn = findViewById(R.id.button_optout);
        lockBtn = findViewById(R.id.button_lock);

        btnUpdate.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        optInBtn.setOnClickListener(this);
        optOutBtn.setOnClickListener(this);
        lockBtn.setOnClickListener(this);
    }

    private void getUserData() {
        mAccountViewModel.getAccountDetails();
    }

    private void setObserver() {
        mAccountViewModel.getLogoutLiveData().observe(this, new Observer<UIResponse>() {
            @Override
            public void onChanged(@Nullable UIResponse uiResponse) {
                switch (uiResponse.getResponseType()) {
                    case SUCCESS:
                        hideProgressBar();
                        finish();
                        break;

                    case IN_PROGRESS:
                        showProgressBar(R.string.dialog_logging_out);
                        break;

                    case ERROR:
                        hideProgressBar();
                        break;
                }
            }
        });

        mAccountViewModel.getAccountLiveData().observe(this, new Observer<UIResponse>() {

            @Override
            public void onChanged(@Nullable UIResponse uiResponse) {
                switch (uiResponse.getResponseType()) {
                    case SUCCESS:
                        hideProgressBar();
                        if (uiResponse.getData() != null) {
                            User u = (User) uiResponse.getData();
                            mFirstNameWrapper.getEditText().setText(u.getFirstName());
                            mLastNameWrapper.getEditText().setText(u.getLastName());
                        }
                        break;

                    case IN_PROGRESS:
                        showProgressBar(R.string.dialog_updating_user_data);
                        break;

                    case ERROR:
                        hideProgressBar();
                        break;
                }
            }
        });

        mAccountViewModel.getPurchaseLiveData().observe(this, new Observer<UIResponse>() {
            @Override
            public void onChanged(@Nullable UIResponse uiResponse) {
                switch (uiResponse.getResponseType()) {
                    case SUCCESS:
                        hideProgressBar();
                        if (uiResponse.getData() != null) {
                            String message = (String) uiResponse.getData();
                            Toast.makeText(AccountActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                        break;

                    case IN_PROGRESS:
                        showProgressBar(R.string.dialog_purchase_item);
                        break;

                    case ERROR:
                        hideProgressBar();
                        Toast.makeText(AccountActivity.this, "Invalid User!", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_update:
                if (isValidInput()) {
                    mFirstNameWrapper.setErrorEnabled(false);
                    mLastNameWrapper.setErrorEnabled(false);
                    String firstName = null;
                    String lastName = null;
                    Editable firstNameEditable = mFirstNameWrapper.getEditText().getText();
                    Editable lastNameEditable = mLastNameWrapper.getEditText().getText();
                    if (firstNameEditable != null) {
                        firstName = firstNameEditable.toString();
                    }
                    if (lastNameEditable != null) {
                        lastName = lastNameEditable.toString();
                    }
                    mAccountViewModel.setAccountDetails(firstName, lastName);
                } else {
                    mFirstNameWrapper.setError("First name is not valid!");
                    mLastNameWrapper.setError("Last name is not valid!");
                    //TODO: invalid input
                }
                break;

            case R.id.button_logout:
                mAccountViewModel.logout();
                break;

            case R.id.button_optin:
                final FingerprintFragment f1 = FingerprintFragment.show(getSupportFragmentManager(), FingerprintFragment.ScreenType.OPTIN, null);
                f1.setCallback( new FingerprintActionsImpl() {
                    @Override
                    public void onOptIn() {
                        validateViews();
                        f1.dismiss();
                    }
                });

                break;

            case R.id.button_optout:
                final FingerprintFragment f2 = FingerprintFragment.show(getSupportFragmentManager(), FingerprintFragment.ScreenType.OPTOUT, null);
                f2.setCallback( new FingerprintActionsImpl() {
                    @Override
                    public void onOptOut() {
                        validateViews();
                        f2.dismiss();
                    }
                });

                break;

            case R.id.button_lock:
                GigyaSDKWrapper.lock(new IFingerprintCallbacks() {
                    @Override
                    public void onSuccess() {
                        validateViews();
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onNotSupported(GigyaFingerprintManager.FingerprintError error) {

                    }
                });
                break;
        }
    }

    /**
     * Validating that there is at least one input field with no empty value
     *
     * @return
     */
    private boolean isValidInput() {

        Editable firstNameEditable = mFirstNameWrapper.getEditText().getText();
        Editable lastNameEditable = mLastNameWrapper.getEditText().getText();
        String firstName = null;
        String lastName = null;

        if (firstNameEditable != null) {
            firstName = firstNameEditable.toString();
        }
        if (lastNameEditable != null) {
            lastName = lastNameEditable.toString();
        }

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            return false;
        }
        return true;
    }


    private void showProgressBar(int messageID) {
        mProgressText.setText(messageID);
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressLayout.setVisibility(View.GONE);
    }
}
