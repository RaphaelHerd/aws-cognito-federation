package com.gigya.android.sdk.sample.network.commands;

import com.gigya.android.sdk.sample.GigyaSampleLog;
import com.gigya.android.sdk.sample.ICallback;
import com.gigya.android.sdk.sample.ICommand;
import com.gigya.android.sdk.sample.model.User;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.GSResponseListener;
import com.gigya.socialize.android.GSAPI;

public class SetAccountInfoCommand implements ICommand {

    private static final String TAG = "SetAccountInfoCommand";
    private static final String METHOD_NAME = "accounts.setAccountInfo";
    private ICallback<User, Exception> mCallback;
    private String mFirstName;
    private String mLastName;
    private String mUid;


    public SetAccountInfoCommand(String uid, String firstName, String lastName, ICallback callback) {
        mFirstName = firstName;
        mLastName = lastName;
        mUid = uid;
        mCallback = callback;
    }


    @Override
    public void execute() {
        final GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
                try {
                    if (response.getErrorCode() == 0) { // SUCCESS! response status = OK
                        /**
                         * On Success
                         */
                    } else
                        {
                            /**
                             * On Error
                             */
                        GigyaSampleLog.i(TAG, "Error: in setUserInfo operation. " + response.toString());
                        int errorCode = response.getErrorCode();
                        String errorMessage = response.getErrorMessage();
                            /**
                             * Use the errorCode and errorMessage
                             */
                    }
                } catch (Exception ex) {
                    mCallback.onError(ex);
                }
            }
        };

        GSObject profileInfo = new GSObject();
        profileInfo.put("firstName", mFirstName);
        profileInfo.put("lastName", mLastName);

        GSObject accountInfo = new GSObject();
        accountInfo.put("UID", mUid);

        accountInfo.put("profile", profileInfo);

        GSAPI.getInstance().sendRequest(METHOD_NAME, accountInfo, resListener, null);
    }
}
