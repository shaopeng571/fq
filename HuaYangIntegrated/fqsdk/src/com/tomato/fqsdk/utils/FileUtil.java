package com.tomato.fqsdk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import android.content.Context;
import android.os.Environment;

public class FileUtil {
	// public static String path = SPConfig.DEFAULT_SDCARD +
	// File.separator+"Games";
	public static void makeFiles(String path) {
		makeFile(path);
		makeFile(path + "/GBA");
		makeFile(path + "/GBC");
		makeFile(path + "/SFC");
		makeFile(path + "/FC");
		makeFile(path + "/MD");
		makeFile(path + "/PS");
		makeFile(path + "/MAME");
		makeFile(path + "/MAME/roms");
		makeFile(path + "/NDS");
		makeFile(path + "/ARCADE");
		makeFile(path + "/N64");
		makeFile(path + "/WSC");
		makeFile(path + "/PSP");
		makeFile(path + "/ONS");
		makeFile(path + "/ANDROID");
	}

	public static void makeFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	private static void delFolder(String folderPath) {
	     try {
	        delAllFile(folderPath); //ɾ                
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        java.io.File myFilePath = new java.io.File(filePath);
	        myFilePath.delete(); //ɾ     ļ   
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}

	//ɾ  ָ   ļ          ļ 
	//param path  ļ           ·  
	   public static boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	    	   file.delete();
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//  ɾ   ļ         ļ 
	             delFolder(path + "/" + tempList[i]);//  ɾ     ļ   
	             flag = true;
	          }
	       }
	       file.delete();
	       return flag;
	     }
	   public static boolean delONSFile(String path) {
		   boolean flag = false;
		   File file = new File(path);
		   if (!file.exists()) {
			   return flag;
		   }
		   if (!file.isDirectory()&&!file.getName().endsWith(".dat")&&!file.getName().endsWith(".png")) {
			   file.delete();
			   return flag;
		   }
		   String[] tempList = file.list();
		   File temp = null;
		   for (int i = 0; i < tempList.length; i++) {
			   if (path.endsWith(File.separator)) {
				   temp = new File(path + tempList[i]);
			   } else {
				   temp = new File(path + File.separator + tempList[i]);
			   }
			   if (temp.isFile()&&!temp.getName().endsWith(".dat")&&!temp.getName().endsWith(".png")) {
				   temp.delete();
			   }
			   if (temp.isDirectory()) {
				   delONSFile(path + "/" + tempList[i]);//  ɾ   ļ         ļ 
				   delFolder(path + "/" + tempList[i]);//  ɾ     ļ   
				   flag = true;
			   }
		   }
//		   file.delete();
		   return flag;
	   }

	/*
	 * public static List getRecentApps(Context context) { boolean isLocal =
	 * true; List list = new ArrayList<App>(); InfoSource infoSource =
	 * (InfoSource) AppStore.instance(context) .infoSource(); List<App>
	 * localList = infoSource.getRecentApps(); List<Node> netList = new
	 * ArrayList<Node>(); if (localList.size() == 0) { isLocal = false; try {
	 * netList = infoSource.retrieveRemoteCategoryChildren(
	 * InfoSource.CATEGORY_ID_RECOMMEND, 0, 4); } catch (InfoSourceException e)
	 * { e.printStackTrace(); } } list.add(isLocal); list.addAll(localList);
	 * list.addAll(netList); return list; }
	 */
	   /**
	   * ·   Ƿ    
	   * @param path  ·  
	   * @Return    Ƿ 
	   */
	   public static boolean isFileExit(String path) {
	   if(path == null) {
	   return false;
	   } else {
	   try {
	   File f = new File(path);
	   if(f.exists()) {
	   return true;
	   }
	   } catch (Exception e) {
	   e.printStackTrace();
	   }

	   return false;
	   }
	   }
//    Ŀ¼ е  ļ ȫ     Ƶ   Ŀ¼
public static void copyFilesFassets(Context context, String oldPath, String newPath) {
    try {

        //   ȡassetsĿ¼ µ      ļ   Ŀ¼  
        String fileNames[] = context.getAssets().list(oldPath);

        //      Ŀ¼       ظ    ÷    ݹ ؽ      ļ 
        if (fileNames.length > 0) {
            File file = new File(newPath);
            file.mkdirs();
            for (String fileName : fileNames) {
                copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
            }
        }
        //       ļ     ѭ            ȡ ֽ д  
        else {
            InputStream is = context.getAssets().open(oldPath);
            FileOutputStream fos = new FileOutputStream(new File(newPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
        }
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * 
	 * @param fromFile
	 *                 Ƶ  ļ 
	 * @param toFile
	 *               Ƶ Ŀ¼ ļ 
	 * @param rewrite
	 *             Ƿ    ´    ļ 
	 * 
	 *            <p>
	 *             ļ  ĸ  Ʋ       
	 */
	public static void copyfile(File fromFile, File toFile, Boolean rewrite) {

		if (!fromFile.exists()) {
			return;
		}

		if (!fromFile.isFile()) {
			return;
		}
		if (!fromFile.canRead()) {
			return;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}

		// if (toFile.exists()) {
		// return;
		// }
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}

		try {
			FileInputStream fosfrom = new FileInputStream(fromFile);
			FileOutputStream fosto = new FileOutputStream(toFile);

			byte[] bt = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			//  ر    롢     
			fosfrom.close();
			fosto.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    public static void writeFileToSD(String pathName,String fileName,String content) {
    	String sdStatus = Environment.getExternalStorageState();
    	if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
    		return;
    	}
    	try {
    		File path = new File(pathName);
    		File file = new File(pathName + fileName);
    		if( !path.exists()) {
    			path.mkdir();
    		}
    		if( !file.exists()) {
    			file.createNewFile();
    		}
    		FileOutputStream stream = new FileOutputStream(file);
    		byte[] buf = content.getBytes();
    		stream.write(buf);    
    		stream.write("\r\n".getBytes());
    		stream.close();
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
//  // ύ        
//   public static void PostToServer(String tag, String sb){
//
//    	final String sbbString = sb;
//    	final String tagString = tag;
//    	new Thread(new Runnable() {	
//    		@Override
//    		public void run() {
//    			try {
//    				List<BasicNameValuePair> postData=new ArrayList<BasicNameValuePair>();
//    				postData.add(new BasicNameValuePair("name", tagString + "-"+android.os.Build.MANUFACTURER+"-"+android.os.Build.MODEL+"-"+android.os.Build.VERSION.SDK+".txt"));
//    				postData.add(new BasicNameValuePair("data", sbbString));
//    				Post("http://update.vgabc.com/index.php?a=post", postData, 10, 10);
//    				//LogUtil.d("upload",Post("http://update.vgabc.com/index.php?a=post", postData, 10, 10));
//    			} catch (Exception e) {
//    				e.printStackTrace();
//    				//LogUtil.d("upload", "Exception", e);
//    			}
//    			
//    		}
//    	}).start();
//    	
//    }
//   public static  String Post(String URL, List<BasicNameValuePair> params, int ConnectionTimeout, int SoTimeout) {  
//            HttpPost httpPost = new HttpPost(URL); 
//            String returnString = null;  
//            HttpParams httpParameters = new BasicHttpParams();  
//            try {
//            	if(params!=null){
//            		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(  
//                        params, "utf-8");  
//                	httpPost.setEntity(urlEncodedFormEntity);
//                	for(BasicNameValuePair param : params){
//                		Log.e(">>>"+ param.getName() , param.getValue());
//                	}
//            	}
//                HttpConnectionParams.setConnectionTimeout(httpParameters, 1000*ConnectionTimeout);  
//                HttpConnectionParams.setSoTimeout(httpParameters, 1000*SoTimeout);
//                HttpClient httpClient = new DefaultHttpClient(httpParameters);  
//                HttpResponse httpResponse = httpClient.execute(httpPost);  
//                HttpEntity httpEntity = httpResponse.getEntity();  
//                InputStream inputStream = httpEntity.getContent();  
//                BufferedInputStream bufferedInputStream = new BufferedInputStream(  
//                        inputStream);  
//                ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(50);  
//                int current = 0;  
//                while ((current = bufferedInputStream.read()) != -1) {  
//                    byteArrayBuffer.append(current);  
//                }  
//                returnString = EncodingUtils.getString(byteArrayBuffer  
//                        .toByteArray(), "utf-8");  
//            } catch (UnsupportedEncodingException e) {  
//                e.printStackTrace();  
//            } catch (ClientProtocolException e) {  
//                e.printStackTrace();  
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            }  
//            return returnString;  
//    }
}
