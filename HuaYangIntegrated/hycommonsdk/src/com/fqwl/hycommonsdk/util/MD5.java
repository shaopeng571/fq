package com.fqwl.hycommonsdk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5
{private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
    'A', 'B', 'C', 'D', 'E', 'F' };  
public static String toHexString(byte[] b) {  
//String to  byte  
StringBuilder sb = new StringBuilder(b.length * 2);    
for (int i = 0; i < b.length; i++) {    
    sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);    
    sb.append(HEX_DIGITS[b[i] & 0x0f]);    
}    
return sb.toString();    
}  
public static String get(String s) {  
try {  
    // Create MD5 Hash  
    MessageDigest digest = java.security.MessageDigest.getInstance("MD5");  
    digest.update(s.getBytes());  
    byte messageDigest[] = digest.digest();  
                              
    return toHexString(messageDigest);  
} catch (NoSuchAlgorithmException e) {  
    e.printStackTrace();  
}  
                      
return "";  
}

  public static boolean check(String content, String md5) {
    return get(content).equals(md5);
  }

  public static final String sign(String content, String key)
  {
    return get(content + key);
  }

  public static boolean check(String content, String key, String md5) {
    return get(content + key).equals(md5);
  }
}