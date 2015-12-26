package net.launcher.utils;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * @note Note this is not async!
 * */
public class FileDownloader {
	public String file;
	public String outFile;
	//public double speed;
	//private long downloadStartTime;
	protected ArrayList<ActionListener> update = new ArrayList<ActionListener>();
	public long size = 0;
	
	public void addUpdateListener(ActionListener a){
		update.add(a);
	}
	
	public void onUpdate(Object o){
		for(ActionListener a : update){
			a.update(o);
		}
	}
	
	public FileDownloader(String file, String out){
		this.file = file;
		this.outFile = out;
		System.out.println(file + "\n" + out);
	}
	
	public void start() throws MalformedURLException, IOException, Exception{
		byte[] buffer = new byte[1024];
		OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try{
			out = new BufferedOutputStream(new FileOutputStream(outFile));
			conn = new URL(file).openConnection();
			in = conn.getInputStream();
			//downloadStartTime = System.getCurrentTimeMillis();
			size = conn.getHeaderFieldInt("content-length",0);
			
			int read = 0;
			long totalRead = 0;
			while((read = in.read(buffer)) != -1){
				totalRead += read;
				out.write(buffer,0,read);
				onUpdate((Object)totalRead);
			}
			
		}finally{
			try{
				if(out != null){
					out.close();
				}
				if(in != null){
					in.close();
				}
			}catch(IOException e){}
		}
	}
	
	
}
