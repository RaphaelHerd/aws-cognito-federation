package com.gigya.android.sdk.sample.network.commands;

import com.gigya.android.sdk.sample.GigyaSampleLog;
import com.gigya.android.sdk.sample.ICallback;
import com.gigya.android.sdk.sample.ICommand;
import com.gigya.android.sdk.sample.model.User;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.GSResponseListener;
import com.gigya.socialize.android.GSAPI;

public class GetAccountInfoCommand implements ICommand {

    private static final String TAG = "GetAccountInfoCommand";
    private static final String METHOD_NAME = "accounts.getAccountInfo";
    private ICallback<User, Exception> mCallback;

    public GetAccountInfoCommand(ICallback callback) {
        mCallback = callback;
    }

    @Override
    public void execute() {
        GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
                GigyaSampleLog.i(TAG, "Response:" + response.toString());
                try {
                    if (response.getErrorCode() == 0) {
                        GigyaSampleLog.i(TAG, "Got User: " + response.toString());
                        GSObject profile = response.getObject("profile", null);
                        String uid = response.getString("UID", "");

                        String fn = profile.getString("firstName", "");
                        String ln = profile.getString("lastName", "");
                        User u = new User(fn, ln, uid);
                        GigyaSampleLog.i(TAG, "Success in getAccountInfo operation. " + " firstName: " + fn + " lastName: " + ln);
                        response.getErrorCode();
                                response.getErrorMessage();
                        mCallback.onSuccess(u);
                    } else {  // Error
                        GigyaSampleLog.i(TAG, "Error: Success in getAccountInfo operation.");
                        mCallback.onError(new Exception("Error: Success in getAccountInfo operation."));
                    }
                } catch (Exception ex) {
                    mCallback.onError(ex);
                }
            }
        };

        GSAPI.getInstance().sendRequest(METHOD_NAME, null, resListener, null);
    }
}
