package com.gigya.android.sdk.sample.ui.activities;

import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.GSResponseListener;

public class LoginGSResponseListener implements GSResponseListener {
    /***
     *
     */
    private String access_token = new String();

    /***
     *
     * @return
     */
    public String getLoginToken() {
        return access_token;
    }

    /***
     *
     */
    @Override
    public void onGSResponse(String method, GSResponse response, Object context) {
        try {
            System.out.println(response.toString());
            System.out.println("Statuscode is : " + response.getData().getString("statusReason"));
            if (response.getErrorCode() == 0) {

                GSObject responseData = response.getData();

                access_token = response.getData().getString("regToken");


            } else { // Error
                System.out.println("Got error on setStatus: " + response.getLog());
                System.out.println(response.getErrorMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
