package com.gigya.android.sdk.sample.managers;

import com.gigya.android.sdk.sample.ICallback;
import com.gigya.android.sdk.sample.network.commands.PurchaseCommand;

public class PurchasingManager {

    private static final String TAG = "PurchasingManager";
    private static volatile PurchasingManager mInstance;

    private PurchasingManager() {
    }

    public static PurchasingManager getInstance() {
        if (mInstance == null) {
            synchronized (PurchasingManager.class) {
                if (mInstance == null) {
                    mInstance = new PurchasingManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * Sending purchase request to the local server
     * @param userId
     * @param productId
     * @param callback
     */
    public void purchaseProduct(String userId, String productId, final ICallback<String, Exception> callback) {
        new PurchaseCommand(userId, productId, new ICallback<String, Exception>() {
            @Override
            public void onSuccess(String message) {
                callback.onSuccess(message);
            }

            @Override
            public void onError(Exception error) {
                callback.onError(error);
            }
        }).execute();
    }
}
