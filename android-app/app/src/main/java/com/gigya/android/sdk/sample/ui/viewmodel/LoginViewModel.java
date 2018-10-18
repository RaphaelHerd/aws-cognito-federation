package com.gigya.android.sdk.sample.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.gigya.android.sdk.sample.ICallback;
import com.gigya.android.sdk.sample.IGigyaEvents;
import com.gigya.android.sdk.sample.ui.UIResponse;
import com.gigya.android.sdk.sample.managers.RegistrationManager;

/**
 * Login Screen view model, handling two login types: RaaS and native login
 */
public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";
    private MutableLiveData<UIResponse> mLoginLiveData;


    public MutableLiveData<UIResponse> getLoginLiveData(){
        if(mLoginLiveData == null){
            mLoginLiveData = new MutableLiveData<>();
        }
        return mLoginLiveData;
    }

    /**
     * Native Login
     * Calling to the show login UI API of the gigya SDK
     */
    public void showLoginUI(){
        mLoginLiveData.postValue(UIResponse.IN_PROGRESS());
        RegistrationManager.getInstance().addObserver(new IGigyaEvents() {
            @Override
            public void onEvent(EventType eventType) {
                RegistrationManager.getInstance().removeObserver(this);
                if(eventType == EventType.LOGIN){
                    mLoginLiveData.postValue(UIResponse.SUCCESS(null));
                }else{
                    mLoginLiveData.postValue(UIResponse.ERROR("Logout Failed"));
                }
            }
        });
        RegistrationManager.getInstance().showUI();
    }

    /**
     * RaaS login using screen sets
     */
    public void showPlugin(String prefix, String language){
        mLoginLiveData.postValue(UIResponse.IN_PROGRESS());
        RegistrationManager.getInstance().addObserver(new IGigyaEvents() {
            @Override
            public void onEvent(EventType eventType) {
                RegistrationManager.getInstance().removeObserver(this);
                if(eventType == EventType.LOGIN){
                    mLoginLiveData.postValue(UIResponse.SUCCESS(null));
                }else{
                    mLoginLiveData.postValue(UIResponse.ERROR("Logout Failed"));
                }
            }
        });
        RegistrationManager.getInstance().showPlugin(prefix, language);
    }


    public boolean isSecured(){
        return RegistrationManager.getInstance().isSecured();
    }
}
