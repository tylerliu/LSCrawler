import java.io.*;
import java.net.*;

import java.util.Scanner;

public class LScrawler implements Runnable {
	private String httpUrl, saveFile;
	private boolean IsDownload;
	private Thread t;

	public LScrawler(String httpUrl, String saveFile, boolean isdownload){
		t = new Thread(this, httpUrl.substring(httpUrl.lastIndexOf('/') + 1));
		this.httpUrl = httpUrl;
		this.saveFile = saveFile;
		this.IsDownload = isdownload;
		t.start();
	}

	@Override
	public void run(){
		int bytesum = 0, byteread = 0;
		InputStream inStream = null;
		URL url = null;
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e1) {}

		try {
			URLConnection conn = url.openConnection();
			conn.connect();
			inStream = conn.getInputStream();
			int lenghtOfFile = conn.getContentLength();
		} catch (FileNotFoundException e) {// file not exist, skip
		} catch (Exception e) {}
		
		//if no exception, file exist
		System.out.println("LsFetcher: hit 1 target..." + httpUrl.substring(httpUrl.lastIndexOf('/') + 1));
		try {
			if (!this.IsDownload)
				return;// if not download exit
			FileOutputStream fs = new FileOutputStream(saveFile);

			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			System.out.println(bytesum);
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String path = null;
		System.out.print("Do you want to download the photos?(Y/N)");
		boolean isdownload = (scan.next().toUpperCase().charAt(0) == 'Y');
		if (isdownload) {
			System.out.print("enter directory:");
			path = scan.next();
		}
		final String staticAdd = "http://www.lschs.org/uploaded/_assets/images/portraits/students/2015-16/";
		final String suffix = ".jpg";
		for (int i = 160000; i <= 200000; i++) {
			new LScrawler(staticAdd + i + suffix, path + i + suffix, isdownload);
		}
		scan.close();
	}
}
