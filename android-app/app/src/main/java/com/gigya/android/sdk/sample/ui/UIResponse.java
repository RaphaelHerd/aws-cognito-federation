package com.gigya.android.sdk.sample.ui;

public class UIResponse<D> {

    private static final String TAG = "UIResponse";

    public enum ResponseType {IN_PROGRESS, SUCCESS, ERROR}
    private ResponseType mResponseType;
    private String mResponseMessage;
    private D mData;

    private UIResponse(D data, String responseMessage, ResponseType type) {
        mData = data;
        mResponseMessage = responseMessage;
        mResponseType = type;
    }

    public static UIResponse IN_PROGRESS() {
        return new UIResponse(null, null, ResponseType.IN_PROGRESS);
    }

    public static <D> UIResponse SUCCESS(D data) {
        return new UIResponse(data, null, ResponseType.SUCCESS);
    }

    public static UIResponse ERROR(String message) {
        return new UIResponse(null, message, ResponseType.ERROR);
    }

    public ResponseType getResponseType() {
        return mResponseType;
    }

    public String getResponseMessage() {
        return mResponseMessage;
    }

    public D getData() {
        return mData;
    }

}
