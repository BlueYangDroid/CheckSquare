package com.hisense.checksquare.widget.parsetask;

import com.bluelinelabs.logansquare.LoganSquare;
import com.hisense.checksquare.entity.ParseEntity;
import com.hisense.checksquare.widget.AssetUtil;
import com.orhanobut.logger.Logger;

import java.io.InputStream;

/**
 * Created by yanglijun.ex on 2017/2/13.
 */

public class AssetParserTask extends ParserTask {

    private static final String TAG = AssetParserTask.class.getSimpleName();
    public AssetParserTask(ParseListener parseListener, String assetName) {
        super(parseListener, assetName);
    }

    @Override
    protected ParseEntity parse(String assetName) {

        InputStream inputStream = AssetUtil.getStreamFromAssets(assetName);

        try {
            return LoganSquare.parse(inputStream, ParseEntity.class);
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
