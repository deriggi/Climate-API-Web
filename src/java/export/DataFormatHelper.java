/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package export;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import domain.web.AnnualMapData;
import domain.web.EnsembleDatum;
import domain.web.GcmDatum;
import domain.web.MapData;
import domain.web.MonthlyMapData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author wb385924
 */
public class DataFormatHelper {

    private static final String comma = ",";
    private static final String newline = "line.separator";

    /**
     * Given a media type return in that form and default to json
     */

    public static String formatData(String type, Object data) {

        if (type != null && type.equals(MediaType.APPLICATION_XML)) {
            XStream xstream = new XStream();
            String xml = xstream.toXML(data);
            return xml;

        } else {
            Gson gson = new Gson();
            return gson.toJson(data);
        }
    }

    public static String formatEnsembles(String type, Collection<EnsembleDatum> ensembles) {

        if (type != null && type.equals(MediaType.APPLICATION_XML)) {
            XStream xstream = new XStream();
            String xml = xstream.toXML(ensembles);
            return xml;
         } else if (type.equals(MediaType.TEXT_PLAIN)) {
            return getEnsembleDatumAsCsv(ensembles);
        } else {
            Gson gson = new Gson();
            return gson.toJson(ensembles);
        }
    }

    public static String formatMonthlyOrAnnualMap(String type, HashMap<Integer, Double> map) {
        if (map == null) {
            return new Gson().toJson(map);
        }

        if (type != null && type.equals(MediaType.APPLICATION_XML)) {
            XStream xstream = new XStream();
            MapData md = createMonthOrAnnual(map);
            String xml = xstream.toXML(md);
            return xml;
        } else if (type.equals(MediaType.TEXT_PLAIN)) {
            return getMapDataCsv(map);
        } else {
            Gson gson = new Gson();
            return gson.toJson(getAnnualOrMonthlyMapValuesAsList(map));
        }
    }

    private static MapData createMonthOrAnnual(HashMap<Integer, Double> map) {
        if (map == null ) {
            return null;
        }

        if (map.size() == 1) {
            AnnualMapData annual = new AnnualMapData();
            annual.assignValuesFromMap(map);
            return annual;
        } else {
            MonthlyMapData md = new MonthlyMapData();
            md.assignValuesFromMap(map);
            return md;
        }


//        md.makeReadableMonthMap(map);
    }

    private static String getMapDataCsv(HashMap<Integer, Double> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (map.size() == 1) {
            addAnnualCsvHeader(sb);
        } else if(map.size() > 1){
            addMonthCsvHeader(sb);
        }

        sb.append(System.getProperty(newline));
        Set<Integer> keys = map.keySet();
        int counter = 0;
        for (Integer key : keys) {
            sb.append(map.get(key));
            if (counter++ < keys.size() - 1) {
                sb.append(comma);
            }
        }
        return sb.toString();
    }

    private static List<Double> getAnnualOrMonthlyMapValuesAsList(HashMap<Integer, Double> map) {
        if (map == null || map.isEmpty()) {
            return new ArrayList<Double>(0);
        }
        Double [] monthvals = null;
        if (map.size() == 1) {
            return new ArrayList<Double>(map.values());
        } else if (map.size() > 1){
            // this code is crap
            Set<Integer> keys = map.keySet();
            List<Integer> sortedKeys = new ArrayList<Integer>(keys);
            Collections.sort(sortedKeys);
            boolean isZeroBased = true;
            if(sortedKeys.get(0) == 1){
                isZeroBased = false;
            }
            monthvals = new Double[12];
            for(Integer i: keys){
                if(!isZeroBased){
                    i = i - 1;
                    monthvals[i] = map.get(i+1);
                }else{
                    monthvals[i] = map.get(i);
                }
                
            }
        }

        return Arrays.asList(monthvals);
    }

    public static String formatGcms(String type, Collection<GcmDatum> datums) {
        if (type.equals(MediaType.TEXT_PLAIN)) {
            return getGcmDatumAsCsv(type, datums);
        }
        return formatData(type, datums);
    }

    public static String getGcmDatumAsCsv(String type, Collection<GcmDatum> datums) {
        if (datums == null || datums.isEmpty()) {
            return null;
        }
         if (datums == null || datums.isEmpty()) {
            return null;
        }

        GcmDatum tester = datums.iterator().next();

        StringBuilder sb = new StringBuilder();
        sb.append(tester.getCsvHeader());

        sb.append(System.getProperty(newline));
        for (GcmDatum datum : datums) {
            sb.append(datum.getCsvLine());
            sb.append(System.getProperty(newline));

        }
        return sb.toString();

    }

    private static void addMonthCsvHeader(StringBuilder sb) {
        if (sb == null) {
            return;
        }
        sb.append("Jan");
        sb.append(comma);
        sb.append("Feb");
        sb.append(comma);
        sb.append("Mar");
        sb.append(comma);
        sb.append("Apr");
        sb.append(comma);
        sb.append("May");
        sb.append(comma);
        sb.append("Jun");
        sb.append(comma);
        sb.append("Jul");
        sb.append(comma);
        sb.append("Aug");
        sb.append(comma);
        sb.append("Sep");
        sb.append(comma);
        sb.append("Oct");
        sb.append(comma);
        sb.append("Nov");
        sb.append(comma);
        sb.append("Dec");

    }

    private static void addAnnualCsvHeader(StringBuilder sb) {
        if (sb == null) {
            return;
        }
        sb.append("annual");

    }

    public static String getEnsembleDatumAsCsv( Collection<EnsembleDatum> datums) {
        if (datums == null || datums.isEmpty()) {
            return null;
        }

        EnsembleDatum tester = datums.iterator().next();

        StringBuilder sb = new StringBuilder();
        sb.append(tester.getCsvHeader());

        sb.append(System.getProperty(newline));
        for (EnsembleDatum datum : datums) {
            sb.append(datum.getCsvLine());
            sb.append(System.getProperty(newline));

        }
        return sb.toString();
    }

    
}
