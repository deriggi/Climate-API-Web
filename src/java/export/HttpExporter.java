/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Johnny
 */
public class HttpExporter {

    public String postMessage(String msg, String address) {
        BufferedInputStream bis = null;
        BufferedReader br = null;
        InputStreamReader isr = null;

        PrintWriter pw = null;
        System.out.println("RouterMDB/postMessage to address: " + address);
        try {
            URL url = new URL(address);
            URLConnection uc = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) uc;
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "text/xml");
            pw = new PrintWriter(conn.getOutputStream());
            pw.write(msg);
            pw.close();
            bis = new BufferedInputStream(conn.getInputStream());


            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("we got a good response");

                isr = new InputStreamReader(bis);
                br = new BufferedReader(isr);
                String response = null;
                StringBuilder sb = new StringBuilder();
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
                return sb.toString();
            } else {
                System.out.println(conn.getResponseMessage());
                // Server returned HTTP error code.
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                    Logger.getLogger(HttpExporter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pw != null) {
                pw.close();
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    Logger.getLogger(HttpExporter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return null;
    }
}
