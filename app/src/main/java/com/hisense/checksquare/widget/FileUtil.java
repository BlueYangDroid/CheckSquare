package com.hisense.checksquare.widget;

import com.hisense.checksquare.widget.log.LogUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by T.F Guo on 2017/3/3.
 */

public class FileUtil {
    public static String readFile(String fileName){

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr =  new FileReader(fileName);
            br = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            String content = builder.toString();
            LogUtil.d("readFile() <-- result = " + content);
            return content;

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }
}
