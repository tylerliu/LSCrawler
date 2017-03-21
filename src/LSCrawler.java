import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LSCrawler implements Runnable {
    private static String path;
    private static String prefix = "http://www.lschs.org/uploaded/_assets/images/portraits/students/";
    private static final String suffix = ".jpg";
    private final String httpUrl, saveFile;
    private static boolean isDownload;

    private LSCrawler(int ID) {
        this.httpUrl = prefix + ID + suffix;
        this.saveFile = path + ID + suffix;
    }

    @Override
    public void run(){
        int byteRead;
        InputStream inStream;
        try {
            URLConnection conn = new URL(httpUrl).openConnection();
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

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Do you want to download the photos?(Y/N):");
        isDownload = scan.next().toUpperCase().charAt(0) == 'Y';
        if (isDownload) {
            System.out.print("enter directory:");
            path = scan.next();
        }
        int year = Calendar.getInstance().get(Calendar.YEAR) - (Calendar.getInstance().get(Calendar.MONTH) < 9 ? 1 : 0);
        prefix += year + "-" + (year % 100 + 1) + "/";
        ExecutorService fetcher = Executors.newFixedThreadPool(16);
        for (int i = (year + 1) % 100 * 10000; i <= ((year + 1) % 100 + 4) * 10000; i++)
            fetcher.execute(new LSCrawler(i % 1000000));
        fetcher.shutdown();
        scan.close();
        while (!fetcher.isTerminated()) {
        }
    }
}
