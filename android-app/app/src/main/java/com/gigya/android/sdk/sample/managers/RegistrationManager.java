package com.gigya.android.sdk.sample.managers;

import android.content.Context;

import com.gigya.android.sdk.sample.ICallback;
import com.gigya.android.sdk.sample.IGigyaEvents;
import com.gigya.android.sdk.sample.model.User;
import com.gigya.android.sdk.sample.wrapper.GigyaSDKWrapper;
import com.gigya.socialize.android.managers.FingerprintOperation;
import com.gigya.socialize.android.managers.IFingerprintCallbacks;
import com.gigya.socialize.android.managers.IFingerprintOperation;


/**
 *
 */
public class RegistrationManager {

    private static final String TAG = "RegistrationManager";
    private static volatile RegistrationManager mInstance;

    private RegistrationManager() {
    }

    public static RegistrationManager getInstance() {
        if (mInstance == null) {
            synchronized (RegistrationManager.class) {
                if (mInstance == null) {
                    mInstance = new RegistrationManager();
                }
            }
        }
        return mInstance;
    }


    /**
     * Initializing the SDK
     *
     * @param ctx
     * @param apiKey
     * @param dataCenter
     */
    public void initialize(Context ctx, String apiKey, String dataCenter) {
        GigyaSDKWrapper.initializeSDK(ctx, apiKey, dataCenter);

    }

    /**
     * Showing the UI of the SDK
     */
    public void showUI() {
        GigyaSDKWrapper.showUI();
    }

    public void showPlugin(String prefix, String language) {
        GigyaSDKWrapper.showPlugin(prefix, language);
    }

    public void getUserInfo(ICallback<User, Exception> callback) {
        GigyaSDKWrapper.getAccountInfo(callback);
    }

    public void setUserInfo(String uid, String firstName, String lastName, ICallback<Void, Exception> callback) {
        GigyaSDKWrapper.setUserInfo(uid, firstName, lastName, callback);
    }

    public IFingerprintOperation fingerprintOptin(IFingerprintCallbacks response) {
        return GigyaSDKWrapper.optIn(response);
    }

    public IFingerprintOperation fingerprintOptout(IFingerprintCallbacks response) {
        return GigyaSDKWrapper.optOut(response);
    }

    public IFingerprintOperation fingerprintLogin(IFingerprintCallbacks response) {
        return GigyaSDKWrapper.unlock(response);
    }


    public boolean isSecured() {
        return GigyaSDKWrapper.isOptIn();
    }

    /**
     * Logging out the user
     */
    public void logout() {
        GigyaSDKWrapper.logout();
    }

    public void addObserver(IGigyaEvents gigyaEvents) {
        GigyaSDKWrapper.addObserver(gigyaEvents);
    }

    public void removeObserver(IGigyaEvents gigyaEvents) {
        GigyaSDKWrapper.removeObserver(gigyaEvents);
    }


}
