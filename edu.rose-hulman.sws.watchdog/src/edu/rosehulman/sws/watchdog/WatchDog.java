package edu.rose-hulman.sws.watchdog;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class WatchDog {

	public static final String serverFilepath = System.getProperty("user.dir") + File.separator + "server" + File.separator + "webserver.jar";
	public static final String rootDirFilepath = System.getProperty("user.dir") + File.separator + "web";
	public static final String port = "8080";
	public static final String pluginDirFilepath = System.getProperty("user.dir") + File.separator + "plugins";

	
	public static void main(String[] args) throws Exception {
//		ProcessBuilder builder = new ProcessBuilder("java", "-jar", serverFilepath);//, rootDirFilepath, port, pluginDirFilepath);
		Process p = Runtime.getRuntime().exec("java -jar " + serverFilepath + " " + rootDirFilepath + " " + port + " " + pluginDirFilepath);
		//Process p = builder.start();
		System.out.println("server started");
		InputStream in = p.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		Scanner scanner = new Scanner(in);
		new Thread(new WatchDogRunnable(scanner)).start();

	}

}
