package net.launcher.components;

public class ServerInfo {
	public String name;
	public String ip;
	public int port;
	public String version;
	
	public ServerInfo(String n, String o,String i, int p){
		name = n;
		version = o;
		ip = i;
		port = p;
	}
	
	public String getName(){
		return this.name.replaceAll("\\s", "").toLowerCase();
	}
}
