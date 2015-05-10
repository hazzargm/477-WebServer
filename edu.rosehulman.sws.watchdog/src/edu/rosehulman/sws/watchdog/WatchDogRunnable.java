package edu.rosehulman.sws.watchdog;

import java.util.Scanner;

public class WatchDogRunnable implements Runnable {
	
	private Scanner scanner;

	public WatchDogRunnable(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public void run() {
		String line;
		while(true) {
			if (scanner.hasNext())
				System.out.println(scanner.next());
		}
	}

}
