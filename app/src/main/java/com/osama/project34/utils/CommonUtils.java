package com.osama.project34.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class CommonUtils {
    public static String getJsonfromUrl(String mUrl) throws IOException {
        URL Url = new URL(mUrl);
        HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        line = sb.toString();
        connection.disconnect();
        is.close();
        sb.delete(0, sb.length());
        return line;
    }
}
