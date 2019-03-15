package com.huayang.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import android.content.Context;
import android.os.Environment;
import cn.kkk.sdk.util.AesUtil;


public class InitDataUtils {
	public final static String INFO_DIR="/Android/data/hy/";
	private final static String KKK_INIT_DATA = "INIT.DAT";
	/**
	 * 保存初始化数据
	 * 
	 * @param context
	 * @param string
	 */
	public static void saveInitData(Context context, String result) {
		if (sdcardAvailable()) {
			String fileName = Environment.getExternalStorageDirectory()
					+ INFO_DIR+ KKK_INIT_DATA;
			boolean flag = writeString(result, fileName);
			if (!flag) {
				FLogger.d("init save data faile");
			}	
		}else {
			FLogger.d("init save data faile no sd");
		}
		
	}
	/**
	 * 保存初始化数据
	 * 
	 * @param context
	 * @param string
	 */
	public static String getInitData(Context context) {
		if (sdcardAvailable()) {
			String fileName = Environment.getExternalStorageDirectory()
					+ INFO_DIR+ KKK_INIT_DATA;
			File file=new File(fileName);
			if (!file.exists()||!file.isFile()) {
				return "";
			}
		return	readString(fileName);
			
//			boolean flag = FileUtil.writeString(result, fileName);
//			if (!flag) {
//				Logger.d("init save data faile");
//			}	
		}else {
			FLogger.d("init save data faile no sd");
			return "";
		}
		
	}
	/**
	 * 从文件中读取数据
	 * 
	 * @param fileName
	 *            文件路径
	 * @return
	 */

	public static String readString(String fileName) {
		String content = null;
		if (sdcardAvailable()) {
			init(fileName);
			try {
				BufferedReader 	reader = new BufferedReader(new FileReader(new File(fileName)));
				StringBuffer buffer = new StringBuffer();
				String len = null;
				while ((len = reader.readLine()) != null) {
					buffer.append(len);
				}
				content = buffer.toString();
				if (content != null && !content.equals("")) {
					// AES解密
					AesUtil aes = new AesUtil();
					content = aes.getDesString(content);
				}
				return content;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}
	/**
	 * 初始化
	 * 
	 * @param fileName
	 *            文件路径
	 * @return
	 */
	private static boolean init(String fileName) {

		try {
			FLogger.d("fileName =" + fileName);
			String rootDir = fileName.substring(0,
					fileName.lastIndexOf("/") + 1);
			File rootFile = new File(rootDir);
			if (!rootFile.exists() || !rootFile.isDirectory()) {
				rootFile.mkdirs();
			}
			File file = new File(fileName);
			if (!file.exists() && !file.isFile()) {
				file.createNewFile();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean writeString(String content, String fileName) {
		if (sdcardAvailable()) {
			init(fileName);
			try {
				FileWriter writer = new FileWriter(new File(fileName), false);
				// AES加密
				AesUtil aes = new AesUtil();
				content = aes.getEncString(content);

				writer.write(content);
				writer.flush();
				writer.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	/**
	 * 判断sdcard是否可用
	 * 
	 * @return
	 */
	private static boolean sdcardAvailable() {
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				FLogger.d( "SDCard无法正常使用...");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
