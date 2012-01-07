/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.v1.basin;


import com.sun.jersey.spi.resource.Singleton;
import dao.GeoDao;
import dao.basin.BasinDao;
import database.DBUtils;
import java.sql.Connection;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import service.WebService;
import util.RequestUtil;
import service.WebService.Delivery;

/**
 *
 * @author wb385924
 */
@Singleton
@Path("/v1/basin/")
public class P_BasinDataResource {


    private static final Logger log = Logger.getLogger(P_BasinDataResource.class.getName());

     @GET
    @Path("/kml/{wbhuc : \\d{1,6}}")
    @Produces(MediaType.APPLICATION_XML)
    public String getKmlCountryBoundary(@PathParam("wbhuc") int wbhuc) {

        int basinId = -1;
        Connection c = DBUtils.getConnection();
        try {
            basinId = GeoDao.getEntityId(c, "basin", "code", wbhuc);
            String kml = GeoDao.getGeometryAsKML(c, "basin", "geom", "id", basinId);
            return kml;
        } finally {
            DBUtils.close(c);
        }




    }

    // ================= L1 GCM =======================
    @GET
    @Path("/{tempagg}/{gcm:\\w{8,15}}/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getGcmScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {
            log.info("getting basin single gcm single scenario");
          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));
        return WebService.get().getGcmScenario(gcmName, scenarioName, statName, fyear, tyear, basinId ,WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg),mediaType, Delivery.WEB);
    }

    // ================= L1 GCM download =======================
    @GET
    @Path("/dl/{tempagg}/{gcm:\\w{8,15}}/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadGcmScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
             @PathParam("wbhuc") String wbhuc) {

          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        return WebService.get().getGcmScenario(gcmName, scenarioName, statName, fyear, tyear, basinId ,WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg),mediaType, Delivery.DOWNLOAD);
    }


      // ===================== L2 gcm   ============================//
    @GET
    @Path("/{tempagg}/{gcm:\\w{8,15}}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getGcmData(
            @PathParam("tempagg") String tempagg,
            @PathParam("gcm") String gcmName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {
            log.info("getting basin single gcm");
         String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));


        return WebService.get().getSingleGcmData(gcmName,statName, fyear, tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, WebService.Delivery.WEB);//getScenarioDataDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }


    // ===================== L2 gcm  Download ============================//
    @GET
    @Path("/dl/{tempagg}/{gcm:\\w{8,15}}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadGcmData(
            @PathParam("tempagg") String tempagg,
            @PathParam("gcm") String gcmName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {

        String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));


        return WebService.get().getSingleGcmData(gcmName, statName, fyear, tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, WebService.Delivery.DOWNLOAD);//getScenarioDataDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }

     // ===================== L2 Gcm  ============================//
    @GET
    @Path("/{tempagg}/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getScenarioData(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
           @PathParam("wbhuc") String wbhuc) {

        log.info("getting basin single scenario");
          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);

        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));
        return WebService.get().getSingleScenarioData(scenarioName, statName, fyear, tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, WebService.Delivery.WEB);//getScenarioDataDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }


    // ===================== L2 Gcm  Download ============================//
    @GET
    @Path("/dl/{tempagg}/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadScenarioData(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {

          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        return WebService.get().getSingleScenarioData(scenarioName, statName, fyear, tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, WebService.Delivery.DOWNLOAD);//getScenarioDataDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }



    // =================  L2 ensemble =============================
    @GET
    @Path("/{tempagg}/ensemble/{scenario:(?i)(20c3m|a2|b1)}/{percentile}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getEnsembleScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
           @PathParam("wbhuc") String wbhuc) {
           log.info("getting basin scenario percentile");
          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        return WebService.get().getEnsembleScenario(scenarioName, statName, fyear, tyear, percentile, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // =================  L2 ensemble =============================
    @GET
    @Path("/dl/{tempagg}/ensemble/{scenario:(?i)(20c3m|a2|b1)}/{percentile}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadEnsembleScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("wbhuc") String wbhuc) {

          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        return WebService.get().getEnsembleScenario(scenarioName, statName, fyear, tyear, percentile, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
    }



        // ===============  gcm projections  ==================
    @GET
    @Path("/{tempagg}/{stat}/projection/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getGcmProjections(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("wbhuc") String wbhuc) {
            log.info("getting basin all projections");
         String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        return WebService.get().getGcmProjections(statName, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }



      // ===============  ensemble projections  ==================
    @GET
    @Path("/{tempagg}/ensemble/{stat}/projection/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getEnsembleProjections(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
             @PathParam("wbhuc") String wbhuc) {
            log.info("getting basin projections");
       String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));



        return WebService.get().getEnsembleProjections(statName, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

      // =============== download ensemble projections  ==================
    @GET
    @Path("/dl/{tempagg}/ensemble/{stat}/projection/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadEnsembleProjections(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
             @PathParam("wbhuc") String wbhuc) {
            log.info("getting basin  projections");
        String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));


        return WebService.get().getEnsembleProjections(statName, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
    }




    // =================  L3 ensemble  =============================
    @GET
    @Path("/{tempagg}/ensemble/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getAllScenarioEnsemble(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {
            log.info("getting basin all scenario all percnetile");
          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));
        return WebService.get().getAllScenarioEnsemble(statName, fyear, tyear,  basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

     // =================  L3 ensemble  download  =============================

    @GET
    @Path("/dl/{tempagg}/ensemble/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadAllScenarioEnsemble(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {

          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        return WebService.get().getAllScenarioEnsemble(statName, fyear, tyear,  basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);

    }


     // =================  ensemble single scenario all percentile =============================
    @GET
    @Path("/{tempagg}/ensemble/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getSingleScenarioAllPercentile(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {
            log.info("getting basin single scenario all percentile");
         String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        return WebService.get().getSingleScenarioAllPercentile(scenarioName, statName,fyear,tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // =================  download ensemble single scenario all percentile =============================
    @GET
    @Path("/dl/{tempagg}/ensemble/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadSingleScenarioAllPercentile(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {

         String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        return WebService.get().getSingleScenarioAllPercentile(scenarioName, statName,fyear,tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
    }


     // =================  get ensemble single percentile all scenario =============================
    @GET
    @Path("/{tempagg}/ensemble/{percentile:\\d{2}}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getSinglePercentileAllScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("wbhuc") String wbhuc) {
            log.info("getting basin ensemble single percentile all scenario");
        String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));


        return WebService.get().getSinglePercentileAllScenario(percentile, statName,fyear,tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // =================  download ensemble single percentile all scenario =============================
    @GET
    @Path("/dl/{tempagg}/ensemble/{percentile:\\d{2}}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadSinglePercentileAllScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("wbhuc") String wbhuc) {

        String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        return WebService.get().getSinglePercentileAllScenario(percentile, statName,fyear,tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
    }


   
    // ===============  L3  Gcm  ==================
    @GET
    @Path("/{tempagg}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getAllGcmAllScenarioAllGcmMonthlyDataIso(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {
            log.info("getting basin all gcm all scenarios");
          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));

        
        return WebService.get().getAllGcmAllScenario(statName, fyear, tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }


    // ========  L3 Gcm Download  ===================  //
    @GET
    @Path("/dl/{tempagg}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadAllGcmAllScenarioAllGcmMonthlyDataIso(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {

          String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);
        int basinId = BasinDao.get().getBasinIdFromCode(Integer.parseInt(identifier));


        Response r = WebService.get().getAllGcmAllScenario(statName, fyear, tyear, basinId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
        return r;
    }

    
   
//    private Response getScenarioDataDelegate(
//            String scenarioName,
//            String statName,
//            int fyear,
//            int tyear,
//            String iso3, String format) {
//        int countryId = CountryService.get().getId(iso3);
//        log.log(Level.INFO, "getting country id is {0}", countryId);
//        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
//        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
//        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);
//
//        //@TODO validate the config here
//        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.mean, null, scenario, stat, fyear, tyear);
//        if (scenario == null || stat == null) {
//            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
//            return Response.ok(errorResponse, format).build();
//        }
//        config.setAreaId(countryId);
//        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getAllGcmAreaData(config, false);
//        String formattedData = DataFormatHelper.formatGcms(format, gcms);
//
//        Response.ResponseBuilder builder = Response.ok(formattedData, format);
//        return builder.build();
//    }


//    private Response getAllScenarioEnsembleDelegate( String statName, int fyear, int tyear, int percentile, String iso3, String format) {
//        int countryId = CountryService.get().getId(iso3);
//        log.log(Level.INFO, "getting country id is {0}", countryId);
//        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
//        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
//
//        P_EnsembleConfig config = new P_EnsembleConfig(P_GcmStatsProperties.stat_type.mean, null, stat, fyear, tyear, percentile);
//        if (stat == null ) {
//            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
//            return Response.ok(errorResponse, format).build();
//        }
//        config.setAreaId(countryId);
//        List<EnsembleDatum> ensembles =  P_EnsembleConfigAreaDao.get().getAllScenarioAreaDataForTime(config, false);
//
//        String formattedData = DataFormatHelper.formatEnsembles(format, ensembles);
//        Response.ResponseBuilder builder = Response.ok(formattedData, format);
//
//        return builder.build();
//    }
    
    // ============  aggregate across all gcms ==================//
//    @GET
//    @Path("/{scenario}/{stat}/{fyear}/{tyear}/{iso3}")
//    public Response getAllMonthlyDataIso(
//            @PathParam("scenario") String scenarioName,
//            @PathParam("stat") String statName,
//            @PathParam("fyear") int fyear,
//            @PathParam("tyear") int tyear,
//            @PathParam("iso3") String iso3) {
//
//        if (iso3 == null || iso3.length() < 3) {
//            return Response.ok(ResponseConstants.INVALID_ISO, MediaType.APPLICATION_JSON).build();
//        }
//        if (iso3 != null && (iso3.contains(ResponseConstants.xml) || iso3.contains(ResponseConstants.XML))) {
//            return getAllMonthlyDataIsoDelegate(scenarioName, statName, fyear, tyear, iso3.substring(0, iso3.indexOf(ResponseConstants.DOT)), MediaType.APPLICATION_XML);
//        }
//
//        return getAllMonthlyDataIsoDelegate(scenarioName, statName, fyear, tyear, iso3, MediaType.APPLICATION_JSON);
//    }
 
//    private Response getAllGcmAllScenarioMonthlyDataIsoDelegate(
//            String statName,
//            int fyear,
//            int tyear,
//            String iso3, String format, boolean download) {
//        int countryId = CountryService.get().getId(iso3);
//        log.log(Level.INFO, "getting country id is {0}", countryId);
//        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
//        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
//
//        //@TODO validate the config here
//        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.mean, null, null, stat, fyear, tyear);
//        if ( stat == null) {
//            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
//            return Response.ok(errorResponse, format).build();
//        }
//        config.setAreaId(countryId);
//        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getAllScenarioAllGcmData(config, false);
//        String formattedData = DataFormatHelper.formatGcms(format, gcms);
//        Response.ResponseBuilder r = null;
//        if(!download){
//            r =  Response.ok(formattedData, format);
//        }
//        else{
//            r = Response.ok(formattedData, format).header("Content-Disposition", "attachement; filename=download.csv");
//        }
//        return r.build();
//    }
}
