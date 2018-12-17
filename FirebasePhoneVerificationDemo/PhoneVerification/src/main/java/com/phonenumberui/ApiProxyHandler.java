package com.phonenumberui;

import android.os.AsyncTask;
import com.phonenumberui.utility.Utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiProxyHandler extends AsyncTask<String, String, String> {

    private String _idToken;

    public ApiProxyHandler(String idRoken) {
        _idToken = idRoken;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = new String();
        try {
            URL url = new URL("https://api2.qa.thefarmin.com/DummyApiToRemoveAfterInitialCreation");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // setting the  Request Method Type
            httpURLConnection.setRequestMethod("GET");
            // adding the headers for request
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization", _idToken);

            try{
                // to log the response code of your request
                int responseCode = httpURLConnection.getResponseCode();
                Utility.log("MyHttpRequestTask doInBackground : " +responseCode);
                String responseMessage = httpURLConnection.getResponseMessage();
                // to log the response message from your server after you have tried the request.
                Utility.log("MyHttpRequestTask doInBackground : " +responseMessage);

                // handle error response code it occurs
                InputStream inputStream;
                if (200 <= responseCode && responseCode <= 299) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();

                String currentLine;
                while ((currentLine = in.readLine()) != null)
                    response.append(currentLine);

                in.close();

                String responseString = response.toString();
                Utility.log("Response Body : " + responseString);

                result = responseString;
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                // this is done so that there are no open connections left when this task is going to complete
                httpURLConnection.disconnect();
            }
        } catch (Exception ex) {
            Utility.log("ERROR :" + ex.getMessage());
            result = ex.getMessage();
        }

        return result;
    }
}
