package com.vcode.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * @author yaorui
 */
public class TestCaseHandler {

  public static boolean isZipExist(String filePath) {
    File file = new File(filePath);
    return file.exists();
  }

  /**
   * @param fileFullPath  zipfile's full path
   * @param filename      not include .zip
   * @param unzipFilePath target dir
   * @return String: error message
   * @Description unzip to unzipFilePath
   * @Date 2020/2/24 17:43
   */
  public static String processZipFile(String fileFullPath, String filename, String unzipFilePath) {

    if (!unzipFilePath.endsWith(File.separator)) {
      unzipFilePath += File.separator;
    }
    // target dir
    String unzipTargetPath = unzipFilePath + filename;
    File dir = new File(unzipTargetPath);
    // create dir
    if (!dir.mkdirs()) {
      return "mkdirs fail";
    }
    // get file
    try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
            new FileInputStream(fileFullPath)))) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        extractEntryContent(zis, entry, unzipTargetPath);
      }
    } catch (IOException e) {
      return e.toString();
    }
    // write info data
    if (dir.list() == null || Objects.requireNonNull(dir.list()).length == 0) {
      return "dir is blank";
    }
    File[] infileList = dir.listFiles((directory, name) -> name.endsWith(".in"));
    if (infileList == null) {
      return "there are no in files";
    }
    List<String> filenameList = Arrays.asList(Objects.requireNonNull(dir.list()));
    // generate info data
    HashMap<String, Object> info = new HashMap<>(1);
    Vector<HashMap<String, String>> data = new Vector<>();
    for (File file : infileList) {
      HashMap<String, String> infoItem = new HashMap<>(2);
      String infileFullName = file.getName();
      String itemFilename = infileFullName.substring(0, infileFullName.lastIndexOf("."));
      String outfileFullName = itemFilename + ".out";
      if (filenameList.contains(outfileFullName)) {
        infoItem.put("input_name", infileFullName);
        infoItem.put("out_name", outfileFullName);
        data.add(infoItem);
      }
    }
    info.put("info", data);
    ObjectMapper mapper = new ObjectMapper();
    try {
      PrintWriter pw = new PrintWriter(new FileWriter(unzipTargetPath + "/info"));
      pw.println(mapper.writeValueAsString(info));
      pw.close();
    } catch (IOException e) {
      return e.toString();
    }
    return null;
  }

  public static void extractEntryContent(ZipInputStream zis, ZipEntry entry,
                                         String unzipDir) throws IOException {

    String entryFileName = entry.getName();
    String entryPath = unzipDir + File.separator + entryFileName;
    createFile(entryPath);
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
            entryPath));
    byte[] buffer = new byte[1024];
    int count;
    while ((count = zis.read(buffer)) != -1) {
      bos.write(buffer, 0, count);
    }
    bos.close();
  }

  public static void createFile(String filePath) throws IOException {
    File file = new File(filePath);
    File parent = file.getParentFile();

    if (!parent.exists()) {
      parent.mkdirs();
    }
    file.createNewFile();
  }

  public static void moveTestCaseFiles(String testCasePath, String dirName) throws IOException {
    Path sourcePath = Paths.get("/tmp/" + dirName);
    if (!testCasePath.endsWith(File.separator)) {
      testCasePath += File.separator;
    }
    File folder = new File(testCasePath);
    if (!folder.exists() && !folder.isDirectory()) {
      folder.mkdirs();
    }
    Path targetPath = Paths.get(testCasePath + dirName);
    FileUtils.moveDirectory(sourcePath.toFile(), targetPath.toFile());
  }

  public static void removeTestCaseFiles(String testCasePath, String dirName) throws IOException {
    if (!testCasePath.endsWith(File.separator)) {
      testCasePath += File.separator;
    }
    File dir = new File(testCasePath + dirName);
    FileUtils.deleteDirectory(dir);
  }
}
