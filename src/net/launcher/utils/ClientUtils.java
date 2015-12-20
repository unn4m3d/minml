package net.launcher.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.launcher.Settings;
import net.launcher.TempSettings;

public class ClientUtils {
	
	public static ArrayList<URL> urls = new ArrayList<URL>();
	public static Map<String, BufferedImage> imgs = new HashMap<String, BufferedImage>();
	
	public static int getPlatform()
	{
		String osName = System.getProperty("os.name").toLowerCase();

		if(osName.contains("win")) return 2;
		if(osName.contains("mac")) return 3;
		if(osName.contains("solaris")) return 1;
		if(osName.contains("sunos")) return 1;
		if(osName.contains("linux")) return 0;
		if(osName.contains("unix")) return 0;

		return 4;
	}
	
	public static File getMcDir()
	{
		String home = System.getProperty("user.home", "");
		String path = Settings.clientPath.replaceAll(Pattern.quote(Settings.CLIENTNAME),TempSettings.client.getName());
		switch(getPlatform())
		{
			case 1: return new File(path.replaceAll(Pattern.quote(Settings.APPDATA),home));
			case 2:
				String appData = System.getenv("SYSTEMDRIVE");
				if(appData != null) return new File(path.replaceAll(Pattern.quote(Settings.APPDATA), home));
				else return new File(path.replaceAll(Pattern.quote(Settings.APPDATA),home));
			//case 3: return new File(home, path);
			default: return new File(path.replaceAll(Pattern.quote(Settings.APPDATA),home));
		}
	}
	
	public static File getAppdata(){
		String home = System.getProperty("user.home","");
		switch(getPlatform())
		{
			case 1: return new File(home);
			case 2:
				String appData = System.getenv("SYSTEMDRIVE");
				if(appData != null) return new File(appData);
				else return new File(home);
			//case 3: return new File(home, path);
			default: return new File(home);
		}
	}
	
	public static ArrayList<File> list(File dir, boolean recur){
		ArrayList<File> result = new ArrayList<File>();
		if(!dir.isDirectory()){
			result.add(dir);
			return result;
		}
		for(File f : dir.listFiles()){
			if(f.isDirectory()){
				if(recur){
					result.addAll(list(f,true));
				}else{
					result.add(f);
				}
			}else{
				result.add(f);
			}
			
		}
		return result;
	}
	
	
	@SuppressWarnings("deprecation")
	public static void updateURLs(){
		urls = new ArrayList<URL>();
		File[] dirs = new File[]{
			new File(getMcDir(),"bin"),
			new File(getMcDir(),"libraries")
		};
		
		for(File d : dirs){
			if(!d.exists() || !d.isDirectory()) continue;
			ArrayList<File> l = list(d,true);
			for(File f : l){
				try{
				if(f.getName().matches("\\.jar")) urls.add(f.toURL());
				}catch(MalformedURLException e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public static String readAll(String path) throws FileNotFoundException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(path));
		StringBuilder sb = new StringBuilder();
		try {
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		} finally {
		    br.close();
		}
		return sb.toString();
	}

	public static void patchDir( URLClassLoader cl ) {
		if(!Settings.patchDir) return;
		
		try {
			Class< ? > c = cl.loadClass( "net.minecraft.client.Minecraft" );

			send("Changing client dir...");
			
			for ( Field f : c.getDeclaredFields() ) {       
				if( f.getType().getName().equals( "java.io.File" ) & Modifier.isPrivate( f.getModifiers() ) 
						& Modifier.isStatic( f.getModifiers() )) 
				{
					f.setAccessible( true );
					f.set( null, getMcDir() );
		            send("Patching succesful, herobrine removed.");
		            return;
				}
			}
		}catch ( Exception e ) {
			sendErr( "Client not patched" );
		}
	}
	
	public static void send(String s){
		System.out.println(s);
	}
	
	public static void sendErr(String s){
		System.err.println(s);
	}
	
	public static BufferedImage getLocalImage(String name)
	{
		try
		{
			if(imgs.containsKey(name)) return (BufferedImage)imgs.get(name);

			BufferedImage img = ImageIO.read(ClientUtils.class.getResource("/net/launcher/theme/" + name + ".png"));
			imgs.put(name, img);
			send("Opened local image: " + name + ".png");
			return img;
		}
		catch(Exception e)
		{
			sendErr("Fail to open local image: " + name + ".png");
			return getEmptyImage();
		}
	}
	
	public static BufferedImage getEmptyImage()
	{
		return new BufferedImage(9, 9, BufferedImage.TYPE_INT_ARGB);
	}
	
	public static File getAssetsDir(){
		return new File(Settings.workingDir.replaceAll(Pattern.quote(Settings.APPDATA), getAppdata().toString()));
	}
	
	
}
