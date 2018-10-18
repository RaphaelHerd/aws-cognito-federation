package com.gigya.android.sdk.sample.wrapper;


import android.content.Context;

import com.gigya.android.sdk.sample.GigyaSampleLog;
import com.gigya.android.sdk.sample.ICallback;
import com.gigya.android.sdk.sample.IGigyaEvents;
import com.gigya.android.sdk.sample.model.User;
import com.gigya.android.sdk.sample.network.commands.GetAccountInfoCommand;
import com.gigya.android.sdk.sample.network.commands.SetAccountInfoCommand;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.android.GSAPI;
import com.gigya.socialize.android.GSPluginFragment;
import com.gigya.socialize.android.event.GSAccountsEventListener;
import com.gigya.socialize.android.event.GSPluginListener;
import com.gigya.socialize.android.event.GSSocializeEventListener;
import com.gigya.socialize.android.managers.IFingerprintCallbacks;
import com.gigya.socialize.android.managers.IFingerprintOperation;

import java.util.HashSet;
import java.util.Set;

/**
 * This is an helper class that handlers all the calls to the Gigya Android SDK APIs
 */
public class GigyaSDKWrapper {

    private static final String TAG = "GigyaSDKWrapper";
    private static Set<IGigyaEvents> mEventObservers = new HashSet<>();

    /**
     * Calling to the in initializeSDK API of the Gigya Android SDK
     *
     * @param context
     * @param apiKey
     * @param apiDomain
     */
    public static void initializeSDK(Context context, String apiKey, String apiDomain) {
        GigyaSampleLog.d(TAG, "init Gigya SDK with key: " + apiKey + " apiDomain: " + apiDomain);
        GSAPI.getInstance().initialize(context, apiKey, apiDomain);
    }

    public static void showUI() {
        GigyaSampleLog.d(TAG, "showUI");
        GSAPI.getInstance().setSocializeEventListener(new GSSocializeEventListener() {
            @Override
            public void onLogin(String provider, GSObject user, Object context) {
                GigyaSampleLog.i(TAG, "(setSocializeEventListener)----onLogin----");
                notifyObservers(IGigyaEvents.EventType.LOGIN);
            }

            @Override
            public void onLogout(Object context) {
                notifyObservers(IGigyaEvents.EventType.LOGOUT);
                GigyaSampleLog.i(TAG, "(setSocializeEventListener)----onLogout----");
            }

            @Override
            public void onConnectionAdded(String provider, GSObject user, Object context) {
                GigyaSampleLog.i(TAG, "(setSocializeEventListener)----onConnectionAdded----");
            }

            @Override
            public void onConnectionRemoved(String provider, Object context) {
                GigyaSampleLog.i(TAG, "(setSocializeEventListener)----onConnectionRemoved----");
            }
        });
        GSObject params = new GSObject();
        params.put("enabledProviders", "facebook,twitter,googleplus");
        GSAPI.getInstance().showLoginUI(params, null, null);
    }

    /**
     * Calling to the showPluginDialog API of the Gigya SDK
     * This is showing the RaaS login - screenset
     */
    public static void showPlugin(String prefix, String language) {
        // "Default-RegistrationLogin"
        GigyaSampleLog.d(TAG, "showPlugin");
//        GSObject params = new GSObject();
//        params.put("screenSet", prefix + "-RegistrationLogin"); // todo: move to param
//        params.put("lang", language);
//        params.put("deviceType", "auto");
        GSObject params = new GSObject();
        params.put("screenSet", "Default-RegistrationLogin"); // todo: move to param
        params.put("deviceType", "auto");

        GSAPI.getInstance().setAccountsEventListener(new GSAccountsEventListener() {
            public void onLogin(GSObject gsObject, Object o) {
                GigyaSampleLog.i(TAG, "(setAccountsEventListener)----onLogin----");
                notifyObservers(IGigyaEvents.EventType.LOGIN);
            }

            @Override
            public void onLogout(Object o) {
                GigyaSampleLog.i(TAG, "(setAccountsEventListener)----onLogout----");
                notifyObservers(IGigyaEvents.EventType.LOGOUT);
            }
        });

        GSAPI.getInstance().showPluginDialog("accounts.screenSet", params, new GSPluginListener() {
            @Override
            public void onLoad(GSPluginFragment gsPluginFragment, GSObject gsObject) {
                GigyaSampleLog.i(TAG, "----onLoad----");
            }

            @Override
            public void onError(GSPluginFragment gsPluginFragment, GSObject gsObject) {
                GigyaSampleLog.i(TAG, "----onError---- " +
                        " Description: " + gsObject.getString("description", "") +
                        " ErrorCode: " + gsObject.getString("errorCode", "") +
                        " EventName: " + gsObject.getString("eventName", "") +
                        " toString: " + gsObject.toJsonString());
            }

            @Override
            public void onEvent(GSPluginFragment gsPluginFragment, GSObject gsObject) {
                GigyaSampleLog.i(TAG, "----onEvent----");
            }
        }, null);
    }

    /**
     * @param callback
     */
    public static void getAccountInfo(final ICallback<User, Exception> callback) {
        new GetAccountInfoCommand(callback).execute();
    }

    /**
     * @param firstName
     * @param lastMame
     * @param callback
     */
    public static void setUserInfo(String uid, String firstName, String lastMame, final ICallback<Void, Exception> callback) {
        new SetAccountInfoCommand(uid, firstName, lastMame, callback).execute();
    }

    public static IFingerprintOperation optIn(IFingerprintCallbacks response) {
        return GSAPI.getInstance().fingerprint.optIn(response);
    }

    public static IFingerprintOperation unlock(IFingerprintCallbacks response) {
        return GSAPI.getInstance().fingerprint.unlock(response);
    }

    public static void lock(IFingerprintCallbacks response) {
        GSAPI.getInstance().fingerprint.lock(response);
    }


    public static IFingerprintOperation optOut(IFingerprintCallbacks response) {
        return GSAPI.getInstance().fingerprint.optOut(response);
    }

    public static boolean isOptIn() {
        return GSAPI.getInstance().fingerprint.isOptIn();
    }


    /**
     * Logging out from Gigya SDK
     */
    public static void logout() {
        GSAPI.getInstance().logout();
    }

    public static void addObserver(IGigyaEvents gigyaEvents) {
        mEventObservers.add(gigyaEvents);
    }

    public static void removeObserver(IGigyaEvents gigyaEvents) {
        mEventObservers.remove(gigyaEvents);
    }

    private static void notifyObservers(IGigyaEvents.EventType eventType) {
        for (IGigyaEvents observer : mEventObservers) {
            observer.onEvent(eventType);
        }
    }
}
