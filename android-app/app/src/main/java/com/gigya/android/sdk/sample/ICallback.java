package com.gigya.android.sdk.sample;

public interface ICallback<R, E extends Throwable> {

    void onSuccess(R result);
    void onError(E error);
}
