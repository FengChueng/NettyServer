package com.zyl.netty.server.utils;

public class NumberUtils {
	public static String bytesToHexString(byte[] src){   
	    StringBuilder stringBuilder = new StringBuilder("");   
	    if (src == null || src.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < src.length; i++) {   
	        int v = src[i] & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv);   
	    }   
	    return stringBuilder.toString();   
	}  
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }  
	    
	    if (hexString.length() % 2 != 0) {
			hexString = "0" + hexString;
		}
	    
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	 } 
	/**  
	 * Convert char to byte  
	 * @param c char  
	 * @return byte  
	 */  
	 private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	 } 
	 
	 
	 /**
	   * 将一个单字节的Byte转换成十六进制的数
	   * 
	   * @param b	byte
	   * @return convert result
	   */
	public static String byteToHex(byte b) {
		int i = b & 0xFF;
		String hexString = Integer.toHexString(i).toUpperCase();
		if (hexString.length() == 1) {
			hexString = "0" + hexString;
		}
		
		return hexString;
	}
	
	/**
	 * int(<=255) -> string(16进制) -> byte
	 * @param f
	 * @return
	 */
	public static byte hexint2byte(int f){
		byte [] byteV = hexStringToBytes(Integer.toHexString(f));
		return byteV[0];
	}
	
	public static void main(String[] args) {
		System.out.println(bytesToHexString(new byte[]{hexint2byte(10)}));
		System.out.println(byteToHex((byte)0xaa));
		System.out.println(Integer.toHexString((byte)0xa0));
	}
}
