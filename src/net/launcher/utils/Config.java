package net.launcher.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {
	public static JSONObject config;
	
	
	public static void load(String path) throws FileNotFoundException, ParseException, IOException{
		JSONParser p = new JSONParser();
		config = (JSONObject) p.parse(ClientUtils.readAll(path));
	}
	
	public static void save(String path) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(path, "UTF-8");
		writer.print(config.toJSONString());
		writer.close();
	}
	
	public static Object get(String key, Object d){
		try{
			Object o = config.get(key);
			return (o != null ? o : d);
		}catch(NullPointerException e){
			return d;
		}
	}
}
