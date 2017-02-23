package com.hisense.checksquare.widget.parsetask;

import com.bluelinelabs.logansquare.LoganSquare;
import com.hisense.checksquare.entity.ParseEntity;
import com.orhanobut.logger.Logger;

import java.io.InputStream;

/**
 * Created by yanglijun.ex on 2017/2/13.
 */

public class JsonParserTask extends ParserTask {

    private static final String TAG = JsonParserTask.class.getSimpleName();

    public JsonParserTask(ParseListener parseListener, String jsonString) {
        super(parseListener, jsonString);
    }

    public JsonParserTask(ParseListener parseListener, InputStream jsonStream) {
        super(parseListener, jsonStream);
    }

    @Override
    protected ParseEntity parse(String json) {
        try {
            return LoganSquare.parse(json, ParseEntity.class);
        } catch (Exception e) {
            Logger.e(TAG, "Exception = " + e.getMessage());
            return new ParseEntity();
        } finally {
            System.gc();
        }
    }

    @Override
    protected ParseEntity parseStream(InputStream json) {
        try {
            return LoganSquare.parse(json, ParseEntity.class);
        } catch (Exception e) {
            Logger.e(TAG, "Exception = " + e.getMessage());
            return new ParseEntity();
        } finally {
            System.gc();
        }
    }
}
