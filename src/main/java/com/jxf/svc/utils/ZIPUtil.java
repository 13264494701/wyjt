package com.jxf.svc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件打包方法
 * @author 吴海源
 *
 */
public class ZIPUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ZIPUtil.class);
	/**
	 * 得到指定目录下所有的文件，并组合成名称数�?
	 */
	public  static String[] getFileNames(String baseDirName) {
		File file_dir = new File(baseDirName);
		if (!file_dir.exists()) {
			file_dir.mkdirs();
		}
		File[] files = file_dir.listFiles();
		int file_length = files.length;
		String[] fileNames = new String[file_length];
		for (int i = 0; i < files.length; i++) {
			fileNames[i] = files[i].getName();
		}		
		return fileNames;
	}
	/**
	 * 调用文件打包方法
	 * @param filePath
	 * @param zipFilePath
	 */
	public static boolean createZipFile(String filePath, String zipFilePath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFilePath);
			zos = new ZipOutputStream(fos);
			writeZipFile(new File(filePath), zos, "");
		} catch (FileNotFoundException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return false;
		} finally {
			try {
				if (zos != null)
					zos.close();
			} catch (IOException e) {
				logger.error(Exceptions.getStackTraceAsString(e));
				return false;
			}
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				logger.error(Exceptions.getStackTraceAsString(e));
				return false;
			}
		}
		
		return true;
	}
	/**
	 * 生成压缩文件
	 * 把字节流内容写入压缩包中
	 * @param f
	 * @param zos
	 * @param hiberarchy
	 * wuff 2012-12-13
	 */
	private static void writeZipFile(File f, ZipOutputStream zos, String hiberarchy) {
		if (f.exists()) {
			if (f.isDirectory()) {
				hiberarchy += f.getName() + "/";
				File[] fif = f.listFiles();
				for (int i = 0; i < fif.length; i++) {
					writeZipFile(fif[i], zos, hiberarchy);
				}

			} else {
				FileInputStream fis = null;
				int bytes_read = 0;
				try {
					fis = new FileInputStream(f);
					ZipEntry ze = new ZipEntry(hiberarchy + f.getName());
					zos.putNextEntry(ze);
					byte[] b = new byte[1024];
					while ((bytes_read = fis.read(b)) != -1) {
						zos.write(b,0,bytes_read);
						b = new byte[1024];
					}
					zos.closeEntry();					
				} catch (FileNotFoundException e) {
					logger.error(Exceptions.getStackTraceAsString(e));
				} catch (IOException e) {
					logger.error(Exceptions.getStackTraceAsString(e));
				} finally {
					try {
						if (fis != null)
							fis.close();
					} catch (IOException e) {
						logger.error(Exceptions.getStackTraceAsString(e));
					}
				}

			}
		}

	}

}
