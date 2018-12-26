package com.lz.base.log;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {

    public final static String FILE_EXTENSION_SEPARATOR = ".";

    private FileUtil() {
        throw new AssertionError();
    }


    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (!file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append   is append, if true, write to the end of file, else clear
     *                 content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StringUtil.isEmpty(content)) {
            return false;
        }
        if (StringUtil.isEmpty(filePath)) {// 文件夹内容为空
            return false;
        }
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    // public static boolean writeWifiStateFileWithTime(String content) {
    // try {
    // return writeFileWithTime(MySpUtils.getWifiStateLogFilePath(),
    // content);
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return false;
    // }

    // public static boolean writeMessageFileWithTime(String content) {
    // try {
    // return writeFileWithTime("", content);
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return false;
    // }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream   the input stream
     * @param append   if <code>true</code>, then bytes will be written to the end of
     *                 the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of
     *               the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            if (file != null) {
                makeDirs(file.getAbsolutePath());
                o = new FileOutputStream(file, append);
                byte data[] = new byte[1024];
                int length = -1;
                while ((length = stream.read(data)) != -1) {
                    o.write(data, 0, length);
                }
                o.flush();
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * move file
     *
     * @param sourceFilePath
     * @param destFilePath
     */
    public static void moveFile(String sourceFilePath, String destFilePath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }

    /**
     * move file
     *
     * @param srcFile
     * @param destFile
     */
    public static void moveFile(File srcFile, File destFile) {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

    /**
     * copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset
     *                    </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * get file name from path, not include suffix
     * <p>
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     * <p>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     * <p>
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (StringUtil.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * get suffix of file from path
     * <p>
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Creates the directory named by the trailing filename of this file,
     * including the complete directory path required to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath
     * @return
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtil.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file
     * system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtil.isBlank(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (StringUtil.isBlank(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     *
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file
     * does not exist.
     */
    public static long getFileSize(String path) {
        if (StringUtil.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 创建文件夹
     *
     * @param folderName
     * @return
     */
    public static boolean createFolder(String folderName) {
        if (TextUtils.isEmpty(folderName)) return false;
        File localFile = new File(folderName);
        if (!localFile.isDirectory()) {
            localFile = new File(getFolderName(localFile));
        }
        if ((localFile.exists()) && (localFile.isDirectory())) {
            return true;
        }
        return localFile.mkdirs();
    }

    /**
     * 创建文件
     *
     * @return
     */
    public static boolean createFile(String fileName) {
        return createFile(fileName, true);
    }

    /**
     * 创建文件
     *
     * @return
     */
    public static boolean createFile(String fileName, boolean isDelete) {
        boolean result = false;
        File file = new File(fileName);
        if (file.exists()) {
            if (isDelete) {
                file.delete();
            } else {
                return true;
            }
        }
        createFolder(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 创建文件
     *
     * @return
     */
    public static File createFile1(String fileName) {
        return createFile1(fileName, true);
    }


    /**
     * 获取文件指定位置字节
     * @param file
     * @param pos
     * @param len
     * @return
     * @throws IOException
     */
    public static byte[] readsss(File file,int pos,int len) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        stream.skip(pos); //跳过之前的字节数
        byte[] b = new byte[len];
        stream.read(b);
        System.out.print(new String(b));
        stream.close();
        return b;
    }
    /**
     * 创建文件
     *
     * @return
     */
    public static File createFile1(String fileName, boolean isDelete) {
        File file = new File(fileName);
        if (file.exists()) {
            if (isDelete) {
                file.delete();
            }
        }
        createFolder(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return file;
        }
    }

    /**
     * 创建文件
     *
     * @return
     */
    public static boolean createFile(File file, boolean isDelete) {
        boolean result = false;
        String fileName = file.getAbsolutePath();
        if (file.exists()) {
            if (isDelete) {
                file.delete();
            } else {
                return true;
            }
        }
        createFolder(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 创建文件
     *
     * @return
     */
    public static boolean createFile(File file) {
        return createFile(file, true);
    }

    /**
     * 删除指定文件或者文件夹，需要权限：<uses-permission
     * android:name="android.permission.WRITE_EXTERNAL_STORAGE"
     * ></uses-permission>
     *
     * @param file 文件/文件夹全路径
     * @return
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            // 文件不存在，返回true
            return true;
        } else {
            // 文件存在
            if (file.isFile()) {
                String path = file.getAbsolutePath();
                boolean deleted = file.delete();
                return deleted;
            } else if (file.isDirectory()) {
                // 文件夹
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    return file.delete();
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                String path = file.getAbsolutePath();
                boolean deleted = file.delete();
                return deleted;
                // return true;
            }
        }
        return false;
    }

    /**
     * 获取文件扩展名
     *
     * @param file 文件全路径
     * @return
     */
    public static String getFileExtension(File file) {
        String result = null;
        String filePath = file.getAbsolutePath();
        int lastIndexOfFileSeparator = filePath.lastIndexOf(".");
        if (lastIndexOfFileSeparator > -1) {
            result = filePath.substring(lastIndexOfFileSeparator + 1);
        }
        return result;
    }

    /**
     * 获取文件名
     *
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        String result = null;
        result = file.getName();
        return result;
    }

    /**
     * 获取文件名（不包括后缀名）
     *
     * @param file
     * @return
     */
    public static String getFileNameWithoutExtension(File file) {
        String result = null;
        if (!file.exists()) {
            // 文件不存在
        } else {
            String fileName = getFileName(file);
            int lastIndexOfFileSeparator = fileName.lastIndexOf(".");
            if (lastIndexOfFileSeparator > -1) {
                result = fileName.substring(0, lastIndexOfFileSeparator);
            }
        }
        return result;
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long result = -1;
        if (!file.exists()) {
            // 文件不存在
        } else {
            long bytes = file.getTotalSpace();
            result = bytes / (8 * 1024 * 1024);
        }
        return result;
    }

    /**
     * 获取foldername
     *
     * @param file
     * @return
     */
    public static String getFolderName(File file) {
        String result = null;
        String fileName = file.getAbsolutePath();
        int lastIndexOf = fileName.lastIndexOf(File.separatorChar);
        result = fileName.substring(0, lastIndexOf);
        return result;
    }

    /**
     * 文件是否存在
     *
     * @param file
     * @return
     */
    public static boolean isFileExist(File file) {
        return file.exists();
    }

    /**
     * 文件夹是否存在
     *
     * @return
     */
    public static boolean isFolderExist(File file) {
        return file.exists();
    }

    /**
     * 创建文件夹
     *
     * @param folder 文件夹
     * @return
     */
    public static boolean createFolder(File folder) {
        String folderName = folder.getAbsolutePath();
        return createFolder(folderName);
    }

    /**
     * 将字符串写入到文本文件中
     *
     * @param strcontent
     * @param strFilePath
     */
    public static void writeToTxtFile(String strcontent, String strFilePath, boolean isWriteToBegin) {
        // 每次写入时，都换行写
        String strContent = strcontent + "\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                createFile(strFilePath, false);
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (isWriteToBegin) {
                raf.seek(0);
            } else {
                raf.seek(file.length());
            }
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符串写入到文本文件中
     *
     * @param strcontent
     */
    public static void writeToTxtFile(String strcontent, File file, boolean isWriteToBegin) {
        // 每次写入时，都换行写
        String strContent = strcontent + "\n";
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (isWriteToBegin) {
                raf.seek(0);
            } else {
                raf.seek(file.length());
            }
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符串写入到文本文件中
     */
    public static void writeToTxtFileWithMappedByteBuffer(String content, File file, boolean
            isWriteToBegin) {
        // 每次写入时，都换行写
        String strContent = content + "\n";
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            if (isWriteToBegin) {
                raf.seek(0);
            } else {
                raf.seek(file.length());
            }
            FileChannel fc = raf.getChannel();
            int length = 8 * 1024 * 4;
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, length);
            for (int i = 0; i < strContent.getBytes().length; i++) {
                mbb.put(strContent.subSequence(i, length).toString().getBytes(), 0, length);
                i += length;
            }
            fc.close();
            if (raf != null) {
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath(Context context) {
        if (FileUtil.sdCardIsAvailable()) {
            return Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            return context.getFilesDir();
        }
    }

    /**
     * SD卡是否可用.
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }


}
