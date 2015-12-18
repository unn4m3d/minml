package net.launcher.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypt{
	public static String encrypt(String input, String key){
		  byte[] crypted = null;
		  try{
		    SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
		      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		      cipher.init(Cipher.ENCRYPT_MODE, skey);
		      crypted = cipher.doFinal(input.getBytes());
		    }catch(Exception e){
		    	System.err.println("Ключ должен быть 16 символов");
		    }
		    return new String(new sun.misc.BASE64Encoder().encode(crypted));
		}

	public static String decrypt(String input, String key){
		byte[] output = null;
		try{
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
		    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		    cipher.init(Cipher.DECRYPT_MODE, skey);
		    output = cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(input));
		}catch(Exception e){
		    System.err.println("Ключ шифрование не совпадает или больше 16 символов, или полученна ошибка от launcher.php");
		    System.err.println("Проверьте настройку  в Settings.java или connect.php");
		}
		return new String(output);
	} 
	
	public static String xorencode(String text, String key)
	{
		String res = ""; int j = 0;
		for (int i = 0; i < text.length(); i++)
		{
			res += (char)(text.charAt(i) ^ key.charAt(j));
			j++; if(j==key.length()) j = 0;
		}
		return res;
	}
	
	public static String strtoint(String text)
	{
		String res = "";
		for (int i = 0; i < text.length(); i++) res += (int)text.charAt(i) + "-";
		res = res.substring(0, res.length() - 1);
		return res;
	}
	
	public static String inttostr(String text)
	{
		String res = "";
		for(int i = 0; i < text.split("-").length; i++) res += (char)Integer.parseInt(text.split("-")[i]);
		return res;
	}
}
