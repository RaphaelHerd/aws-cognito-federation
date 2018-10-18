package com.gigya.android.sdk.sample.network.commands;

import com.gigya.android.sdk.sample.GigyaSampleLog;
import com.gigya.android.sdk.sample.ICallback;
import com.gigya.android.sdk.sample.ICommand;
import com.gigya.android.sdk.sample.api.IOfirStoreAPI;
import com.gigya.android.sdk.sample.api.OfirResponse;
import com.gigya.android.sdk.sample.model.PurchaseBody;
import com.gigya.android.sdk.sample.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseCommand implements ICommand {

    private static final String TAG = "PurchaseCommand";
    private String mUserId;
    private String mProductId;
    private ICallback<String, Exception> mCallback;

    public PurchaseCommand(String userId, String productId, ICallback<String, Exception> callback) {
        mUserId = userId;
        mProductId = productId;
        mCallback = callback;
    }

    @Override
    public void execute() {
        IOfirStoreAPI api = RetrofitClient.getClient().create(IOfirStoreAPI.class);
        PurchaseBody body = new PurchaseBody();
        body.productId = mProductId;
        body.userId = mUserId;
        Call<OfirResponse> response = api.purchaseItem(body);
        response.enqueue(new Callback<OfirResponse>() {
            @Override
            public void onResponse(Call<OfirResponse> call, Response<OfirResponse> response) {
                if (response != null && response.body() != null && response.body().getId() == 0) {
                    GigyaSampleLog.i(TAG, "response.body().getId() " + response.body().getId());
                    int id = response.body().getId();
                    GigyaSampleLog.i(TAG, "onResponse with id " + id);
                    //Success
                    if (id == 0) {
                        String message = response.body().getMessage();
                        GigyaSampleLog.i(TAG, "onResponse with message " + message);
                        mCallback.onSuccess(message);
                    } else {
                        mCallback.onError(new Exception("Error: purchase failed"));
                    }
                } else {
                    mCallback.onError(new Exception("Error: purchase failed"));
                }
            }

            @Override
            public void onFailure(Call<OfirResponse> call, Throwable t) {
                mCallback.onError(new Exception("Error: purchase failed"));
            }
        });
    }
}
