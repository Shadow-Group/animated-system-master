package com.osama.project34.database;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by bullhead on 9/5/17.
 */

public class Util {
    private static final String separator = ";";

    public static String makeString(ArrayList<String> recipients) {
        StringBuilder builder = new StringBuilder();
        for (String recipient : recipients) {
            builder.append(recipient);
            builder.append(separator);
        }
        return builder.toString();
    }

    public static ArrayList<String> makeArrayListFromString(String data) {
        return new ArrayList<>(Arrays.asList(data.split(separator)));

    }
}
