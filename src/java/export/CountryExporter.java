/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package export;

import com.google.gson.Gson;
import dao.country.CountryDao;
import database.DBUtils;
import domain.Country;
import export.util.FileExportHelper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wb385924
 */
public class CountryExporter {

    private static Logger log = Logger.getLogger(CountryExporter.class.getName());

     private final String GET_SCALED_COUNTRY_SVG = "select country_iso_3,   st_assvg( st_scale(basin_geom,10.0,10.0) ,1,6 ) , country_iso_3 from basin inner join country on basin_country_id = country_id where basin_id = ?";


    private final String GET_SIMPLE_SCALED_COUNTRY_SVG = "select country_iso_3, st_assvg(  st_scale(st_simplify(country_boundary_shape,0.09),10.0,10.0) ,1,6 ), country_iso_3 from country_boundary inner join country on country_boundary_country_id = country_id ";
    //private final String GET_BASIN_IDS_WHERE_COUNTRY_BOUNDARIES_EXIST = "select  basin_id from basin inner join country_boundary on basin_country_id = country_boundary_country_id inner join country on basin_country_id = country_id ";


    private String exportSimpleSVG(){
        Connection c = DBUtils.getConnection();
        PreparedStatement ps = null;
        try {

            ps = c.prepareStatement(GET_SIMPLE_SCALED_COUNTRY_SVG);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                StringBuilder sb = new StringBuilder();
                String iso3 = rs.getString(1);
                String svg = rs.getString(2);
                sb.append(iso3);
                sb.append("|");
                sb.append(svg);
                sb.append(System.getProperty("line.separator"));
                FileExportHelper.appendToFile("countrysvg.txt","sb.toString()");
                
                log.log(Level.FINE, "we got {0} and {1} ",new String[]{iso3,svg});
                
            }


        } catch (SQLException ex) {
            log.log(Level.SEVERE,"error exporting  ",ex);
        } finally{
            DBUtils.close(c, ps, null);
        }
        return null;
    }

     

     public HashMap<String,String> getCountryCodeMap(){
         CountryDao dao = CountryDao.get();
         List<Country> countries = dao.getCountries();
         log.log(Level.INFO,"have country list of size {0}",countries.size());
         HashMap<String,String> codeMap = new HashMap<String,String>();
         for(Country c : countries){
            if(c.getIso2() != null && c.getIso3() != null){
             codeMap.put(c.getIso2(), c.getIso3());
             }
         }
         log.log(Level.INFO,"returing code map of size {0}",codeMap.size());
         
         return codeMap;
     }

    public void exportCountries(){
        CountryDao dao = CountryDao.get();
        String json = new Gson().toJson(dao.getMinimalCountries());
        log.info(json);
        new HttpExporter().postMessage(json, "http://climateportal2.appspot.com/country");
        log.info("done exporting");
    }
    
    public static void main(String[] args){
        HashMap<String,String> codeMap = new CountryExporter().getCountryCodeMap();
        FileExportHelper.writeToFile("countrycodes.json", new Gson().toJson(codeMap));
    }

}
