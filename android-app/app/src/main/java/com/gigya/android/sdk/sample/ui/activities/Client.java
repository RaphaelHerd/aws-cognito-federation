package com.gigya.android.sdk.sample.ui.activities;

import android.content.Context;

import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;
import com.gigya.socialize.GSResponseListener;
import com.gigya.socialize.android.GSAPI;

import java.util.Scanner;

public class Client {

	final static String phoneNumer = "4915150713713";
	final static String password = "PassW@rt123";
	final static String firstName = "rap";
	final static String lastName = "herd";
	final static String role = "farmer"; // farmer|miller|driver
	final static String secretAnswer = "this is an answer and i don't know for what it is";

	public static void registerUser(InitializeActivity window) {
		Register register = new Register();
		register.initRegistration(window, phoneNumer,password, firstName, lastName, secretAnswer,role);

	}

	/***
	 *
	 * @param myPhoneNumber
	 * @param password
	 * @param init_token
	 */
	public static void login(String myPhoneNumber, String password, String init_token){

		//GSAPI.getInstance().initialize(window, "3_Z74lSKP8HD5TWl47B_wSG_iI7XWWwhn52sHEOaWQWhim9NgP0XlhN6Mkw97F56ew", "eu1.gigya.com");

		System.out.println("Trying to login with: "+ myPhoneNumber + " - " + password);

		GSObject params = new GSObject();
	//	params.put("regToken", init_token);
		params.put("loginID", myPhoneNumber);
		params.put("password", password);
		GSResponseListener resListener = new LoginGSResponseListener();

		// Step 3 - Sending the request
		String methodName = "accounts.login";
		GSAPI.getInstance().sendRequest(methodName, params, resListener, null);
	}
}