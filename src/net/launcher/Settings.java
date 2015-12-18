package net.launcher;

import java.io.File;

import net.launcher.components.ServerInfo;

public class Settings {
	public final static String APPDATA = "{A}";
	public final static String CLIENTNAME = "{C}";
	
	public static String workingDir = APPDATA + File.separator + ".thesinner";
	public static String clientPath = workingDir + File.separator + CLIENTNAME;
	
	public static int width;
	public static int height;
	
	public static String title = "The Sinner";
	
	public static String webpath = "http://the-sinner.net/site";
	
	public static boolean useAutoenter = false;
	public static boolean patchDir = false;
	
	public static ServerInfo[] servers = new ServerInfo[]{
		new ServerInfo("The Sinner","1.7.10","the-sinner.net",25565)
	};
	
	public static String protectionKey = "Change this if you aren't noob";
	public static String key1 = "0123456789ABCDEF"; //16значный ключ
	public static String key2 = "0123456789ABCDEF";
	
	public static String version = "Cadaveria";
	
	public static void onStartMinecraft(){}
	
}
