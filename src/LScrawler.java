import java.io.*;
import java.net.*;

import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LSCrawler {
	private String path = null;
	private boolean UnknownCommand;
	private boolean Rotate = 1;

	public static void main(String[] args) {

		do{
			if(UnknownCommand){
				System.out.print("It seems that you refused to Download the Photos...\n");
			}

			System.out.print("Do you want to download the photos?(Y/N/Quit):");
			isDownload = scan.next();

			if(isDownload == "Y" || isDownload == "y"){
				UnknownCommand = 0;
				System.out.print("enter directory:");
				path = scan.next();
				startDownloading(path);
				Rotate = 1;
			}
			else if(isDownload.toUpperCase() == "QUIT"){Rotate == 0;};
			else{
				UnknownCommand = 1;
				Rotate = 1;
			}

		}while(Rotate)

        System.out.print("Thanks for using!");


	}
}
