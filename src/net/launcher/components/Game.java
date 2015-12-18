package net.launcher.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.json.simple.*;


import net.minecraft.Launcher;
import net.launcher.Settings;
import net.launcher.TempSettings;
import net.launcher.utils.ClientUtils;
import net.launcher.utils.Config;
import net.launcher.utils.Crypt;
//import net.launcher.utils.BaseUtils;
//import net.launcher.utils.EncodingUtils;
//import net.launcher.utils.GuardUtils;
import net.launcher.utils.java.eURLClassLoader;

public class Game extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static Launcher mcapplet;
	private eURLClassLoader cl;
	String Class = null;
	Timer timer = null;
	int i = 0;
	
	public Game(JSONObject answer)
	{
		
		String bin = ClientUtils.getMcDir().toString() + File.separator + "bin" + File.separator;	
		cl = new eURLClassLoader(ClientUtils.urls.toArray(new URL[ClientUtils.urls.size()]));
		
		boolean old = false;
		try
		{   
			cl.loadClass("net.minecraft.client.Minecraft");
			old = true;
		} catch(Exception e) {}
		String user = (String) answer.get("user");
		String session = Crypt.xorencode((String)answer.get("sid"), Settings.protectionKey);
		
		if(old)
		{		
			/*Thread check = new Thread(new Runnable() {
			    @Override
				public void run() {
			    	GuardUtils.checkMods(answer, true);
					if(Settings.useModCheckerTimer ) new Timer(30000, new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							GuardUtils.checkMods(answer, false);
						}
					}).start();
			    }
			});
			check.start();*/ //Будет восстановлено в следующих версиях
			
			try
			{
				addWindowListener(new WindowListener()
				{
					public void windowOpened(WindowEvent e) {}
					public void windowIconified(WindowEvent e) {}
					public void windowDeiconified(WindowEvent e) {}
					public void windowDeactivated(WindowEvent e) {}
					public void windowClosed(WindowEvent e) {}
					public void windowActivated(WindowEvent e) {}
					public void windowClosing(WindowEvent e)
					{
						mcapplet.stop();
						mcapplet.destroy();
						System.exit(0);
					}
				});
				setForeground(Color.BLACK);
				setBackground(Color.BLACK);
				
				mcapplet = new Launcher(bin, ClientUtils.urls.toArray(new URL[ClientUtils.urls.size()]));
				mcapplet.customParameters.put("username", user);
				mcapplet.customParameters.put("sessionid", session);
				mcapplet.customParameters.put("stand-alone", "true");
				if(Settings.useAutoenter)
				{
					mcapplet.customParameters.put("server", TempSettings.client.ip);
					mcapplet.customParameters.put("port", String.valueOf(TempSettings.client.port));
				}
				setTitle(Settings.title);
				/*if(Frame.main != null)
				{
					Frame.main.setVisible(false);
					setBounds(Frame.main.getBounds());
					setExtendedState(Frame.main.getExtendedState());
					setMinimumSize(Frame.main.getMinimumSize());
				}*/
				setSize(Settings.width, Settings.height+28);
				setMinimumSize(new Dimension(Settings.width, Settings.height+28));
				setLocationRelativeTo(null);
				mcapplet.setForeground(Color.BLACK);
				mcapplet.setBackground(Color.BLACK);
				setLayout(new BorderLayout());
				add(mcapplet, BorderLayout.CENTER);
				validate();
				if((boolean)Config.get("fullscreen", false)) setExtendedState(JFrame.MAXIMIZED_BOTH);
				setIconImage(ClientUtils.getLocalImage("favicon"));
				setVisible(true);
				
				/*if(Settings.useConsoleHider)
				{
					System.setErr(new PrintStream(new NulledStream()));
					System.setOut(new PrintStream(new NulledStream()));
				}*/
				mcapplet.init();
				mcapplet.start();
			} catch(Exception e)
			{
				e.printStackTrace();
			}
			
		} else {
			/*Thread check = new Thread(new Runnable() {
				@Override
				public void run() {
					GuardUtils.checkMods(answer, true);
					ActionListener a = new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							GuardUtils.checkMods(answer, false);
					        if (++i > Settings.useModCheckerint) {
					            timer.stop();
					        }
					        
					    }
					};
				timer = new Timer(30000, a);
			    timer.start();
			    }
			    });
			check.start();*/
			try
			{
				System.out.println("Running Minecraft");
				String jarpath = ClientUtils.getMcDir().toString() + File.separator + "bin" + File.separator;
				String minpath = ClientUtils.getMcDir().toString();
				String assets = ClientUtils.getAssetsDir().toString() + File.separator;
				List<String> params = new ArrayList<String>();
				System.setProperty("fml.ignoreInvalidMinecraftCertificates", "true");
				System.setProperty("fml.ignorePatchDiscrepancies", "true");
				System.setProperty("org.lwjgl.librarypath", jarpath+"natives");
				System.setProperty("net.java.games.input.librarypath", jarpath+"natives");
				System.setProperty("java.library.path", jarpath+"natives");
				if((boolean)Config.get("fullscreen", false))
				{          
					params.add("--fullscreen");
					params.add("true");
				}
				else
				{
					params.add("--width");
					params.add(String.valueOf(Settings.width));
					params.add("--height");
					params.add(String.valueOf(Settings.height));
				}	
				if(Settings.useAutoenter) {
					params.add("--server");
					params.add(TempSettings.client.ip);
					params.add("--port");
					params.add(String.valueOf(TempSettings.client.port));
				}		
				try {
					cl.loadClass("com.mojang.authlib.Agent");
					params.add("--accessToken");
					params.add(session);
					params.add("--uuid");
					params.add(Crypt.xorencode(Crypt.inttostr((String)answer.get("sid")), Settings.protectionKey));
					params.add("--userProperties");
					params.add("{}");
					params.add("--assetIndex");
					params.add(TempSettings.client.version);
				} catch (ClassNotFoundException e2) {
					params.add("--session");
					params.add(session);
				}		
				params.add("--username");
				params.add(user);
				params.add("--version");
				params.add(TempSettings.client.version);
				params.add("--gameDir");
				params.add(minpath);
				params.add("--assetsDir");
				if(Integer.parseInt(TempSettings.client.version.replace(".", "")) < 173)
				{
					params.add(assets+"assets/virtual/legacy");
				} else {
					params.add(assets+"assets");
				}
				boolean tweakClass = false;
				try {
					cl.loadClass("com.mumfrey.liteloader.launch.LiteLoaderTweaker");
					params.add("--tweakClass");
					params.add("com.mumfrey.liteloader.launch.LiteLoaderTweaker");
					tweakClass = true;
				} catch (ClassNotFoundException e) {}	
				try {
					cl.loadClass("cpw.mods.fml.common.launcher.FMLTweaker");
					params.add("--tweakClass");
					params.add("cpw.mods.fml.common.launcher.FMLTweaker");
					tweakClass = true;
				} catch (ClassNotFoundException e) {}
	            if(tweakClass)
				{
					Class = "net.minecraft.launchwrapper.Launch";
				} else {
					Class = "net.minecraft.client.main.Main";
				}
				
                //Frame.main.setVisible(false);
				try
				{
					Class<?> start = cl.loadClass(Class);
					Method main = start.getMethod("main", new Class[] { String[].class });
					main.invoke(null, new Object[] { params.toArray(new String[0]) });
				} catch (Exception e)
				{
					e.printStackTrace();
					System.exit(0);
				}
			} catch (Exception e) {}
		}
	}
}
