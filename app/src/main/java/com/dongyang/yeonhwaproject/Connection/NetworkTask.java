package com.dongyang.yeonhwaproject.Connection;


import android.content.ContentValues;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by JongHwa on 2018-06-08.
 */

public class NetworkTask {

    public InputStream request(String url, ContentValues params){
        HttpURLConnection urlConn = null;
        StringBuilder sbParams = new StringBuilder();

        if (params == null) sbParams.append("");
        else{
            sbParams.append("?");

            boolean isAnd = false;

            String key;
            String value;

            for(Map.Entry<String, Object> parameter : params.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                if(isAnd)   sbParams.append("&");

                sbParams.append(key).append("=").append(value);

                if(!isAnd){
                    if(params.size() >= 2)  isAnd = true;
                }
            }
        }

        String newURL = url + sbParams.toString();

        try {
            URL requestURL = new URL(newURL);
            urlConn = (HttpURLConnection) requestURL.openConnection();

            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Accept-Chatset", "UTF-8");
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

            if(urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)  return null;

            return urlConn.getInputStream();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
