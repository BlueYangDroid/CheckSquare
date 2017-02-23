package com.hisense.checksquare.widget.parsetask;

import android.os.AsyncTask;

import com.hisense.checksquare.BuildConfig;
import com.hisense.checksquare.entity.ParseEntity;
import com.orhanobut.logger.Logger;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by yanglijun.ex on 2017/2/13.
 */

public abstract class ParserTask extends AsyncTask<Void, Void, ParseEntity> {

    private static final String TAG = ParserTask.class.getSimpleName();

    public interface ParseListener {
        void onComplete(ParseEntity parseResult);
    }

    private ParseListener mParseListener;
    private String mJsonString;
    private InputStream mJsonStream;

    protected ParserTask(ParseListener parseListener, String jsonString) {
        mParseListener = parseListener;
        mJsonString = jsonString;
    }

    protected ParserTask(ParseListener parseListener, InputStream jsonStream) {
        mParseListener = parseListener;
        mJsonStream = jsonStream;
    }

    @Override
    protected ParseEntity doInBackground(Void... params) {
        System.gc();
        long startTime = System.nanoTime();
        ParseEntity entity;
        if (mJsonString != null) {
            entity = parse(mJsonString);
        } else {
            entity = parseStream(mJsonStream);
        }
        long endTime = System.nanoTime();
        if (BuildConfig.DEBUG) {
            long duration = TimeUnit.NANOSECONDS.toMicros(endTime - startTime);
            Logger.d(TAG, "duration = " + duration);
        }

        return entity;
    }

    @Override
    protected void onPostExecute(ParseEntity parseResult) {
        if (null != mParseListener) {
            mParseListener.onComplete(parseResult);
        }
    }

    protected abstract ParseEntity parse(String json);
    protected abstract ParseEntity parseStream(InputStream json);
}