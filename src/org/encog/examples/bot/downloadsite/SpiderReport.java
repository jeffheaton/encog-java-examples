package org.encog.examples.bot.downloadsite;

import java.io.*;
import java.net.*;

import org.encog.bot.html.URLUtility;
import org.encog.bot.spider.Spider;
import org.encog.bot.spider.SpiderParseHTML;
import org.encog.bot.spider.SpiderReportable;


/**
 * Recipe #13.2: Download Site 
 * 
 * Copyright 2007 by Jeff Heaton(jeff@jeffheaton.com)
 *
 * HTTP Programming Recipes for Java Bots
 * ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 * 
 * The report class for the Site Downloader.
 * 
 * This software is copyrighted. You may use it in programs
 * of your own, without restriction, but you may not publish
 * the source code without the author's permission. For more
 * information on distributing this code, please visit:
 * http://www.heatonresearch.com/hr_legal.php
 * 
 * @author Jeff Heaton
 * @version 1.1
 */
public class SpiderReport implements SpiderReportable {
  /*
   * The base host. Only URL's from this host will be
   * downloaded.
   */
  private String base;

  /*
   * The local path to save downloaded files to.
   */
  private String path;

  /**
   * Construct a SpiderReport object.
   * 
   * @param path
   *          The local file path to store the files to.
   */
  public SpiderReport(String path) {
    this.path = path;
  }

  /**
   * This function is called when the spider is ready to
   * process a new host. This function simply stores the
   * value of the current host.
   * 
   * @param host
   *          The new host that is about to be processed.
   * @return True if this host should be processed, false
   *         otherwise.
   */
  public boolean beginHost(String host) {
    if (this.base == null) {
      this.base = host;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Called when the spider is starting up. This method
   * provides the SpiderReportable class with the spider
   * object. This method is not used in this manager.
   * 
   * @param spider
   *          The spider that will be working with this
   *          object.
   */
  public void init(Spider spider) {
  }

  /**
   * Called when the spider encounters a URL. If the URL is
   * on the same host as the base host, then the function
   * will return true, indicating that the URL is to be
   * processed.
   * 
   * @param url
   *          The URL that the spider found.
   * @param source
   *          The page that the URL was found on.
   * @param type
   *          The type of link this URL is.
   * @return True if the spider should scan for links on
   *         this page.
   */
  public boolean spiderFoundURL(URL url, URL source,
      SpiderReportable.URLType type) {

    if ((this.base != null) && (!this.base.equalsIgnoreCase(url.getHost()))) {
      return false;
    }

    return true;
  }

  /**
   * Called when the spider is about to process a NON-HTML
   * URL.
   * 
   * @param url
   *          The URL that the spider found.
   * @param stream
   *          An InputStream to read the page contents from.
   * @throws IOException
   *           Thrown if an IO error occurs while processing
   *           the page.
   */
  public void spiderProcessURL(URL url, InputStream stream) throws IOException {
    byte[] buffer = new byte[1024];

    int length;
    String filename = URLUtility.convertFilename(this.path, url, true);

    try {
      OutputStream os = new FileOutputStream(filename);
      do {
        length = stream.read(buffer);
        if (length != -1) {
          os.write(buffer, 0, length);
        }
      } while (length != -1);
      os.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Called when the spider is ready to process an HTML
   * URL. Download the contents of the URL to a local file.
   * 
   * @param url
   *          The URL that the spider is about to process.
   * @param parse
   *          An object that will allow you you to parse the
   *          HTML on this page.
   * @throws IOException
   *           Thrown if an IO error occurs while processing
   *           the page.
   */
  public void spiderProcessURL(URL url, SpiderParseHTML parse)
      throws IOException {
    String filename = URLUtility.convertFilename(this.path, url, true);
    OutputStream os = new FileOutputStream(filename);
    parse.getStream().setOutputStream(os);
    parse.readAll();
    os.close();

  }

  /**
   * Called when the spider tries to process a URL but gets
   * an error. This method is not used in tries manager.
   * 
   * @param url
   *          The URL that generated an error.
   */
  public void spiderURLError(URL url) {
  }

}
