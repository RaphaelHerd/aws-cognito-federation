package com.gigya.android.sdk.sample.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigya.android.sdk.sample.GigyaSampleLog;
import com.gigya.android.sdk.sample.R;
import com.gigya.android.sdk.sample.ui.UIResponse;
import com.gigya.android.sdk.sample.ui.viewmodel.FingerprintViewModel;
import com.gigya.socialize.android.log.GigyaLog;
import com.gigya.socialize.android.managers.GigyaFingerprintManager;
import com.gigya.socialize.android.managers.IFingerprintCallbacks;
import com.gigya.socialize.android.managers.IFingerprintOperation;


/**
 * Created by Ofir Antebi on 24/06/2018
 */
public class FingerprintFragment extends DialogFragment {

    private static final String TAG = "FingerprintFragment";
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;
    private ImageView mIcon;
    private TextView mErrorTextView;
    private FingerprintViewModel mFingerprintViewModel;
    private IFingerprintActions mFingerprintActions;
    private IFingerprintOperation mOptInOperation;
    private ScreenType mScreenType;

    public enum ScreenType {UNLOCK, OPTIN, OPTOUT}

    public static FingerprintFragment show(FragmentManager manager, ScreenType type, IFingerprintActions fingerprintActions) {
        FingerprintFragment fragment = new FingerprintFragment();
        fragment.setCallback(fingerprintActions);
        fragment.setType(type);
        fragment.show(manager, TAG);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity();
        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        mFingerprintViewModel = ViewModelProviders.of(this).get(FingerprintViewModel.class);
        setObserver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.sign_in));
        View v = inflater.inflate(R.layout.fingerprint_dialog_container, container, false);

        mIcon = v.findViewById(R.id.fingerprint_icon);
        mErrorTextView = v.findViewById(R.id.fingerprint_status);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        GigyaSampleLog.i(TAG, "onResume");
        login();
    }

    public void setCallback(IFingerprintActions fingerprintActions) {
        mFingerprintActions = fingerprintActions;
    }

    private void setType(ScreenType type) {
        mScreenType = type;
    }


    private void setObserver() {
        GigyaSampleLog.i(TAG, "setObserver");
        mFingerprintViewModel.getLoginLiveData().observe(this, new Observer<UIResponse>() {
            @Override
            public void onChanged(@Nullable UIResponse uiResponse) {
                switch (uiResponse.getResponseType()) {
                    case SUCCESS:
                        GigyaLog.i(TAG, "SUCCESS");
                        if (mFingerprintActions != null) {
                            mFingerprintActions.onOptIn();
                        }
                        break;

                    case IN_PROGRESS:
                        GigyaLog.i(TAG, "IN_PROGRESS");
                        break;

                    case ERROR:
                        GigyaLog.i(TAG, "ERROR");
                        break;
                }
            }
        });
    }


    public void login() {
        GigyaSampleLog.i(TAG, "show");

        switch (mScreenType) {
            case UNLOCK:
                mOptInOperation = mFingerprintViewModel.unlock(new IFingerprintCallbacks() {
                    @Override
                    public void onSuccess() {
                        mFingerprintActions.onLock();
                        GigyaSampleLog.i(TAG, "show - onSuccess");
                        showSuccess();
                        mIcon.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(mFingerprintActions != null){
                                    mFingerprintActions.onUnlock();
                                }
                            }
                        }, SUCCESS_DELAY_MILLIS);
                    }

                    @Override
                    public void onCancel() {
                        GigyaSampleLog.i(TAG, "show- onCancel");
                    }

                    @Override
                    public void onNotSupported(GigyaFingerprintManager.FingerprintError error) {
                        GigyaSampleLog.i(TAG, "show - onNotSupported");
                        Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        GigyaSampleLog.i(TAG, "show - onError");
                        showError(e.getMessage());
                    }
                });
                break;

            case OPTIN:
                mOptInOperation = mFingerprintViewModel.optIn(new IFingerprintCallbacks() {
                    @Override
                    public void onSuccess() {
                        GigyaSampleLog.i(TAG, "show - onSuccess");
                        showSuccess();
                        mIcon.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(mFingerprintActions != null){
                                    mFingerprintActions.onOptIn();
                                }
                            }
                        }, SUCCESS_DELAY_MILLIS);
                    }

                    @Override
                    public void onCancel() {
                        GigyaSampleLog.i(TAG, "show- onCancel");
                    }

                    @Override
                    public void onNotSupported(GigyaFingerprintManager.FingerprintError error) {
                        GigyaSampleLog.i(TAG, "show - onNotSupported");
                        Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        GigyaSampleLog.i(TAG, "show - onError");
                        showError(e.getMessage());
                    }
                });
                break;

            case OPTOUT:
                mOptInOperation = mFingerprintViewModel.optOut(new IFingerprintCallbacks() {
                    @Override
                    public void onSuccess() {
                        GigyaSampleLog.i(TAG, "show - onSuccess");
                        showSuccess();

                        mIcon.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(mFingerprintActions != null){
                                    mFingerprintActions.onOptOut();
                                }
                            }
                        }, SUCCESS_DELAY_MILLIS);
                    }

                    @Override
                    public void onCancel() {
                        GigyaSampleLog.i(TAG, "show- onCancel");
                    }

                    @Override
                    public void onNotSupported(GigyaFingerprintManager.FingerprintError error) {
                        GigyaSampleLog.i(TAG, "show - onNotSupported");
                        Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        GigyaSampleLog.i(TAG, "show - onError");
                        showError(e.getMessage());
                    }
                });
                break;

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOptInOperation != null) {
            mOptInOperation.cancel();
        }
    }

    /**
     * Showing error
     *
     * @param error
     */
    private void showError(String error) {
        mIcon.setImageResource(R.drawable.ic_fingerprint_error);
        mErrorTextView.setText(error);
        mErrorTextView.setTextColor(mErrorTextView.getResources().getColor(R.color.warning_color, null));
        mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }

    private void showSuccess() {
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mIcon.setImageResource(R.drawable.ic_fingerprint_success);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.success_color, null));
        mErrorTextView.setText(
                mErrorTextView.getResources().getString(R.string.fingerprint_success));
    }


    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.hint_color, null));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.fingerprint_hint));
            mIcon.setImageResource(R.mipmap.ic_fp_40px);
        }
    };
}
