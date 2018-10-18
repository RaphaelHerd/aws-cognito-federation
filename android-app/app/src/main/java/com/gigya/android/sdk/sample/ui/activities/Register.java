package com.gigya.android.sdk.sample.ui.activities;

import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.GSResponseListener;
import com.gigya.socialize.android.GSAPI;

import java.util.Scanner;

public class Register {
    /***
     *
     * @param window
     * @param phoneNumer
     * @param password
     * @param firstName
     * @param lastName
     * @param secretAnswer
     * @param role
     */
    public void initRegistration(InitializeActivity window, final String phoneNumer, final String password, final String firstName, final String lastName, final String secretAnswer, final String role) {

        GSAPI.getInstance().initialize(window, "3_Z74lSKP8HD5TWl47B_wSG_iI7XWWwhn52sHEOaWQWhim9NgP0XlhN6Mkw97F56ew", "eu1.gigya.com");

        // Step 2 - Defining response listener. The response listener will handle the request's response.
        GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
            try {
                System.out.println(response.toString());
                if (response.getErrorCode() == 0) {

                    String reqToken = response.getData().getString("regToken");

                    registerAccount(phoneNumer, password, reqToken, firstName, lastName, secretAnswer, role, phoneNumer);

                } else { // Error
                    System.out.println("Got error on setStatus: " + response.getLog());
                    System.out.println(response.getErrorMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
        };
        // Step 3 - Sending the request
        String methodName = "accounts.initRegistration";
        GSAPI.getInstance().sendRequest(methodName, null, resListener, null);
    }

    /***
     *
     * @param phoneNumber
     * @param password
     * @param firstName
     * @param lastName
     * @param secretAnswer
     */
    private void registerAccount(final String phoneNumber, String password, final String reqToken, String firstName, String lastName, String secretAnswer, String role, final String phoneNumer){

        System.out.println("Trying to register with: "+ phoneNumber + " - " + password);

        // Step 1 - Defining request parameters
        GSObject params = new GSObject();
        params.put("regToken", reqToken);

        params.put("username", phoneNumber);
        params.put("secretAnswer", secretAnswer);
        params.put("password", password);
        params.put("finalizeRegistration", true);
      //  params.put("city", role); // todo change to real property

        GSObject profile = new GSObject();
        profile.put("firstName", firstName);
        profile.put("email", "email@email.de");
        profile.put("lastName", lastName);
        params.put("profile", profile);

        // Step 2 - Defining response listener. The response listener will handle the request's response.
        GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
                try {
                    System.out.println(response.toString());
                    if (response.getErrorCode() == 0) {

                        String newReqToken = response.getData().getString("regToken");
                        tfaVerification(newReqToken, reqToken, phoneNumber);

                    } else { // Error
                        System.out.println("Got error on setStatus: " + response.getLog());
                        System.out.println(response.getErrorMessage());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        // Step 3 - Sending the request
        String methodName = "accounts.register";
        GSAPI.getInstance().sendRequest(methodName, params, resListener, null);
    }

    /***
     *
     * @param newReqToken
     * @param reqToken
     * @param phoneNumer
     */
    private void tfaVerification(final String newReqToken, final String reqToken, final String phoneNumer) {
        // Step 1 - Defining request parameters
        GSObject params = new GSObject();
        params.put("regToken", newReqToken);
        params.put("provider", "gigyaPhone");
        params.put("mode", "register");

        // Step 2 - Defining response listener. The response listener will handle the request's response.
        GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
                try {
                    System.out.println(response.toString());
                    if (response.getErrorCode() == 0) {

                        String gigyaAssertion = response.getData().getString("gigyaAssertion");
                        sendVerificationCode(gigyaAssertion, newReqToken, reqToken, phoneNumer);

                    } else { // Error
                        System.out.println("Got error on setStatus: " + response.getLog());
                        System.out.println(response.getErrorMessage());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        // Step 3 - Sending the request
        String methodName = "accounts.tfa.initTFA";
        GSAPI.getInstance().sendRequest(methodName, params, resListener, null);
    }

    /***
     *
     * @param gigyaAssertion
     * @param newReqToken
     * @param reqToken
     * @param phoneNumer
     */
    private void sendVerificationCode(final String gigyaAssertion, final String newReqToken, final String reqToken, final String phoneNumer) {
        // Step 1 - Defining request parameters
        GSObject params = new GSObject();
        params.put("gigyaAssertion", gigyaAssertion);
        params.put("method", "sms");
        params.put("phone", phoneNumer);
        params.put("lang", "en");

        // Step 2 - Defining response listener. The response listener will handle the request's response.
        GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
                try {
                    System.out.println(response.toString());
                    if (response.getErrorCode() == 0) {

                        String phvToken = response.getData().getString("phvToken");

                        System.out.println("Enter SMS validation code");
                        Scanner in = new Scanner(System.in);
                        String smsCode = in.nextLine();

                        completeVerification(smsCode,phvToken, gigyaAssertion, newReqToken, reqToken);


                    } else { // Error
                        System.out.println("Got error on setStatus: " + response.getLog());
                        System.out.println(response.getErrorMessage());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        // Step 3 - Sending the request
        String methodName = "accounts.tfa.phone.sendVerificationCode";
        GSAPI.getInstance().sendRequest(methodName, params, resListener, null);

    }

    /***
     *
     * @param smsCode
     * @param phvToken
     * @param gigyaAssertion
     * @param newReqToken
     * @param reqToken
     */
    private void completeVerification(String smsCode, String phvToken, final String gigyaAssertion, final String newReqToken, final String reqToken) {
        // Step 1 - Defining request parameters
        GSObject params = new GSObject();
        params.put("gigyaAssertion", gigyaAssertion);
        params.put("phvToken", phvToken);
        params.put("code", smsCode);

        // Step 2 - Defining response listener. The response listener will handle the request's response.
        GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
                try {
                    System.out.println(response.toString());
                    if (response.getErrorCode() == 0) {

                        String providerAssertion = response.getData().getString("providerAssertion");
                        finalizeTfa(providerAssertion, gigyaAssertion, newReqToken, reqToken);


                    } else { // Error
                        System.out.println("Got error on setStatus: " + response.getLog());
                        System.out.println(response.getErrorMessage());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        // Step 3 - Sending the request
        String methodName = "accounts.tfa.phone.completeVerification";
        GSAPI.getInstance().sendRequest(methodName, params, resListener, null);
    }

    /**
     *
     * @param providerAssertion
     * @param gigyaAssertion
     * @param newReqToken
     * @param reqToken
     */
    private void finalizeTfa(String providerAssertion, final String gigyaAssertion, final String newReqToken, final String reqToken) {
        // Step 1 - Defining request parameters
        GSObject params = new GSObject();
        params.put("gigyaAssertion", gigyaAssertion);
        params.put("providerAssertion", providerAssertion);
       // params.put("regToken", newReqToken);

        // Step 2 - Defining response listener. The response listener will handle the request's response.
        GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
            try {
                System.out.println(response.toString());
                if (response.getErrorCode() == 0) {

                    String providerAssertion = response.getData().getString("providerAssertion");
                    finalizeRegistration(reqToken);


                } else { // Error
                    System.out.println("Got error on setStatus: " + response.getLog());
                    System.out.println(response.getErrorMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
        };

        // Step 3 - Sending the request
        String methodName = "accounts.tfa.finalizeTFA";
        GSAPI.getInstance().sendRequest(methodName, params, resListener, null);
    }

    /***
     *
     * @param reqToken
     */
    private void finalizeRegistration(String reqToken) {
        // Step 1 - Defining request parameters
        GSObject params = new GSObject();
        params.put("regToken", reqToken);

        // Step 2 - Defining response listener. The response listener will handle the request's response.
        GSResponseListener resListener = new GSResponseListener() {
            @Override
            public void onGSResponse(String method, GSResponse response, Object context) {
            try {
                System.out.println(response.toString());
                if (response.getErrorCode() == 0) {



                } else { // Error
                    System.out.println("Registration successful: " + response.getLog());
                    System.out.println(response.getErrorMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
        };

        // Step 3 - Sending the request
        String methodName = "accounts.finalizeRegistration";
        GSAPI.getInstance().sendRequest(methodName, params, resListener, null);
    }
}
