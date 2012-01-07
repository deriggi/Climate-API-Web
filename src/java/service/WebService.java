/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import domain.web.EnsembleDatum;
import domain.web.GcmDatum;
import export.DataFormatHelper;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.ResponseConstants;
import shapefileloader.ensemble.P_EnsembleConfig;
import shapefileloader.ensemble.P_EnsembleConfigAreaDao;
import shapefileloader.gcm.P_GcmConfig;
import shapefileloader.gcm.P_GcmConfigAreaDao;
import shapefileloader.gcm.P_GcmStatsProperties;
import shapefileloader.gcm.P_GcmStatsProperties.stat_type;

/**
 *
 * @author wb385924
 */
public class WebService {

    private static Logger log = Logger.getLogger(WebService.class.getName());

    public enum TempAgg {

        ANNUAL(true), MONTHLY(false);
        private boolean isAnnual;

        public boolean isIsAnnual() {
            return isAnnual;
        }

        TempAgg(boolean isAnnual) {
            this.isAnnual = isAnnual;
        }
    }

    public enum Delivery {

        DOWNLOAD(true), WEB(false);
        private boolean download;

        public boolean isDownload() {
            return download;
        }

        Delivery(boolean isDownload) {
            this.download = isDownload;
        }
    }
    private static WebService webService = null;
    private static HashMap<String, P_GcmStatsProperties.stat_type> statTypeMap = new HashMap<String, P_GcmStatsProperties.stat_type>();
    private static HashMap<String, Boolean> isAnnualMap = new HashMap<String, Boolean>();
    private static HashMap<String, WebService.TempAgg> tempAggMap = new HashMap<String, WebService.TempAgg>();

    public static WebService get() {

        if (webService == null) {
            webService = new WebService();
            initMap();
        }

        return webService;
    }

    public P_GcmStatsProperties.stat_type getStatType(String key) {
        if (key == null) {
            return null;
        }
        return statTypeMap.get(key);
    }

    public boolean isAnnual(String key) {
        if (key == null || !isAnnualMap.containsKey(key)) {
            return false;
        }
        return isAnnualMap.get(key);
    }

    public WebService.TempAgg geTempAgg(String key) {
        if (key == null || !tempAggMap.containsKey(key)) {
            return WebService.TempAgg.MONTHLY;
        }
        return tempAggMap.get(key);
    }

    private static void initMap() {
        statTypeMap.put("mavg", stat_type.mean);
        statTypeMap.put("annualavg", stat_type.mean);
        statTypeMap.put("manom", stat_type.anom);
        statTypeMap.put("annualanom", stat_type.anom);

        isAnnualMap.put("mavg", false);
        isAnnualMap.put("annualavg", true);
        isAnnualMap.put("manom", false);
        isAnnualMap.put("annualanom", true);


        tempAggMap.put("mavg", WebService.TempAgg.MONTHLY);
        tempAggMap.put("annualavg", WebService.TempAgg.ANNUAL);
        tempAggMap.put("manom", WebService.TempAgg.MONTHLY);
        tempAggMap.put("annualanom", WebService.TempAgg.ANNUAL);
    }

    /**
     *          // L3  No scenario required
     *
     * @param statName
     * @param fyear
     * @param tyear
     * @param iso3
     * @param statType
     * @param isAnnual
     * @param format
     * @param download
     * @return
     */
    public Response getAllGcmAllScenario(
            String statName,
            int fyear,
            int tyear,
            int areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {

        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(statType, null, null, stat, fyear, tyear);
        if (stat == null || statType == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);
//        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getAllScenarioAllGcmData(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatGcms(format, gcms);
         List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getGcmData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatGcms(format, gcms);
        
         Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }

    public Response getGcmProjections(
            String statName,
            int areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {

        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(statType, null, null, stat, 2000, -1);
        if (stat == null || statType == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);
//        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getAllScenarioAllGcmData(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatGcms(format, gcms);
         List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getGcmData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatGcms(format, gcms);

         Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }

     public Response getEnsembleProjections(String statName,  int areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {

        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);

        P_EnsembleConfig config = new P_EnsembleConfig(statType, null, stat, 2000, -1, -1);
        if (stat == null || statType == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);

//        List<EnsembleDatum> ensembles = P_EnsembleConfigAreaDao.get().getAllScenarioAreaDataForTime(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatEnsembles(format, ensembles);

        List<EnsembleDatum> ensembles = P_EnsembleConfigAreaDao.get().getEnsembleData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatEnsembles(format, ensembles);

         Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }
    /**
     * L3 Ensemble
     * 
     * @param statName
     * @param fyear
     * @param tyear
     * @param percentile
     * @param areaId
     * @param statType
     * @param agg
     * @param format
     * @param delivery
     * @return
     */
    public Response getAllScenarioEnsemble(String statName, int fyear, int tyear, int areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {

        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);

        P_EnsembleConfig config = new P_EnsembleConfig(statType, null, stat, fyear, tyear, -1);
        if (stat == null || statType == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);

//        List<EnsembleDatum> ensembles = P_EnsembleConfigAreaDao.get().getAllScenarioAreaDataForTime(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatEnsembles(format, ensembles);

        List<EnsembleDatum> ensembles = P_EnsembleConfigAreaDao.get().getEnsembleData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatEnsembles(format, ensembles);

        Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }


    public Response getSingleScenarioAllPercentile(String scenarioName, String statName, int fyear, int tyear,  int areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {

        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_EnsembleConfig config = new P_EnsembleConfig(statType, scenario, stat, fyear, tyear, -1);
        if (stat == null || statType == null || scenario == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);

        List<EnsembleDatum> ensembles = P_EnsembleConfigAreaDao.get().getEnsembleData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatEnsembles(format, ensembles);

//        List<EnsembleDatum> ensembles = P_EnsembleConfigAreaDao.get().getSingleScenarioAllPercentile(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatEnsembles(format, ensembles);


       Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }


    public Response getSinglePercentileAllScenario(int percentiel, String statName, int fyear, int tyear,  int areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {

        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);

        P_EnsembleConfig config = new P_EnsembleConfig(statType, null, stat, fyear, tyear, percentiel);
        if (stat == null || statType == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);

        List<EnsembleDatum> ensembles = P_EnsembleConfigAreaDao.get().getEnsembleData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatEnsembles(format, ensembles);


        Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }





    public Response getEnsembleScenario(String scenarioName, String statName, int fyear, int tyear, int percentile, int areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_EnsembleConfig config = new P_EnsembleConfig(statType, scenario, stat, fyear, tyear, percentile);
        if (!config.isCompleteIgnoringAreaValueMonth()) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);

//        HashMap<Integer, Double> data = P_EnsembleConfigAreaDao.get().getAreaDataForTime(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatMonthlyOrAnnualMap(format, data);

         List<EnsembleDatum> ensembles = P_EnsembleConfigAreaDao.get().getEnsembleData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatEnsembles(format, ensembles);


         Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }

    /**
     *
     * L2
     *
     * @param scenarioName
     * @param statName
     * @param fyear
     * @param tyear
     * @param areaId
     * @param statType
     * @param agg
     * @param format
     * @return
     */
    public Response getSingleScenarioData(
            String scenarioName,
            String statName,
            int fyear,
            int tyear,
            int areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {


        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(statType, null, scenario, stat, fyear, tyear);
        if (stat == null || statType == null || scenario == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);
//        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getAllGcmAreaData(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatGcms(format, gcms);
        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getGcmData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatGcms(format, gcms);

         Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }




    public Response getSingleGcmData(
            String gcmName,
            String statName,
            int fyear,
            int tyear,
            int areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {


        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(statType, gcm, null, stat, fyear, tyear);
        if (stat == null || statType == null || gcm == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);
        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getGcmData(config, agg.isAnnual);
        String formattedData = DataFormatHelper.formatGcms(format, gcms);

         Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }

    /**
     *  L1
     * @param gcmName
     * @param scenarioName
     * @param statName
     * @param fyear
     * @param tyear
     * @param iso3
     * @param format
     * @return
     */
    public Response getGcmScenario(
            String gcmName,
            String scenarioName,
            String statName,
            int fyear,
            int tyear,
            int  areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(statType, gcm, scenario, stat, fyear, tyear);
        if (!config.isCompleteIgnoringAreaValueMonth()) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);
        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getGcmData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatGcms(format, gcms);

//        HashMap<Integer, Double> data = P_GcmConfigAreaDao.get().getAreaDataForTime(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatMonthlyOrAnnualMap(format, data);

         Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }

     /**
     *  L1
     * @param gcmName
     * @param scenarioName
     * @param statName
     * @param fyear
     * @param tyear
     * @param iso3
     * @param format
     * @return
     */
    public Response getGcmScenarioAllVariables(
            String gcmName,
            String scenarioName,
            int fyear,
            int tyear,
            int  areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(statType, gcm, scenario, null, fyear, tyear);
        if (gcm == null || scenario == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);
        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getGcmData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatGcms(format, gcms);

//        HashMap<Integer, Double> data = P_GcmConfigAreaDao.get().getAreaDataForTime(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatMonthlyOrAnnualMap(format, data);

         Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }

    public Response.ResponseBuilder getProperResponse(String formattedData, Delivery delivery, String format){
        Response.ResponseBuilder r;
        if (delivery.equals(Delivery.WEB)) {
            r = Response.ok(formattedData, format);
        } else {
            String extension = null;
            if(format.equals(MediaType.TEXT_PLAIN)){
                extension = ".csv";
            }else if(format.equals(MediaType.APPLICATION_XML)){
                extension = ".xml";
            }else{
                extension = ".txt";
            }
            r = Response.ok(formattedData, format).header("Content-Disposition", "attachement; filename=download" + extension);
        }
        return r;
    }

     /**
     *  L1
     * @param gcmName
     * @param scenarioName
     * @param statName
     * @param fyear
     * @param tyear
     * @param iso3
     * @param format
     * @return
     */
    public Response getGcmAllVariables(
            String gcmName,
            int fyear,
            int tyear,
            int  areaId, stat_type statType, TempAgg agg, String format, Delivery delivery) {
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(statType, gcm, null, null, fyear, tyear);
        if (gcm == null ) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(areaId);
        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getGcmData(config, agg.isIsAnnual());
        String formattedData = DataFormatHelper.formatGcms(format, gcms);

//        HashMap<Integer, Double> data = P_GcmConfigAreaDao.get().getAreaDataForTime(config, agg.isIsAnnual());
//        String formattedData = DataFormatHelper.formatMonthlyOrAnnualMap(format, data);

         Response.ResponseBuilder r = null;
        r = getProperResponse(formattedData, delivery, format);
        return r.build();
    }
}
