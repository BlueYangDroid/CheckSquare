package com.hisense.checksquare.widget.log;

/**
 * Created by yanglijun.ex on 2017/3/10.
 */



import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Locale;

/**
 * a log client can write to file, default to sdcard dir
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 * <uses-permission android:name="android.permission.WRITE_MEDIA_STORAGE"/>
 */
public class XLogClient implements ILogClient {

    private static final String TAG = XLogClient.class.getSimpleName();

    public static final String APP_VERSION = "";

    private static final String LOGPRE_SPLIT = "|";
    private static final String LOGPRE_APPVER = APP_VERSION + LOGPRE_SPLIT;
    private static final String LOGPRE_TIME_FORMAT = "yyyyMMdd-HH:mm:ss:SSS" + LOGPRE_SPLIT;

    /* 每次打印字符串的最大长度 */
    private static final int LOGCAT_MESSAGE_MAX_LENTH = 4000;

    /* 限制每个log文件最大2M */
    private static final int MAX_LOGFILE_LENGTH = 2 * 1024 * 1024;

    /* 限制log文件个数 */
    private static final int MAX_LOGFILE_NUMBER = 5;

    /* 文件输出目录 */
    private static final String LOG_FILE_DIR = "XLog";
    private static final String FILECONTENT_ENCODE = "UTF-8";

    /* logfile缓存大小 128k */
    private static final long LOGFILE_BUFFER_MAX = 128 * 1024;

    /* 等待缓存输出 */
    private static final long LOGFILE_DELAY = 6 * 1000;
    /* 等待关闭工作线程 */
    private static final long LOGFILE_THREAD_EXIT_DELAY = 2 * 60 * 1000;

    /* 处理消息 */
    private static final int MSG_SAVE_LOG_FILE = 1;
    private static final int MSG_LOG_FILE_THREAD_EXIT = 2;

    /* 内存泄露检测开关 TODO */
    public static final boolean DEBUG_MEMORYWATCHER = true;
    /* STRICT_MODE开关 TODO */
    public static final boolean DEBUG_STRICT_MODE = false;

    private static StringBuilder logFileBuffer;
    private static Handler logFileHandler;

    private String LOGFILE_PATH;
    private String tag;
    private boolean mSaveFlag = true;

    @Override
    public void saveFile(boolean flag) {
        mSaveFlag = flag;
    }

    public void init(String tag) {
        this.tag = tag;
    }

    @Override
    public ILogClient tag(String tag) {
        if (!StringUtil.isEmpty(tag)) {
            this.tag = tag;
        }
        return this;
    }

    @Override
    public void d(Object object) {
        d(mSaveFlag, tag, object.toString());
    }

    /**
     * 公开接口
     * 打印debug级别的log
     *
     * @param msg
     * @param args
     */
    @Override
    public void d(CharSequence msg, Object... args) {
        d(mSaveFlag, tag, msg, args);
    }

    /**
     * 私有方法
     * 打印debug级别的log
     *
     * @param tag
     * @param msg
     */
    private void d(boolean isSaveLog, String tag, CharSequence msg, Object... args) {
        String logcatText = buildMessage(msg, args, LOGPRE_APPVER);
        if (logcatText != null) {
            int len = logcatText.length();
            if (len <= LOGCAT_MESSAGE_MAX_LENTH) {
                Log.d(tag, logcatText);
            } else {
                do {
                    Log.d(tag, logcatText.substring(0, LOGCAT_MESSAGE_MAX_LENTH));
                    logcatText = logcatText.substring(LOGCAT_MESSAGE_MAX_LENTH);
                    len = logcatText.length();
                } while (len > LOGCAT_MESSAGE_MAX_LENTH);
                if (len > 0) {
                    Log.d(tag, logcatText);
                }
            }
        }

        if (isSaveLog) {
            f(tag, msg, args);
        }
    }

    /**
     * 打印warn级别的log
     *
     */
    private void w(String tag, CharSequence msg, Object... objects) {
        String text = buildMessage(msg, objects, LOGPRE_APPVER);
        if (text != null) {
            int len = text.length();
            if (len <= LOGCAT_MESSAGE_MAX_LENTH) {
                Log.w(tag, text);
            } else {
                do {
                    Log.w(tag, text.substring(0, LOGCAT_MESSAGE_MAX_LENTH));
                    text = text.substring(LOGCAT_MESSAGE_MAX_LENTH);
                    len = text.length();
                } while (len > LOGCAT_MESSAGE_MAX_LENTH);
                if (len > 0) {
                    Log.w(tag, text);
                }
            }
        }
    }

    @Override
    public void e(Object object) {
        e(mSaveFlag, tag, object.toString());
    }

    /**
     * 公开接口
     * 打印error级别的log
     *
     * @param message
     * @param args
     */
    @Override
    public void e(CharSequence message, Object... args) {
        e(mSaveFlag, tag, message, args);
    }

    /**
     * 私有方法
     * 打印error级别的log
     *
     * @param tag
     * @param objects
     */
    private void e(boolean isSaveLog, String tag, CharSequence msg, Object... objects) {
        String logcatText = buildMessage(msg, objects, LOGPRE_APPVER);
        if (logcatText != null) {
            int len = logcatText.length();
            if (len <= LOGCAT_MESSAGE_MAX_LENTH) {
                Log.e(tag, logcatText);
            } else {
                do {
                    Log.e(tag, logcatText.substring(0, LOGCAT_MESSAGE_MAX_LENTH));
                    logcatText = logcatText.substring(LOGCAT_MESSAGE_MAX_LENTH);
                    len = logcatText.length();
                } while (len > LOGCAT_MESSAGE_MAX_LENTH);
                if (len > 0) {
                    Log.e(tag, logcatText);
                }
            }
        }

        if (isSaveLog) {
            f(tag, msg, objects);
        }
    }

    @Override
    public void close() {

    }

    /**
     * 保存log到文件
     *
     * @param tag
     * @param objects
     */
    private void f(String tag, CharSequence msg, Object... objects) {
        String text = buildMessage(msg, objects, getTimePrefix(), tag + LOGPRE_SPLIT, LOGPRE_APPVER);
        if (text != null) {
            putLogFileBuffer(text);
        }
    }

    private String getTimePrefix() {
        SimpleDateFormat df = new SimpleDateFormat(LOGPRE_TIME_FORMAT, Locale.ENGLISH);
        return df.format(System.currentTimeMillis());
    }

    private String buildMessage(CharSequence msg, Object[] args, String... prefix) {
        if (StringUtil.isEmpty(msg)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try {
            if (prefix != null) {
                for (String p : prefix) {
                    sb.append(p);
                }
            }
            sb.append(msg);
            if (args != null && args.length > 0) {
                for (Object m : args) {
                    sb.append(m != null ? m : "");
                }
            }
        } catch (ConcurrentModificationException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
        return sb.toString();
    }


    /**
     * 向文件中写文本内容
     *
     * @param file
     * @param content
     * @param append
     */
    public void writeFile(File file, String content, boolean append) {
        if (null == file || null == content) {
            return;
        }
        byte[] bytes = null;
        try {
            bytes = content.getBytes(FILECONTENT_ENCODE);
        } catch (UnsupportedEncodingException e) {
            w(TAG, "write file failed: ", e.getMessage());
        }
        if (bytes != null) {
            writeFile(file, bytes, append);
        }
    }

    /**
     * 向文件中写二进制内容
     *
     * @param file
     * @param content
     * @param append
     */
    public void writeFile(File file, byte[] content, boolean append) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, append);
            out.write(content);
            out.flush();
        } catch (IOException e) {
            w(TAG, "write file failed: ", e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private File getLogFile() {
        if (LOGFILE_PATH == null) {
            try {
                String sdcard = Environment.getExternalStorageDirectory().getCanonicalPath();
                LOGFILE_PATH = sdcard + "/" + LOG_FILE_DIR + "/";
            } catch (IOException e) {
                Log.e(TAG, "getExternalStorageDirectory failed!");
            }
        }

        File path = new File(LOGFILE_PATH);
        if (!path.exists()) {
            if (!path.mkdirs()) {
                Log.e(TAG, "create log directory failed!");
                return null;
            }
        }

        File file = new File(LOGFILE_PATH + "log.0");
        if (file.exists() && file.length() > MAX_LOGFILE_LENGTH) {
            File tmp = new File(LOGFILE_PATH + "log." + (MAX_LOGFILE_NUMBER - 1));
            if (tmp.exists()) {
                if (!tmp.delete()) {
                    Log.e(TAG, "delete log file failed");
                    return null;
                }
            }
            for (int i = MAX_LOGFILE_NUMBER - 2; i >= 0; i--) {
                tmp = new File(LOGFILE_PATH + "log." + i);
                if (tmp.exists()) {
                    if (!tmp.renameTo(new File(LOGFILE_PATH + "log." + (i + 1)))) {
                        Log.e(TAG, "rename log file failed");
                        return null;
                    }
                }
            }
        }
        return file;
    }


    private void putLogFileBuffer(String text) {
        synchronized (XLogClient.class) {
            if (logFileBuffer == null) {
                logFileBuffer = new StringBuilder(text);
            } else {
                logFileBuffer.append(text);
            }
            logFileBuffer.append("\n");

            boolean saveNow = logFileBuffer.length() >= LOGFILE_BUFFER_MAX;
            // schedule log file buffer saving
            if (logFileHandler == null) {
                HandlerThread thread = new HandlerThread("logfile_thread");
                thread.start();
                logFileHandler = new LogFileHandler(thread.getLooper(), this);
                logFileHandler.sendEmptyMessageDelayed(MSG_SAVE_LOG_FILE, saveNow ? 0 : LOGFILE_DELAY);
            } else {
                logFileHandler.removeMessages(MSG_LOG_FILE_THREAD_EXIT);
                if (saveNow) {
                    logFileHandler.removeMessages(MSG_SAVE_LOG_FILE);
                    logFileHandler.sendEmptyMessage(MSG_SAVE_LOG_FILE);
                } else if (!logFileHandler.hasMessages(MSG_SAVE_LOG_FILE)) {
                    logFileHandler.sendEmptyMessageDelayed(MSG_SAVE_LOG_FILE, LOGFILE_DELAY);
                } else {
                    logFileHandler.sendEmptyMessage(MSG_SAVE_LOG_FILE);
                }
            }
        }
    }

    private void flushLogFileBufferLocked() {
        synchronized (XLogClient.class) {
            if (logFileBuffer == null) {
                return;
            }

            String text = logFileBuffer.toString();
            logFileBuffer = null;
            File file = getLogFile();
            if (file != null) {
                writeFile(file, text, true);
            } else {
                w(TAG, "get log file failed.");
            }
        }
    }

    private static class LogFileHandler extends Handler {

        private XLogClient outter;

        public LogFileHandler(Looper looper, XLogClient outter) {
            super(looper);
            this.outter = outter;
        }

        @Override
        public void handleMessage(Message msg) {
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case MSG_SAVE_LOG_FILE:
                    synchronized (XLogClient.class) {
                        logFileHandler.removeMessages(MSG_SAVE_LOG_FILE);
                        outter.flushLogFileBufferLocked();
                        logFileHandler.sendEmptyMessageDelayed(MSG_LOG_FILE_THREAD_EXIT, LOGFILE_THREAD_EXIT_DELAY);
                    }
                    break;

                case MSG_LOG_FILE_THREAD_EXIT:
                    synchronized (XLogClient.class) {
                        if (!logFileHandler.hasMessages(MSG_SAVE_LOG_FILE)) {
                            logFileHandler.getLooper().quit();
                            logFileHandler = null;
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }
}

