package com.gigya.android.sdk.sample.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.gigya.android.sdk.sample.ICallback;
import com.gigya.android.sdk.sample.IGigyaEvents;
import com.gigya.android.sdk.sample.managers.PurchasingManager;
import com.gigya.android.sdk.sample.managers.RegistrationManager;
import com.gigya.android.sdk.sample.model.User;
import com.gigya.android.sdk.sample.ui.UIResponse;

public class AccountViewModel extends ViewModel {

    private static final String TAG = "AccountViewModel";

    private MutableLiveData<UIResponse> mLogoutLiveData;
    private MutableLiveData<UIResponse> mUserLiveData;
    private MutableLiveData<UIResponse> mPurchaseLiveData;
    private User mCurrentUser;

    public MutableLiveData<UIResponse> getLogoutLiveData() {
        if (mLogoutLiveData == null) {
            mLogoutLiveData = new MutableLiveData<>();
        }
        return mLogoutLiveData;
    }

    public MutableLiveData<UIResponse> getAccountLiveData() {
        if (mUserLiveData == null) {
            mUserLiveData = new MutableLiveData<>();
        }
        return mUserLiveData;
    }

    public MutableLiveData<UIResponse> getPurchaseLiveData() {
        if (mPurchaseLiveData == null) {
            mPurchaseLiveData = new MutableLiveData<>();
        }
        return mPurchaseLiveData;
    }

    public void logout() {
        RegistrationManager.getInstance().addObserver(new IGigyaEvents() {
            @Override
            public void onEvent(EventType eventType) {
                if (eventType == EventType.LOGOUT) {
                    RegistrationManager.getInstance().removeObserver(this);
                    mLogoutLiveData.postValue(UIResponse.SUCCESS(null));
                }
            }
        });
        mLogoutLiveData.postValue(UIResponse.IN_PROGRESS());
        RegistrationManager.getInstance().logout();
    }


    public void setAccountDetails(String firstName, String lastName) {
        mUserLiveData.postValue(UIResponse.IN_PROGRESS());
        RegistrationManager.getInstance().setUserInfo(mCurrentUser.getUID(), firstName, lastName, new ICallback<Void, Exception>() {
            @Override
            public void onSuccess(Void result) {
                mUserLiveData.postValue(UIResponse.SUCCESS(null));
            }

            @Override
            public void onError(Exception error) {
                mUserLiveData.postValue(UIResponse.ERROR(error.getMessage()));
            }
        });

    }

    public void getAccountDetails() {
        mUserLiveData.postValue(UIResponse.IN_PROGRESS());
        RegistrationManager.getInstance().getUserInfo(new ICallback<User, Exception>() {
            @Override
            public void onSuccess(User user) {
                mCurrentUser = user;
                mUserLiveData.postValue(UIResponse.SUCCESS(user));
            }

            @Override
            public void onError(Exception error) {
                mUserLiveData.postValue(UIResponse.ERROR(error.getMessage()));
            }
        });
    }


    public void purchase(String productItem) {
        mPurchaseLiveData.postValue(UIResponse.IN_PROGRESS());
        PurchasingManager.getInstance().purchaseProduct(mCurrentUser.getUID(), productItem, new ICallback<String, Exception>() {
            @Override
            public void onSuccess(String result) {
                mPurchaseLiveData.postValue(UIResponse.SUCCESS(result));
            }

            @Override
            public void onError(Exception error) {
                mPurchaseLiveData.postValue(UIResponse.ERROR(error.getMessage()));
            }
        });
    }
}
