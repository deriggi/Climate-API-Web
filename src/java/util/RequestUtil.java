/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.regex.Pattern;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author wb385924
 */
public class RequestUtil {

    /**
     * Given the a resourcedentifier determine the exteions and return the media type string
     * @param resourceIdentifier
     */
    private static Pattern xmlPattern = null;
    private static Pattern csvPattern = null;
    private static final String XML_REGEX = "(?i)\\.XML";
    private static final String CSV_REGEX = "(?i)\\.CSV";
    private static final String DOT = ".";
    public  static final String APP_KML = "application/vnd.google-earth.kml+xml";
    private static Pattern getXMLPattern() {
        if (xmlPattern == null) {
            xmlPattern = Pattern.compile(XML_REGEX);
        }
        return xmlPattern;
    }

    private static Pattern getCSVPattern() {
        if (csvPattern == null) {
            csvPattern = Pattern.compile(CSV_REGEX);
        }
        return csvPattern;
    }


    /**
     * Return up to the dot or just the string
     * @param resourceIdentifier
     * @return
     */
      public static String getIdentifier(String resourceIdentifier) {
        if(resourceIdentifier == null){
            return null;
        }

        int dotIndex = resourceIdentifier.indexOf(DOT);
        if(dotIndex > 0){
            return resourceIdentifier.substring(0,dotIndex);
        }
        return resourceIdentifier;

    }

    public static String getResponseType(String resourceIdentifier) {
        if (resourceIdentifier == null || resourceIdentifier.trim().length() == 0) {
            return MediaType.APPLICATION_JSON;
        }
        if (getXMLPattern().matcher(resourceIdentifier).find()) {
            return MediaType.APPLICATION_XML;
        }
        if (getCSVPattern().matcher(resourceIdentifier).find()) {
            return MediaType.TEXT_PLAIN;
        }

        return MediaType.APPLICATION_JSON;

    }
}
