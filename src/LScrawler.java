import java.io.*;
import java.net.*;

import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LSCrawler implements Runnable {
    private final String httpUrl, saveFile;
	private static string isDownload;
	private final String staticAdd = "http://www.lschs.org/uploaded/_assets/images/portraits/students/"+year + "-" + (year % 100 + 1) +"/";
	private final String suffix = ".jpg";
	private Scanner scan = new Scanner(System.in);
	private String path = null;
	private boolean UnknownCommand;
	private boolean Rotate = 1;

	private LSCrawler(String httpUrl, String saveFile){
		this.httpUrl = httpUrl;
		this.saveFile = saveFile;
	}

	@Override
	public void run(){
		int byteRead;
		InputStream inStream;
		URL url;
		try {
			url = new URL(httpUrl);
            URLConnection conn = url.openConnection();
			conn.connect();
			inStream = conn.getInputStream();

            byte[] buffer = new byte[1024];
            if ((byteRead = inStream.read(buffer)) != -1 && new String(buffer).contains("html")) return;

            //if no exception & error, file exist
            System.out.println("Hit 1 target..." + httpUrl.substring(httpUrl.lastIndexOf('/') + 1));

            if (!isDownload) return;
            FileOutputStream fs = new FileOutputStream(saveFile);
            do
                fs.write(buffer, 0, byteRead);
            while ((byteRead = inStream.read(buffer)) != -1);

			fs.close();
		} catch (Exception ignored) {}
	}

	private void startDownloading(){
		int year = Calendar.getInstance().get(Calendar.YEAR) - (Calendar.getInstance().get(Calendar.MONTH) < 9 ? 1 : 0);
		ExecutorService fetcher = Executors.newFixedThreadPool(16);
		for (int i = (year + 1) % 100 * 10000; i <= ((year + 1) % 100 + 4) * 10000; i++)
			fetcher.execute(new LSCrawler(staticAdd + i + suffix, path + i + suffix));
		fetcher.shutdown();
		scan.close();
		while (!fetcher.isTerminated()){}
	}

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
				startDownloading();
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
