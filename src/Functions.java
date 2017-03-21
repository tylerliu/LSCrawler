import Variables;


class Functions implements Runnable{
    void startDownloading(String path) {
        int year = Calendar.getInstance().get(Calendar.YEAR) - (Calendar.getInstance().get(Calendar.MONTH) < 9 ? 1 : 0);
        ExecutorService fetcher = Executors.newFixedThreadPool(16);
        for (int i = (year + 1) % 100 * 10000; i <= ((year + 1) % 100 + 4) * 10000; i++)
            fetcher.execute(new LSCrawler(staticAdd + i + suffix, path + i + suffix));
        fetcher.shutdown();
        scan.close();
        while (!fetcher.isTerminated()) {
        }
    }

    @Override
    public void run() {
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
        } catch (Exception ignored) {
        }
    }

    void LSCrawler(String httpUrl, String saveFile) {
        this.httpUrl = httpUrl;
        this.saveFile = saveFile;
    }
};
