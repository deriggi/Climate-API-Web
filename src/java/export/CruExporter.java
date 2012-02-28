/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package export;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import domain.CruType;
import java.util.List;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author wb385924
 */
public class CruExporter {

    private static final String newline = "line.separator";
    private static final String comma = ",";

    public static String formatCru(String type, List<? extends CruType > cru) {
        if (cru == null) {
            return new Gson().toJson(null);
        }

        if (type != null && type.equals(MediaType.APPLICATION_XML)) {
            XStream xstream = new XStream();
            String xml = xstream.toXML(cru);
            return xml;
        } else if (type.equals(MediaType.TEXT_PLAIN)) {
            return getCruCsv(cru);
        } else {
            Gson gson = new Gson();
            return gson.toJson(cru);
        }
    }

    private static String getHeader(CruType c) {
        StringBuilder sb = new StringBuilder();
        if (c.getYear() != -1) {
            sb.append("year,");
        }
        if (c.getMonth() != -1) {
            sb.append("month,");
        }
        sb.append("data");
        return sb.toString();
    }

    private static String getCsvLine(CruType c) {
        StringBuilder sb = new StringBuilder();
        if (c.getYear() != -1) {
            sb.append(c.getYear());
            sb.append(comma);
        }
        if (c.getMonth() != -1) {
            sb.append(c.getMonth());
            sb.append(comma);
        }
        sb.append(c.getData());

        return sb.toString();
    }

    private static String getCruCsv(List<? extends CruType> cru) {
        if (cru == null || cru.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getHeader(cru.get(0)));
        sb.append(System.getProperty(newline));
        int counter = 0;
        for (CruType c : cru) {
            sb.append(getCsvLine(c));
            sb.append(System.getProperty(newline));
            
        }
        return sb.toString();

    }
}
