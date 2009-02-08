package org.encog.examples.bot.downloadsite;

import java.io.IOException;
import java.net.URL;

import org.encog.HSQLUtil;
import org.encog.bot.spider.Spider;
import org.encog.util.orm.SessionManager;

/**
 * Download a website using Encog.
 * @author jeff
 */
public class DownloadSite {
  /**
   * The main method.
   * 
   * @param args
   *          Specifies the path to the config file, the
   *          path to download to, and the starting URL.
   */
  public static void main(String args[]) {
    try {
      if (args.length < 2) {
        System.out
            .println(
        "Usage: DownloadSite [Path to download to] [URL to download]");
      } else {
        DownloadSite download = new DownloadSite();
        download.download(new URL(args[1]), args[0]);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void download(URL base, String local)
      throws IOException, InstantiationException,
      IllegalAccessException, ClassNotFoundException {
	System.out.println("Downloading " + base + " to " + local);
    SpiderReport report = new SpiderReport(base.getHost(),local);
    SessionManager manager = HSQLUtil.getSessionManager();
    Spider spider = new Spider(manager, report);
    spider.process(base);
  }
}
