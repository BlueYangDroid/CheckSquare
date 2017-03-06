package com.hisense.checksquare.widget;

import com.hisense.checksquare.MyApplication;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yanglijun.ex on 2017/2/14.
 */

public class AssetUtil {

    public static final String DEFAULT_ASSET_FILENAME = "jsonProperty.txt";
    public static String getFromAssets(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader(MyApplication.getContext().getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

        /**
         * get the json property file data
         * @param fileName
         * @return
         */
        public static InputStream getStreamFromAssets(String fileName) {
        try {
            return MyApplication.getContext().getResources().getAssets().open(fileName);
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
        return null;
    }
}
