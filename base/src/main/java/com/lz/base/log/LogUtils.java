package com.lz.base.log;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LOG 工具类，记录app操作log、飞行航线log，写入文件
 */
public class LogUtils {

    public final static String LOG_ROOT_PATHE = Environment.getExternalStorageDirectory()
            + File.separator + "liuzhao" + File.separator + "log"
            + File.separator;
    public final static String APP_LOG_PATHE = "app" + File.separator;

    public enum LogLevel {
        VERBOSE(Log.VERBOSE),
        DEBUG(Log.DEBUG),
        INFO(Log.INFO),
        WARN(Log.WARN),
        ERROR(Log.ERROR),
        ASSERT(Log.ASSERT);
        private int mValue;
        LogLevel(int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat LOG_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static ExecutorService sLogExecutor = Executors.newSingleThreadExecutor();

    private static boolean sLogEnable = true;
    private static LogLevel sLogLevel = LogLevel.DEBUG;
    private static LogFilesUtils sLogFilesApp;

    public static String LOG_LIUZHAO= "LIU_ZHAO";
    /**
     * 设置Log开关
     *
     * @param enable 开关
     */
    public static void setEnable(boolean enable) {
        sLogEnable = enable;
    }


    /**
     * 设置Log级别
     *
     * @param level 枚举VERBOSE,DEBUG,INFO..
     */
    public static void setLogLevel(LogLevel level) {
        sLogLevel = level;
    }

    public static void setAppLogDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        sLogFilesApp = new LogFilesUtils(dirPath);
    }

    /**
     * 设置APP写入log的文件夹
     *
     * @param dirPath 文件夹地址
     * @param appendFileName 文件名附加SN/UserID TIME+appendFileName
     * @param logFileCount log文件数量
     * @param logFileSize 单个log文件大小MB，-1不限制
     */
    public static void setAppLogDir(String dirPath,String appendFileName,int logFileCount,int logFileSize) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        sLogFilesApp = new LogFilesUtils(dirPath,appendFileName,logFileCount,logFileSize);
    }


    /**
     * app log for debug
     *
     * @param message log message
     * @see Log#d(String, String)
     */
    public static void d(String message) {
        if (sLogEnable) {
            Log.d(LOG_LIUZHAO, message);
            writeToFileIfNeeded(message, LogLevel.DEBUG,sLogFilesApp);
        }
    }

    /**
     * app log for warning
     *
     * @param msg log message
     * @see Log#w(String, String)
     */
    public static void w(String msg) {
        if (sLogEnable) {
            Log.w(LOG_LIUZHAO, msg);
            writeToFileIfNeeded(msg, LogLevel.WARN, sLogFilesApp);
        }
    }

    /**
     * app log for error
     *
     * @param msg message
     * @see Log#i(String, String)
     */
    public static void e(String msg) {
        if (sLogEnable) {
            Log.e(LOG_LIUZHAO, msg);
            writeToFileIfNeeded(msg, LogLevel.ERROR, sLogFilesApp);
        }
    }

    /**
     * App log for information
     *
     * @param msg message
     * @see Log#i(String, String)
     */
    public static void i( String msg) {
        if (sLogEnable) {
            Log.i(LOG_LIUZHAO, msg);
            writeToFileIfNeeded(msg, LogLevel.INFO, sLogFilesApp);
        }
    }
    /**
     * APP log for verbos
     *
     * @param msg log message
     * @see Log#v(String, String)
     */
    public static void v( String msg) {
        if (sLogEnable) {
            Log.v(LOG_LIUZHAO, msg);
            writeToFileIfNeeded( msg, LogLevel.VERBOSE,sLogFilesApp);
        }
    }


    private static void writeToFileIfNeeded(final String msg, LogLevel logLevel, final LogFilesUtils logFilesUtils) {
        if (logLevel.getValue() < sLogLevel.getValue() || logFilesUtils == null) {
            return;
        }
        sLogExecutor.execute(() -> {
            String logMsg = formatLog( msg);
            logFilesUtils.writeLogToFile(logMsg);
        });
    }

    private static String formatLog( String msg) {
        return String.format(Locale.getDefault(),"%s pid=%d %s: %s\n", LOG_DATE_TIME_FORMAT.format(new Date(
                System.currentTimeMillis())), android.os.Process.myPid(), LOG_LIUZHAO, msg);
    }

}