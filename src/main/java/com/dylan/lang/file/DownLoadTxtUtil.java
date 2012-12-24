package com.dylan.lang.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 下载Txt公用类
 * 
 * @author JackYu
 * 
 */
public class DownLoadTxtUtil {
	// LOG定义
	private static final Logger LOG = LoggerFactory.getLogger(DownLoadTxtUtil.class);
	// 下载文件编码
	private static final String FILE_DOWNLOAD_ENCODING = "GBK";

	/**
	 * 功能：TXT文件下載 (默認CNB下載編碼方式)
	 * 
	 * @param data
	 *            下載字串內容
	 * @param response
	 *            響應對象
	 * @param fileName
	 *            文件名稱
	 * @param 下载文件编码
	 */
	public static void writeTxtToResponse(String data, HttpServletResponse response, String fileName) {
		writeTxtToResponse(data, response, fileName, FILE_DOWNLOAD_ENCODING);
	}

	/**
	 * 功能：下載TXT文檔 可指定下載編碼方式
	 * 
	 * @param data
	 *            下載字串內容
	 * @param response
	 *            響應對象
	 * @param fileName
	 *            文件名稱
	 * @param downloadEncoding
	 *            下載編碼
	 */
	public static void writeTxtToResponse(String data, HttpServletResponse response, String fileName,
			String downloadEncoding) {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.reset();

			response.setContentType("text/html; charset=" + FILE_DOWNLOAD_ENCODING);
			String filename2 = fileName + ".txt";

			// response.setHeader(......)这个设置是下载文件的名称为中文,不乱码
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(filename2.getBytes("gb2312"), "iso8859-1"));
			out.write(data.getBytes(downloadEncoding));
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			LOG.error("response writer error!");
			LOG.error(new String(baos.toByteArray()));
			// close stream
			try {
				baos.close();
				ps.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (out != null)
					out.flush();
			} catch (Exception e) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos);
				e.printStackTrace(ps);
				LOG.error("response  flush error!");
				LOG.error(new String(baos.toByteArray()));
				// close stream
				try {
					baos.close();
					ps.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}