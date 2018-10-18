package com.gigya.android.sdk.sample.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.gigya.android.sdk.sample.IGigyaEvents;
import com.gigya.android.sdk.sample.managers.RegistrationManager;
import com.gigya.android.sdk.sample.ui.UIResponse;
import com.gigya.socialize.android.managers.FingerprintOperation;
import com.gigya.socialize.android.managers.IFingerprintCallbacks;
import com.gigya.socialize.android.managers.IFingerprintOperation;

/**
 * Created by Ofir Antebi on 01/07/2018
 */
public class FingerprintViewModel extends ViewModel {

    private static final String TAG = "FingerprintViewModel";

    private MutableLiveData<UIResponse> mLoginLiveData;


    public MutableLiveData<UIResponse> getLoginLiveData() {
        if (mLoginLiveData == null) {
            mLoginLiveData = new MutableLiveData<>();
        }
        return mLoginLiveData;
    }

    public IFingerprintOperation optIn(IFingerprintCallbacks response) {
        mLoginLiveData.postValue(UIResponse.IN_PROGRESS());
        return RegistrationManager.getInstance().fingerprintOptin(response);
    }

    public IFingerprintOperation optOut(IFingerprintCallbacks response) {
        mLoginLiveData.postValue(UIResponse.IN_PROGRESS());
        return RegistrationManager.getInstance().fingerprintOptout(response);
    }

    public IFingerprintOperation unlock(IFingerprintCallbacks response) {
        mLoginLiveData.postValue(UIResponse.IN_PROGRESS());
        return RegistrationManager.getInstance().fingerprintLogin(response);
    }
}
