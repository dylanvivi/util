package com.dylan.lang.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

import org.apache.commons.io.IOUtils;

/**
 * @author dylan
 * @category 读写文件工具类
 */
public class RWFileUtil {
	
	/**
	 * 增量插入
	 * @title appendBinaryToFile
	 * @Description 
	 * @param binary
	 * @param path
	 * @param fileName
	 * @date 2013-7-18
	 */
	public static void appendBinaryToFile(byte[] binary, String path, String fileName) {
		FileOutputStream fos = null;
		try {
			File localFolder = new File(path);
			if (!localFolder.exists()) {
				localFolder.mkdirs();
			}
			File file = new File(path + File.separator + fileName);
			fos = new FileOutputStream(file, true);
			fos.write(binary);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	public static void writeBinaryToFile(byte[] binary, String path, String fileName) {
		FileOutputStream fos = null;
		try {
			File localFolder = new File(path);
			if (!localFolder.exists()) {
				localFolder.mkdirs();
			}
			File file = new File(path + File.separator + fileName);
			fos = new FileOutputStream(file);
			fos.write(binary);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	public static void writeStringToFile(String data, String path, String fileName, boolean isContinue) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			File localFolder = new File(path);
			if (!localFolder.exists()) {
				localFolder.mkdirs();
			}
			File file = new File(path + File.separator + fileName);
			fw = new FileWriter(file, isContinue);
			bw = new BufferedWriter(fw);
			bw.write(data);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fw);
			IOUtils.closeQuietly(bw);
		}
	}

	public static String readTxtFile(String file) {
		StringBuilder sb = new StringBuilder();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				if (!"\n".equalsIgnoreCase(line)) {
					sb.append(line + ",");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fr);
			IOUtils.closeQuietly(br);
		}
		return sb.toString();
	}

	public static String readFile(String file) {
		StringBuilder sb = new StringBuilder();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "utf-8");
			br = new BufferedReader(isr);
			String line = "";
			while ((line = br.readLine()) != null) {
				if (!"\n".equalsIgnoreCase(line)) {
					sb.append(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(br);
		}
		return sb.toString();
	}

	public static void deleteFiles(String path, String excludeSuffix) {
		File dir = new File(path);
		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (excludeSuffix != null && file.getName().contains(excludeSuffix)) {
					continue;
				}
				file.delete();
			}
		}
	}

	public static void copyFiles(String sourcePath, String targetPath) throws IOException {
		File srcDir = new File(sourcePath);
		File[] files = srcDir.listFiles();
		FileChannel in = null;
		FileChannel out = null;
		for (File file : files) {
			try {
				in = new FileInputStream(file).getChannel();
				File outDir = new File(targetPath);
				if (!outDir.exists()) {
					outDir.mkdirs();
				}
				File outFile = new File(targetPath + File.separator + file.getName());
				out = new FileOutputStream(outFile).getChannel();
				in.transferTo(0, in.size(), out);
			} finally {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			}
		}
	}

	public static int getFileBytesCount(String file) {
		int fileByteCount = 0;
		File f = new File(file);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			fileByteCount = fis.available();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileByteCount;
	}

	public static void main(String[] args) {
		System.out.println(readFile("d:/json.txt"));
	}
}
