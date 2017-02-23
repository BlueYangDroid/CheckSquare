package com.hisense.checksquare.widget;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yanglijun.ex on 2017/2/23.
 */

public class CommandUtil {

    public static String processCommand(String... cmd) {

        String result = "";
        ProcessBuilder builder;
        InputStream in = null;
        try {

            builder = new ProcessBuilder(cmd);
            Process process = builder.start();
            in = process.getInputStream();
            byte[] re = new byte[24];
            int read;
            while ((read = in.read(re)) != -1) {
                result = result + new String(re, 0, read);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result.trim();
    }
}
