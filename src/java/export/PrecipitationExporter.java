/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export;

import com.google.gson.Gson;
import dao.countryboundary.CountryPrecipitationDao;
import export.util.FileExportHelper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wb385924
 */
public class PrecipitationExporter {
    private static Logger log = Logger.getLogger(PrecipitationExporter.class.getName());
    
    public enum Format { JSON , CSV }
    private static final String COMMA = ",";

    private static String asCSV(List<HashMap<String,String>> maps){

        StringBuilder sb = new StringBuilder();

        for(HashMap<String,String> map : maps){
            Set<String> keys = map.keySet();
            int counter = 0;
            for(String s : keys){
                sb.append(map.get(s));
                if(counter++ < keys.size()-1){
                    sb.append(COMMA);
                }
            }
            sb.append(System.getProperty("line.separator"));
        }

        return sb.toString();
    }

    private static String asCSVObjects(List<HashMap<String,Object>> maps){

        StringBuilder sb = new StringBuilder();

        for(HashMap<String,Object> map : maps){
            Set<String> keys = map.keySet();
            int counter = 0;
            for(String s : keys){
                sb.append(map.get(s));
                if(counter++ < keys.size()-1){
                    sb.append(COMMA);
                }
            }
            sb.append(System.getProperty("line.separator"));
        }

        return sb.toString();
    }

     public static String exportDataTo(String iso3, Format f, String statPrefix,Date fromDate, Date toDate){
        Gson gson = new Gson();

        CountryPrecipitationDao dao = CountryPrecipitationDao.get();

        String data = null;
        
        if(f == Format.JSON){
            data = gson.toJson(dao.getRainDataAsMap(iso3, statPrefix, fromDate, toDate));
            
        }else{
            data = asCSVObjects(dao.getRainDataAsMap(iso3,statPrefix, fromDate, toDate));
            
        }

        return data;

    }

     public static String exportDataTo(int countryId, Format f, String statPrefix, Date fromDate, Date toDate){
        Gson gson = new Gson();

        CountryPrecipitationDao dao = CountryPrecipitationDao.get();

        String data = null;

        if(f == Format.JSON){
            data = gson.toJson(dao.getRainDataAsMap(countryId, statPrefix,fromDate,toDate));

        }else{
            data = asCSV(dao.getRainDataAsMap(countryId,statPrefix,fromDate,toDate));

        }

        return data;

    }


     /**
      * @TODO: fix this so that dates are a paramter
      * @param fileName
      * @param iso3
      * @param f
      */
    public static void exportDataToFile(String fileName, String iso3, Format f){
        Gson gson = new Gson();

        CountryPrecipitationDao dao = CountryPrecipitationDao.get();
        
        String data = null;
        String extension = null;
        if(f == Format.JSON){
//            data = gson.toJson(dao.getRainDataAsMap(iso3, "gf_precipitation"));
            extension = ".txt";
        }else{
//            data = asCSV(dao.getRainDataAsMap(iso3,"gf_precipitation"));
            extension = ".csv";
        }
        
        FileExportHelper.writeToFile(fileName + "." + extension, data);


        //appendToBarSeparatedFile(json);
       // new HttpExporter().postMessage(json, "http://climateportal2.appspot.com/precipitation");



       // log.info(json);

        /**JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        
       CountryRain cr =  gson.fromJson(array.get(0), CountryRain.class);
       log.info(Float.toString(cr.sum) + " " + cr.rainDate);**/
        
    }

     private void appendToBarSeparatedFile(String barSeparated) {
        FileWriter fw = null;
        try {


            File f = new File("countryprecipitation.txt");
            fw = new FileWriter(f, false);
            fw.append(barSeparated);
            fw.append(System.getProperty("line.separator"));



        } catch (IOException ex) {
            log.log(Level.SEVERE, "error writing ", ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                log.log(Level.SEVERE, "error writing ", ex);
            }
        }


    }

    public static void main(String[] args){
        //moz, uga, moz
//        new PrecipitationExporter().exportDataToFile("droughtdata","SOM", Format.JSON);
    }

    static class CountryRain{
        public CountryRain(){
            
        }
        private String rainDate;
        private float sum;
      

        public CountryRain(String study,float sum, String rainDate){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                sdf.parse(rainDate);
                this.sum = sum;
                this.rainDate = rainDate;
                
                //            System.out.println("my rain date is " + this.rainDate);
            } catch (ParseException ex) {
                Logger.getLogger(PrecipitationExporter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
