package com.lz.base.log;

import android.text.TextUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Log文件管理，文件的数量，大小，增，删，写
 */
public class LogFilesUtils {
    /**
     * 默认 log文件数量10
     */
    private static final int DEFAULT_LOG_FILES_MAX_NUM = 10;
    /**
     * 默认 log文件大小10MB
     */
    private static final int DEFAULT_LOG_FILE_MAX_SIZE = 1024 * 1024 * 100;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private int mFilesMaxNum = -1;
    private int mFileMaxSize = -1; //-1不限制

    private File mCurrentLogFile;
    private String mLogFileDir;
    private String mAppendFileName = "";

    LogFilesUtils(String logFileDir) {
        mLogFileDir = logFileDir;
        mFilesMaxNum = DEFAULT_LOG_FILES_MAX_NUM;
        mFileMaxSize = DEFAULT_LOG_FILE_MAX_SIZE;
    }

    /**
     * @param logFileDir     log文件保存路径
     * @param appendFileName 文件名附加SN/UserID TIME+appendFileName
     * @param logFileCount   log文件数量
     * @param logFileSize    log文件大小MB ，-1不限制大小
     */
    public LogFilesUtils(String logFileDir, String appendFileName, int logFileCount, int logFileSize) {
        mLogFileDir = logFileDir;
        mAppendFileName = appendFileName;
        mFilesMaxNum = logFileCount > 0 ? logFileCount : 1;
        mFileMaxSize = logFileSize;
    }

    /**
     * @param logFileDir  log文件保存路径
     * @param fileMaxNum  log文件数量
     * @param fileMaxSize log文件大小MB ，-1不限制大小
     */
    LogFilesUtils(String logFileDir, int fileMaxNum, int fileMaxSize) {
        mLogFileDir = logFileDir;
        mFilesMaxNum = fileMaxNum;
        mFileMaxSize = fileMaxSize;
    }


    /**
     * log 写入文件
     */
    public void writeLogToFile(String logMessage) {
        if (mCurrentLogFile == null || (mCurrentLogFile.length() >= mFileMaxSize && mFileMaxSize != -1)) {
            mCurrentLogFile = getNewLogFile();
        }
        writeToFile(logMessage, mCurrentLogFile.getPath());
    }

    /**
     * log 写入相应文件
     */
    private void writeToFile(String content, String filePath) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, true);
            fileWriter.write(content);
            fileWriter.flush();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取log文件，log路径下没有创建，log文件达到指定数量删除最早的，否则获取没有写满的文件
     **/
    private File getNewLogFile() {
        File dir = new File(mLogFileDir);
        File[] files = dir.listFiles(fileFilter);
        if (files == null || files.length == 0) {
            // 创建新文件
            return createNewLogFile();
        }
        List<File> sortedFiles = sortFiles(files);
        if (files.length > mFilesMaxNum) {
            // 删掉最早的文件
            delete(sortedFiles.get(0));
        }
        // 取最新的文件，是否写满
        File lastLogFile = sortedFiles.get(sortedFiles.size() - 1);
        if (lastLogFile.length() < mFileMaxSize || mFileMaxSize == -1) {
            return lastLogFile;
        } else {
            // 创建新文件
            return createNewLogFile();
        }
    }

    /**
     * 创建log文件
     */
    private File createNewLogFile() {
        return createFile(mLogFileDir + File.separator + DATE_FORMAT.format(new Date(System.currentTimeMillis())) +
                mAppendFileName + ".txt");
    }

    private List<File> sortFiles(File[] files) {
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new FileComparator());
        return fileList;
    }

    private class FileComparator implements Comparator<File> {
        public int compare(File file1, File file2) {
            if (file1.lastModified() < file2.lastModified()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private FileFilter fileFilter = file -> {
        String tmp = file.getName().toLowerCase();
        if (tmp.endsWith(".log")) {
            return true;
        }
        return false;
    };

    /**
     * 删除文件或目录
     *
     * @param path 文件或目录。
     * @return true 表示删除成功，否则为失败
     */
    synchronized public boolean delete(File path) {
        if (null == path) {
            return true;
        }

        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (!delete(file)) {
                        return false;
                    }
                }
            }
        }
        return !path.exists() || path.delete();
    }

    /**
     * 创建文件， 如果不存在则创建，否则返回原文件的File对象
     *
     * @param path 文件路径
     * @return 创建好的文件对象, 返回为空表示失败
     */
    synchronized private File createFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File file = new File(path);
        if (file.isFile()) {
            return file;
        }

        File parentFile = file.getParentFile();
        if (parentFile != null && (parentFile.isDirectory() || parentFile.mkdirs())) {
            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 上传航线
     *
     * @param strUrl
     * @param logFile
     * @return
     */
    public static boolean uploadLogFile(String strUrl, File logFile) {
        //mCurrentLogFile.getAbsolutePath()
//        ServerConstant.NEW_LOG_URL

        URL uploadurl = null;
        try {
            uploadurl = new URL(strUrl);

            HttpURLConnection http = (HttpURLConnection) uploadurl.openConnection();
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestMethod("PUT");
            http.setConnectTimeout(20 * 1000);
            http.setReadTimeout(20 * 1000);
            http.setRequestProperty("Connection", "Keep-Alive");
            http.setRequestProperty("Content-Type", "text/plain");
            //http.setRequestProperty("Host", "thunderfast2.oss-cn-beijing.aliyuncs.com");
            Map<String, List<String>> requestHeaderMap = http.getRequestProperties();
            http.connect();
            long logfileLength = logFile.length();
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(logFile));
//            FileInputStream fileInputStream =  new FileInputStream(new File(LogUtils.LOG_ROOT_PATHE + "12.log"));
            OutputStream outputStream = http.getOutputStream();
//            DataOutputStream dataOutputStream  =  new DataOutputStream(http.getOutputStream());
//            OutputStreamWriter outputStreamWriter  =  new OutputStreamWriter(http.getOutputStream());
            byte[] readBuffer = new byte[10 * 1024];
            int offset = 0;
            int Length = 0;
            while ((offset = dataInputStream.read(readBuffer)) != -1) {
                outputStream.write(readBuffer, 0, offset);
                //String str = readBuffer.toString();
                Length += offset;
            }
            dataInputStream.close();
            outputStream.close();
            int responseCode = http.getResponseCode();
            //fileInputStream.close();
            //dataOutputStream.flush();
            if (http != null) {
                http.disconnect();
            }
            if (responseCode != 200) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean uploadLogFile(String strUrl) {
        return uploadLogFile(strUrl, mCurrentLogFile);
    }

    public File getCurrentLogFile() {
        return mCurrentLogFile;
    }
}
