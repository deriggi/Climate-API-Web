/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oldv1;

import com.google.gson.Gson;
import dao.countryboundary.CountryPrecipitationDao;
import domain.web.ClimateDatum;
import domain.web.CountryRain;
import domain.web.PrecipitationData;
import export.PrecipitationExporter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import java.util.HashMap;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import sdnis.wb.util.BasicAverager;

/**
 *
 * @author wb385924
 */
@Path("/precipitation")
public class PrecpitationResource {

    private SimpleDateFormat sdf = new SimpleDateFormat();

    @GET
    @Path("/country/{longitude},{latitude}")
    @Produces(MediaType.APPLICATION_JSON)
    public ClimateDatum getPrecipInContainingCountry(@PathParam("latitude") double latitude, @PathParam("longitude") double longitude) {
//
//        CountryPrecipitationDao dao = CountryPrecipitationDao.get();
//        HashMap<String,String> props = dao.getRegionPropertiesContainingPoint(latitude, longitude);
//
//        int id = -1;
//
//        if(props.get("id") != null){
//            id = Integer.parseInt(props.get("id"));
//        }
//
//        List<CountryRain> data = dao.getRainData(id);
//
//        ClimateDatum cd = new ClimateDatum();
//        cd.setMetadata(props);
//        cd.setData(data);
        
        return null;//cd;
        

        //return dao.getRainData(id);
    }

    @GET
    @Path("/country/{fyear}-{fmonth}/{tyear}-{tmonth}/{statprefix}/{longitude},{latitude}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPrecipInContainingCountry(@PathParam("fyear") int fyear, @PathParam("fmonth") int fmonth, @PathParam("tyear") int tyear, @PathParam("tmonth") int tmonth, @PathParam("statprefix") String statPrefix, @PathParam("latitude") double latitude, @PathParam("longitude") double longitude) {

//        Calendar fromDate = Calendar.getInstance();
//        fromDate.set(Calendar.YEAR, fyear);
//        fromDate.set(Calendar.MONTH, fmonth-1);
//        fromDate.set(Calendar.DATE,1);
//
//        Calendar toDate = Calendar.getInstance();
//        toDate.set(Calendar.YEAR, tyear);
//        toDate.set(Calendar.MONTH, tmonth-1);
//        toDate.set(Calendar.DATE,1);
//
//
//
//        CountryPrecipitationDao dao = CountryPrecipitationDao.get();
//        HashMap<String,String> props = dao.getRegionPropertiesContainingPoint(latitude, longitude);
//
//        int id = -1;
//
//        if(props.get("id") != null){
//            id = Integer.parseInt(props.get("id"));
//        }
//
//
//        int dotIndex = -1;
//        if(  (dotIndex = statPrefix.indexOf("."))   != -1){
//            String format = statPrefix.substring(dotIndex+1);
//            if(format.equalsIgnoreCase("csv")){
//                return PrecipitationExporter.exportDataTo(id, PrecipitationExporter.Format.CSV,statPrefix.substring(0,dotIndex),fromDate.getTime(),toDate.getTime());
//            }else{
//                return PrecipitationExporter.exportDataTo(id, PrecipitationExporter.Format.JSON,statPrefix.substring(0,dotIndex),fromDate.getTime(),toDate.getTime());
//            }
//
//        }
        return null;//PrecipitationExporter.exportDataTo(id, PrecipitationExporter.Format.JSON,statPrefix,fromDate.getTime(),toDate.getTime());

        


        //return dao.getRainData(id);
    }

    @GET
    @Path("/country/{fyear}-{fmonth}/{tyear}-{tmonth}/{statprefix}/{iso3}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPrecipInContainingCountryIso(@PathParam("fyear") int fyear, @PathParam("fmonth") int fmonth, @PathParam("tyear") int tyear, @PathParam("tmonth") int tmonth, @PathParam("statprefix") String statPrefix, @PathParam("iso3") String iso3) {

//        Calendar fromDate = Calendar.getInstance();
//        fromDate.set(Calendar.YEAR, fyear);
//        fromDate.set(Calendar.MONTH, fmonth-1);
//        fromDate.set(Calendar.DATE,1);
//
//        Calendar toDate = Calendar.getInstance();
//        toDate.set(Calendar.YEAR, tyear);
//        toDate.set(Calendar.MONTH, tmonth-1);
//        toDate.set(Calendar.DATE,1);
//
//        int dotIndex = -1;
//        if(  (dotIndex = statPrefix.indexOf("."))   != -1){
//            String format = statPrefix.substring(dotIndex+1);
//            if(format.equalsIgnoreCase("csv")){
//                return PrecipitationExporter.exportDataTo(iso3, PrecipitationExporter.Format.CSV,statPrefix.substring(0,dotIndex),fromDate.getTime(),toDate.getTime());
//            }else{
//                return PrecipitationExporter.exportDataTo(iso3, PrecipitationExporter.Format.JSON,statPrefix.substring(0,dotIndex),fromDate.getTime(),toDate.getTime());
//            }
//
//        }
        return null;//PrecipitationExporter.exportDataTo(iso3, PrecipitationExporter.Format.JSON,statPrefix,fromDate.getTime(),toDate.getTime());




        //return dao.getRainData(id);
    }

     @GET
    @Path("/monthavg/{fyear}-{fmonth}/{tyear}-{tmonth}/{statprefix}/{iso3}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMonthAveragePrecipInContainingCountryIso(@PathParam("fyear") int fyear, @PathParam("fmonth") int fmonth, @PathParam("tyear") int tyear, @PathParam("tmonth") int tmonth, @PathParam("statprefix") String statPrefix, @PathParam("iso3") String iso3) {

//        Calendar fromDate = Calendar.getInstance();
//        fromDate.set(Calendar.YEAR, fyear);
//        fromDate.set(Calendar.MONTH, fmonth-1);
//        fromDate.set(Calendar.DATE,1);
//
//        Calendar toDate = Calendar.getInstance();
//        toDate.set(Calendar.YEAR, tyear);
//        toDate.set(Calendar.MONTH, tmonth-1);
//        toDate.set(Calendar.DATE,1);
//
//
//
//        CountryPrecipitationDao dao = CountryPrecipitationDao.get();
//        HashMap<String,BasicAverager> averages = dao.averageByMonth(dao.getRainDataAsMap(iso3, statPrefix, fromDate.getTime(),toDate.getTime()), "rainDate", "sum");
//        Gson gson = new Gson();
//
        return null;//gson.toJson(averages);

        //return dao.getRainData(id);
    }

//
//    @GET
//    @Path("/country/{iso3}/{statprefix}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getPrecipInContainingCountry(@PathParam("iso3") String iso3,  @PathParam("statprefix") String statPrefix) {
//
//
//        int dotIndex = -1;
//        if(  (dotIndex = statPrefix.indexOf("."))   != -1){
//            String format = statPrefix.substring(dotIndex+1);
//            if(format.equalsIgnoreCase("csv")){
//                return PrecipitationExporter.exportDataTo(iso3, PrecipitationExporter.Format.CSV,statPrefix.substring(0,dotIndex));
//            }else{
//                return PrecipitationExporter.exportDataTo(iso3, PrecipitationExporter.Format.JSON,statPrefix.substring(0,dotIndex));
//            }
//
//        }
//        return PrecipitationExporter.exportDataTo(iso3, PrecipitationExporter.Format.JSON,statPrefix);
//
//
//
//    }

    @GET
    @Path("/regions/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClimateDatum> getPrecipWithinShape(@QueryParam("bounds") String polygon, @QueryParam("from") long from, @QueryParam("to") long to){

        return null;//CountryPrecipitationDao.get().getRainDataWitinRegion(polygon, new Date(from), new Date(to));

    }
}
