package com.gigya.android.sdk.sample;

public interface IGigyaEvents {

    enum EventType {LOGIN, LOGOUT, LOAD, CLOSE, ERROR}

    void onEvent(EventType eventType);
}
