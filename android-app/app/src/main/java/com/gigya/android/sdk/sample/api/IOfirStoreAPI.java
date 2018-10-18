package com.gigya.android.sdk.sample.api;

import com.gigya.android.sdk.sample.model.PurchaseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IOfirStoreAPI {


    @POST("/purchase")
    Call<OfirResponse> purchaseItem(@Body PurchaseBody body);


}
